package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.events.PageletRenderedEvent;
import com.fatwire.cs.profiling.ss.events.PageletRenderingListener;
import com.fatwire.cs.profiling.ss.handlers.BodyHandler;
import com.fatwire.cs.profiling.ss.util.HelperStrings;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class URLReaderService {
    private final Log log = LogFactory.getLog(getClass());

    private volatile boolean stopped = false;

    private HostConfig hostConfig;

    private SSUriHelper uriHelper;

    private BodyHandler handler;


    private HttpClient client;

    private int maxPages = Integer.MAX_VALUE;

    private final Set<PageletRenderingListener> listeners = new CopyOnWriteArraySet<PageletRenderingListener>();

    private final List<String> startUrls = new ArrayList<String>();

    private final Scheduler scheduler;

    public URLReaderService(final ThreadPoolExecutor readerPool) {
        super();
        scheduler = new Scheduler(readerPool);

    }

    protected void initClient() {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        client.getHttpConnectionManager().getParams().setConnectionTimeout(
                30000);
        client.getHttpConnectionManager().getParams()
                .setDefaultMaxConnectionsPerHost(15);
        client.getHostConfiguration().setHost(hostConfig.getHostname(),
                hostConfig.getPort());
        client.getParams().setParameter(HttpMethodParams.USER_AGENT,
                "SS-Crawler-0.9");
        final HttpState initialState = new HttpState();
        initialState.addCookie(new Cookie(hostConfig.getHostname(),
                HelperStrings.SS_CLIENT_INDICATOR, Boolean.TRUE.toString(),
                hostConfig.getDomain(), -1, false));

        // set state
        client.setState(initialState);

        // RFC 2101 cookie management spec is used per default
        // to parse, validate, format & match cookies
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
    }

    public void start() {
        initClient();

        for (final String thingToDo : startUrls) {

            scheduler.schedulePage(uriHelper.linkToMap(thingToDo));
        }
        scheduler.waitForlAllTasksToFinish();

    }

    class Scheduler {
        //        private final List<QueryString> urlsToDo = new ArrayList<QueryString>();

        private final Set<QueryString> urlsDone = new HashSet<QueryString>();

        private final ThreadPoolExecutor readerPool;

        private final AtomicBoolean complete = new AtomicBoolean(false);

        private final Object lock = new Object();

        private final AtomicInteger scheduledCounter = new AtomicInteger();

        private volatile int count = 0;

        /**
         * @param executor
         */
        public Scheduler(final ThreadPoolExecutor readerPool) {
            super();
            this.readerPool = readerPool;
        }

        synchronized void schedulePage(final QueryString qs) {
            if (count++ > maxPages) {
                return;
            }
            urlsDone.add(qs);
            final String uri = checkUri(qs);
            scheduledCounter.incrementAndGet();
            final UrlRenderingCallable downloader = new UrlRenderingCallable(
                    client, uri, qs);

            try {
                readerPool.execute(new Harvester(downloader));

            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }

        }

        private String checkUri(final QueryString ssuri) {
            final String uri = uriHelper.toLink(ssuri);
            if (ssuri.getParameters().containsKey(
                    HelperStrings.SS_PAGEDATA_REQUEST) == false) {
                return uri + "&" + HelperStrings.SS_PAGEDATA_REQUEST + "=true";
            }
            return uri;
        }

        void pageComplete(final ResultPage page) {
            synchronized (this) {
                for (final QueryString ssUri : page.getMarkers()) {

                    if (!urlsDone.contains(ssUri)) {
                        if (log.isDebugEnabled()) {
                            log.debug("adding " + ssUri);
                        }
                        schedulePage(ssUri);
                    }
                }
                for (final QueryString ssUri : page.getLinks()) {
                    if (!urlsDone.contains(ssUri)) {
                        if (log.isDebugEnabled()) {
                            //String url = uriHelper.toLink(ssUri);
                            log.debug("adding " + ssUri);
                        }
                        schedulePage(ssUri);
                    }
                }
            }
            final PageletRenderedEvent event = new PageletRenderedEvent(page);
            for (final PageletRenderingListener listener : listeners) {
                listener.renderPerformed(event);
            }
        }

        public void taskFinished() {
            log.debug(readerPool.getActiveCount() + " "
                    + readerPool.getQueue().size());
            //            if (executor.getActiveCount() == 1
            //                    && executor.getQueue().size() == 0) {
            if (scheduledCounter.decrementAndGet() == 0) {
                complete.set(true);
                synchronized (lock) {
                    lock.notifyAll();
                }
            }

        }

        void waitForlAllTasksToFinish() {
            synchronized (lock) {
                while (!complete.get()) {
                    try {
                        lock.wait();
                    } catch (final InterruptedException e) {
                        log.warn(e, e);
                    }
                }
            }
        }
    }

    class Harvester implements Runnable {
        final UrlRenderingCallable downloader;

        /**
         * @param downloader
         */
        public Harvester(final UrlRenderingCallable downloader) {
            super();
            this.downloader = downloader;
        }

        public void run() {

            try {
                final ResultPage page;
                page = downloader.call();
                if (page.getBody() != null) {
                    handler.visit(page);
                }
                scheduler.pageComplete(page);
            } catch (final Exception e) {
                log.error(e, e);
            } finally {
                scheduler.taskFinished();
            }

        }

    }

    /**
     * @return the stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * @param stopped the stopped to set
     */
    public void stop() {
        stopped = true;
    }

    /**
     * @return the maxPages
     */
    public int getMaxPages() {
        return maxPages;
    }

    /**
     * @param maxPages the maxPages to set
     */
    public void setMaxPages(final int maxPages) {
        this.maxPages = maxPages;
    }

    public void addListener(final PageletRenderingListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final PageletRenderingListener listener) {
        listeners.remove(listener);
    }

    public void setHostConfig(final HostConfig hostConfig) {
        this.hostConfig = hostConfig;

    }

    public void addStartUris(final Collection<String> uri) {
        startUrls.addAll(uri);
    }

    /**
     * @return the handler
     */
    public BodyHandler getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(final BodyHandler handler) {
        this.handler = handler;
    }

    /**
     * @return the uriHelper
     */
    public SSUriHelper getUriHelper() {
        return uriHelper;
    }

    /**
     * @param uriHelper the uriHelper to set
     */
    public void setUriHelper(final SSUriHelper uriHelper) {
        this.uriHelper = uriHelper;
    }

}
