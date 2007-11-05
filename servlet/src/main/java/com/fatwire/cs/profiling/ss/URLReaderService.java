package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.events.PageletRenderedEvent;
import com.fatwire.cs.profiling.ss.events.PageletRenderingListener;
import com.fatwire.cs.profiling.ss.util.HelperStrings;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class URLReaderService {
    private final Log log = LogFactory.getLog(getClass());

    private volatile boolean stopped = false;

    private HostConfig hostConfig;

    private SSUriHelper uriHelper;

    private HttpClient client;

    private volatile int count = 0;

    private int maxPages = Integer.MAX_VALUE;

    private final Set<PageletRenderingListener> listeners = new CopyOnWriteArraySet<PageletRenderingListener>();

    private final List<String> startUrls;

    private final Scheduler scheduler;

    /**
     * @param urlsToDo
     */
    public URLReaderService(final List<String> urlsToDo,
            final ThreadPoolExecutor readerPool) {
        super();
        this.startUrls = urlsToDo;
        scheduler = new Scheduler(readerPool);

    }

    void initClient() {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        client.getHttpConnectionManager().getParams().setConnectionTimeout(
                30000);
        client.getHttpConnectionManager().getParams()
                .setDefaultMaxConnectionsPerHost(15);
        client.getHostConfiguration().setHost(hostConfig.getHostname(),
                hostConfig.getPort());
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
        final List<String> initialToDo = new ArrayList<String>(startUrls);

        for (final String thingToDo : initialToDo) {
            scheduler.schedulePage(thingToDo);
        }
        scheduler.waitForlAllTasksToFinish();

    }

    class Scheduler {
        private final List<String> urlsToDo = new ArrayList<String>();

        private final Set<String> urlsDone = new HashSet<String>();

        private final ThreadPoolExecutor readerPool;

        private final AtomicBoolean complete = new AtomicBoolean(false);

        private final Object lock = new Object();

        /**
         * @param readerPool
         */
        public Scheduler(final ThreadPoolExecutor readerPool) {
            super();
            this.readerPool = readerPool;
        }

        void schedulePage(final String uriToDo) {
            if (count++ > maxPages) {
                return;
            }
            String uri = checkUri(uriToDo);
            urlsDone.add(uri);
            final UrlRenderingCallable downloader = new UrlRenderingCallable(
                    client, uri, uriHelper);

            try {
                //                final FutureTask<ResultPage> ft = new FutureTask<ResultPage>(
                //                        downloader);
                //                readerPool.submit(ft);
                //readerPool.submit(new CollectorTask(ft));
                readerPool.execute(new Harvester(downloader));

            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }

        }

        String checkUri(String uri) {
            if (uri.indexOf(HelperStrings.SS_PAGEDATA_REQUEST) == -1) {
                return uri + HelperStrings.SS_PAGEDATA_REQUEST + "=true";
            }
            return uri;
        }

        void pageComplete(final ResultPage page) {

            for (final SSUri ssUri : page.getMarkers()) {
                String url = uriHelper.toLink(ssUri);
                if (!urlsDone.contains(url) && !urlsToDo.contains(url)) {
                    log.debug("adding " + url);
                    schedulePage(url);
                }
            }
            for (final SSUri ssUri : page.getLinks()) {
                String url = uriHelper.toLink(ssUri);
                if (!urlsDone.contains(url) && !urlsToDo.contains(url)) {
                    log.debug("adding " + url);
                    schedulePage(url);
                }
            }
            final PageletRenderedEvent event = new PageletRenderedEvent(this,
                    page);
            for (final PageletRenderingListener listener : listeners) {
                listener.renderPerformed(event);
            }
        }

        public void taskFinished() {
            log.debug(readerPool.getActiveCount() + " "
                    + readerPool.getQueue().size());
            if (readerPool.getActiveCount() == 1
                    && readerPool.getQueue().size() == 0) {
                this.complete.set(true);
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
                    } catch (InterruptedException e) {
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
            ResultPage page;
            try {
                page = downloader.call();
                scheduler.pageComplete(page);
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                scheduler.taskFinished();
            }

        }

    }

    class CollectorTask implements Runnable {
        final Future<ResultPage> future;

        /**
         * @param future
         */
        public CollectorTask(final Future<ResultPage> future) {
            super();
            this.future = future;
        }

        public void run() {

            try {

                final ResultPage page = future.get();
                scheduler.pageComplete(page);

            } catch (final InterruptedException e) {
                log.error(e.getMessage(), e);
            } catch (final ExecutionException e) {
                log.error(e.getMessage(), e);
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
        this.listeners.add(listener);
    }

    public void removeListener(final PageletRenderingListener listener) {
        this.listeners.remove(listener);
    }

    public void setHostConfig(HostConfig hostConfig) {
        this.hostConfig = hostConfig;
        this.uriHelper = new SSUriHelper(hostConfig.getDomain());

    }

}
