package com.fatwire.gst.web.status;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

public class RequestCounter implements
        ConcurrencyCounter<HttpServletRequest, RequestInfo> {

    private final AtomicLong counter = new AtomicLong();

    private final AtomicInteger concurrencyCounter = new AtomicInteger();

    private final Map<Thread, RequestInfo> threadMap = new WeakHashMap<Thread, RequestInfo>();

    private final String name;

    private final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<RequestInfo>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected RequestInfo initialValue() {
            RequestInfo i = new RequestInfo(Thread.currentThread());
            synchronized (threadMap) {
                threadMap.put(Thread.currentThread(), i);
            }
            return i;
        }

    };

    private final Comparator<RequestInfo> requestInfoComparator = new Comparator<RequestInfo>() {

        public int compare(RequestInfo o1, RequestInfo o2) {
            return o1.getThreadName().compareTo(o2.getThreadName());
        }

    };

    public RequestCounter(final String name) {
        this.name = name;
    }

    public long getTotalCount() {
        return counter.get();
    }

    public int getConcurrencyCount() {
        return concurrencyCounter.get();
    }

    public Collection<RequestInfo> getCurrentExecutingOperations() {
        final Set<RequestInfo> s = new TreeSet<RequestInfo>(
                requestInfoComparator);
        RequestInfo[] r;
        synchronized (threadMap) {
            r = threadMap.values().toArray(new RequestInfo[0]);
        }
        for (RequestInfo info : r) {
            if (info.isAlive()) {//filter out inactive threads
                s.add(info);
            }
        }
        return s;

    }

    public String getName() {
        return name;
    }

    public synchronized void reset() {
        counter.set(0);
        concurrencyCounter.set(0);
        for (final RequestInfo info : threadMap.values()) {
            info.reset();
        }
    }

    public void start(final HttpServletRequest request) {
        final RequestInfo current = threadLocal.get();
        if (current.isRunning())
            throw new IllegalStateException("Can't call start twice");
        current.start(request);
        concurrencyCounter.incrementAndGet();
        counter.incrementAndGet();

    }

    /**
     * signal end of the request
     * 
     * @return the execution time for this requets in nano seconds
     */
    public long end(final HttpServletRequest request) {
        final RequestInfo current = threadLocal.get();
        if (current.isRunning()) {
            current.end(request);
            concurrencyCounter.decrementAndGet();
            return current.getExecutionTimeForLastRequest();
        }
        return -1;

    }

}
