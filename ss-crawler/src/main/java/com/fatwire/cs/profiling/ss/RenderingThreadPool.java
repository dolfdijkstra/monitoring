package com.fatwire.cs.profiling.ss;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RenderingThreadPool extends ThreadPoolExecutor {
    private final Log log = LogFactory.getLog(getClass());

    private final Set<FinishedListener> listeners = new CopyOnWriteArraySet<FinishedListener>();

    public RenderingThreadPool(int threadSize) {
        super(threadSize, threadSize, 60, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(5000));

    }

    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (log.isDebugEnabled()) {
            log.debug("afterExecute: " + this.getActiveCount() + " "
                    + getQueue().size());
        }
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

    public void removeListener(FinishedListener listener) {
        this.listeners.remove(listener);
    }

}
