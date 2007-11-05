package com.fatwire.cs.profiling.ss;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RenderingThreadPool extends ThreadPoolExecutor {
    private final Log log = LogFactory.getLog(RenderingThreadPool.class);

    private final Set<FinishedListener> listeners = new CopyOnWriteArraySet<FinishedListener>();

    public RenderingThreadPool(LinkedBlockingQueue<Runnable> workQueue) {
        super(10, 20, 60, TimeUnit.SECONDS, workQueue);

    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        log.debug(this.getActiveCount() + " " + getQueue().size());
        if (this.getActiveCount() == 1 && getQueue().size() == 0) {
            for (FinishedListener listener : listeners) {
                listener.finished();
            }
        }
        super.afterExecute(r, t);
    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    interface FinishedListener {

        void finished();
    }

    public void addListener(FinishedListener listener) {
        this.listeners.add(listener);
    }
}
