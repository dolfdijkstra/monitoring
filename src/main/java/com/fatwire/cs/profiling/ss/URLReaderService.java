package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class URLReaderService {
    private final Log log = LogFactory.getLog(getClass());

    private final List<String> urlsToDo;

    private Set<String> urlsDone;

    private String hostname;

    private int port;

    private String path;

    private volatile boolean stopped = false;

    private HttpClient client;

    private ThreadPoolExecutor readerPool;

    BlockingQueue<Runnable> workQueue;

    /**
     * @param urlsToDo
     */
    public URLReaderService(final List<String> urlsToDo) {
        super();
        this.urlsToDo = urlsToDo;

    }

    void initClient() {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        client.getHttpConnectionManager().getParams().setConnectionTimeout(
                30000);
        client.getHttpConnectionManager().getParams()
                .setDefaultMaxConnectionsPerHost(15);
        client.getHostConfiguration().setHost(hostname, port);
        HttpState initialState = new HttpState();

        // Get HTTP client instance
        client.setState(initialState);

        // RFC 2101 cookie management spec is used per default
        // to parse, validate, format & match cookies
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);

    }

    void initPools() {
        workQueue = new LinkedBlockingQueue<Runnable>();
        readerPool = new ThreadPoolExecutor(3, 20, 60, TimeUnit.SECONDS,
                workQueue);

    }

    public void start() {
        initPools();
        initClient();
        List<String> initialToDo = new ArrayList<String>(urlsToDo);

        for (String thingToDo : initialToDo) {
            read(thingToDo);
        }

    }

    public void waitForCompletion() throws InterruptedException {
        Thread.sleep(1000L);
        while (!isFinished()) {
            Thread.sleep(1000L);
        }

    }

    public boolean isFinished() {
        return this.readerPool.getActiveCount() == 0;
    }

    void read(final String thingToDo) {

        urlsDone.add(thingToDo);
        UrlHandler handler = new UrlHandler(client, thingToDo, path);

        try {
            FutureTask<ResultPage> ft = new FutureTask<ResultPage>(handler);
            readerPool.submit(ft);
            readerPool.submit(new CollectorTask(ft));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    void pageComplete(ResultPage page) {
        log.info("page complete: " + page.getUri());

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

                ResultPage page = future.get();
                pageComplete(page);
                for (String url : page.getMarkers()) {
                    if (!urlsDone.contains(url) && !urlsToDo.contains(url)) {
                        log.info("adding " + url);
                        read(url);
                    }
                }
                for (String url : page.getLinks()) {
                    if (!urlsDone.contains(url) && !urlsToDo.contains(url)) {
                        log.info("adding " + url);
                        read(url);
                    }
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            } catch (ExecutionException e) {
                log.error(e.getMessage(), e);
            }

        }

    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(final int port) {
        this.port = port;
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
     * @return the urlsDone
     */
    public Set<String> getUrlsDone() {
        return urlsDone;
    }

    /**
     * @param urlsDone the urlsDone to set
     */
    public void setUrlsDone(final Set<String> urlsDone) {
        this.urlsDone = urlsDone;
    }

}
