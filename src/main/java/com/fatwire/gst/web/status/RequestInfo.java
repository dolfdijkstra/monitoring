package com.fatwire.gst.web.status;

import java.lang.Thread.State;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

public class RequestInfo {

    private final String threadName;

    private final WeakReference<Thread> thread;

    private final AtomicLong counter = new AtomicLong();

    private volatile String currentUri;

    private volatile long lastStartTime;

    private volatile long lastNanoStartTime;

    private volatile long lastNanoEndTime;

    private volatile long lastExecutionTime;

    private volatile boolean running = false;

    private volatile String method;

    private volatile String remoteHost;

    /**
     * @param t
     */
    public RequestInfo(final Thread t) {
        super();
        this.threadName = t.getName();
        this.thread = new WeakReference<Thread>(t);
    }

    public boolean isAlive() {
        Thread t = thread.get();
        return t != null && t.isAlive();
    }

    protected String createUri(final HttpServletRequest request) {
        final StringBuilder b = new StringBuilder(150);
        //b.append(request.getMethod());
        //b.append(' ');
        b.append(request.getRequestURI());
        final String q = request.getQueryString();
        if (q != null) {
            b.append("?").append(q);
        }
        return b.toString();
    }

    protected void start(HttpServletRequest request) {
        if (running)
            return;
        counter.incrementAndGet();
        this.lastStartTime = System.currentTimeMillis();
        this.lastNanoStartTime = System.nanoTime();
        this.currentUri = createUri(request);
        this.remoteHost = request.getRemoteHost() + ":"
                + request.getRemotePort();
        this.method = request.getMethod();
        running = true;
    }

    protected void end(HttpServletRequest request) {
        if (!running)
            return;
        this.lastNanoEndTime = System.nanoTime();
        this.lastExecutionTime = lastNanoEndTime - lastNanoStartTime;
        running = false;
    }

    /**
     * 
     * @return execution time in nano seconds
     */

    public long getExecutionTimeForLastRequest() {
        if (running) return (System.nanoTime() - lastNanoStartTime);
        return lastExecutionTime;
    }

    /**
     * @return the counter
     */
    public AtomicLong getCounter() {
        return counter;
    }

    /**
     * @return the currentUri
     */
    public String getCurrentUri() {
        return currentUri;
    }

    /**
     * @return the lastEndTime
     */
    public long getLastEndTime() {
        return lastStartTime + (lastNanoEndTime / 1000000L);
    }

    /**
     * @return the lastStartTime
     */
    public long getLastStartTime() {
        return lastStartTime;
    }

    /**
     * @return the threadName
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    public void reset() {
        counter.set(0);
        currentUri = null;
        method = null;

        lastStartTime = 0L;
        lastNanoStartTime = 0L;
        lastNanoEndTime = 0L;
        lastExecutionTime = 0L;
        running = false;

    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the remoteHost
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    public State getThreadState() {
        final Thread x = thread.get();
        if (x != null) {
            return x.getState();
        } else {
            return null;
        }
    }
}
