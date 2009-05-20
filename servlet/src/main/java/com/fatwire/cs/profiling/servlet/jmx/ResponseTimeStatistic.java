package com.fatwire.cs.profiling.servlet.jmx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

public class ResponseTimeStatistic implements ResponseTimeStatisticMBean {

    private final AtomicInteger counter = new AtomicInteger();

    private long minTime = Long.MAX_VALUE;

    private long maxTime = 0;

    private volatile BigDecimal total = BigDecimal.valueOf(0);

    public int getCount() {
        return counter.get();
    }

    /**
     * total processing time in micro seconds
     * 
     */
    public BigDecimal getTotalTime() {
        return total;
    }

    public double getAverage() {
        int n = counter.get();
        if (n == 0)
            return 0;
        return (total.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP)
                .doubleValue());

    }

    void signal(HttpServletRequest request, long nanoTime) {
        counter.incrementAndGet();
        long t = nanoTime / 1000000;

        total = total.add(BigDecimal.valueOf(t));
        minTime = Math.min(minTime, t);
        maxTime = Math.min(maxTime, t);
    }

    /**
     * @return the minTime
     */
    public long getMinTime() {
        return minTime;
    }

    /**
     * @return the maxTime
     */
    public long getMaxTime() {
        return maxTime;
    }
}
