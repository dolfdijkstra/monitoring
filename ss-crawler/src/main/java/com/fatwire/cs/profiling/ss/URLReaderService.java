package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
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
import com.fatwire.cs.profiling.ss.jobs.ProgressMonitor;
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

    private final List<QueryString> startUrls = new ArrayList<QueryString>();

    private final Scheduler scheduler;

    public URLReaderService(final Executor readerPool) {
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

        // RFC 2101 cookie management spec is used per default
        // to parse, validate, format & match cookies
        //client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        //client.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        //client.getParams().makeStrict();
        client.getParams().getDefaults().setBooleanParameter(
                HttpMethodParams.SINGLE_COOKIE_HEADER, true);
        final HttpState initialState = new HttpState();

        initialState.addCookie(new Cookie(hostConfig.getHostname(),
                HelperStrings.SS_CLIENT_INDICATOR, Boolean.TRUE.toString(),
                hostConfig.getDomain(), -1, false));

        // set state
        client.setState(initialState);

    }

    public void start(final ProgressMonitor monitor) {
        initClient();

        monitor.beginTask("Crawling on " + hostConfig.toString(),
                maxPages == Integer.MAX_VALUE ? -1 : maxPages);
        scheduler.monitor = monitor;
        for (final QueryString thingToDo : startUrls) {

            scheduler.schedulePage(thingToDo);
        }
        scheduler.waitForlAllTasksToFinish();
        monitor.done();

    }

    class Scheduler {

        private final Set<QueryString> urlsDone = new HashSet<QueryString>();

        private final Executor executor;

        private final AtomicBoolean complete = new AtomicBoolean(false);

        private final Object lock = new Object();

        private final AtomicInteger scheduledCounter = new AtomicInteger();

        private volatile int count = 0;

        private ProgressMonitor monitor;

        /**
         * @param executor
         */
        public Scheduler(final Executor readerPool) {
            super();
            executor = readerPool;
        }

        synchronized void schedulePage(final QueryString qs) {
            if (monitor.isCanceled()) {
                return;
            }

            if (qs instanceof Link) {
                if (++count > maxPages) {
                    return;
                }
            }
            urlsDone.add(qs);
            final String uri = checkUri(qs);

            scheduledCounter.incrementAndGet();
            final UrlRenderingCallable downloader = new UrlRenderingCallable(
                    client, uri, qs);

            try {
                int priority = 0;
                if (qs instanceof Link) {
                    priority = 5;
                }
                executor.execute(new Harvester(downloader, qs.toString(),
                        monitor, priority));

            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }

        }

        private String checkUri(final QueryString ssuri) {
            final String uri = uriHelper.toLink(ssuri);
            if (true) {
                if (ssuri.getParameters().containsKey(
                        HelperStrings.SS_PAGEDATA_REQUEST) == false) {
                    return uri + "&" + HelperStrings.SS_PAGEDATA_REQUEST
                            + "=true";
                }
            } else {
                if (ssuri.getParameters().containsKey(
                        HelperStrings.SS_CLIENT_INDICATOR) == false) {
                    return uri + "&" + HelperStrings.SS_CLIENT_INDICATOR
                            + "=true";
                }

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
            log.debug("Active workers: " + scheduledCounter.get());
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

    class Harvester implements Runnable, Comparable<Harvester> {
        private final UrlRenderingCallable downloader;

        private final String taskInfo;

        private final ProgressMonitor monitor;

        private final int priority;

        /**
         * @param downloader
         */
        public Harvester(final UrlRenderingCallable downloader,
                final String taskInfo, final ProgressMonitor monitor,
                final int priority) {
            super();
            this.downloader = downloader;
            this.taskInfo = taskInfo;
            this.monitor = monitor;
            this.priority = priority;
        }

        public void run() {
            if (monitor.isCanceled()) {
                return;
            }
            try {
                monitor.subTask(taskInfo);
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

        public int compareTo(Harvester o) {
            if (priority != o.priority) {
                return new Integer(priority).compareTo(o.priority);
            } else {
                return downloader.getUri().compareTo(o.downloader.getUri());
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

    public void addStartUris(final Collection<QueryString> uri) {
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
