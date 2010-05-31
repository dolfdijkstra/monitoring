package com.fatwire.gst.web.servlet.profiling.servlet.jmx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

public class ResponseTimeStatistic implements ResponseTimeStatisticMBean {

    private final AtomicInteger counter = new AtomicInteger();

    private final AtomicLong blockCounter = new AtomicLong();

    private final AtomicLong waitCounter = new AtomicLong();

    private long minTime = Long.MAX_VALUE;

    private long maxTime = 0;

    private volatile BigDecimal total = BigDecimal.valueOf(0);

    private volatile BigDecimal systemTotal = BigDecimal.valueOf(0);

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

    void signal(HttpServletRequest request, Measurement m) {
        counter.incrementAndGet();
        this.blockCounter.addAndGet(m.getBlockCountDelta());
        this.waitCounter.addAndGet(m.getWaitCountDelta());

        if (m.getElapsedUserTime() > 0) {
            long user = m.getElapsedUserTime();
            long cpu = m.getElapsedCpuTime();
            long system = cpu - user;
            if (system > 0) {
                this.systemTotal = this.systemTotal.add(BigDecimal
                        .valueOf(system));
            }

        }

        long t = m.getElapsedTime() / 1000000;

        total = total.add(BigDecimal.valueOf(t));
        minTime = Math.min(minTime, t);
        maxTime = Math.max(maxTime, t);

    }

    /**
     * @return the minTime
     */
    public long getMinTime() {

        return getCount() == 0 ? 0 : minTime;
    }

    /**
     * @return the maxTime
     */
    public long getMaxTime() {
        return getCount() == 0 ? 0 : maxTime;
    }

    public long getBlockCount() {
        return this.blockCounter.get();
    }

    public long getWaitCount() {
        return this.waitCounter.get();
    }

    public BigDecimal getTotalSystemTime() {
        return this.systemTotal;
    }

}
