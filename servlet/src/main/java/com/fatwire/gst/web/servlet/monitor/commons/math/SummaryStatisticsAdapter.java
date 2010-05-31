package com.fatwire.gst.web.servlet.monitor.commons.math;

import java.math.BigDecimal;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import com.fatwire.gst.web.servlet.monitor.Statistics;

public class SummaryStatisticsAdapter implements Statistics {
    private final SummaryStatistics stats;

    private String name;

    private long firstDate;

    private long lastDate;

    public SummaryStatisticsAdapter(String name) {
        stats = new SummaryStatistics();
        this.name = name;

    }

    public double getAverage() {
        return stats.getMean();
    }

    public long getFirstDate() {
        return this.firstDate;
    }

    public long getInvocations() {
        return stats.getN();
    }

    public long getLastDate() {
        return this.lastDate;
    }

    public double getMaxvalue() {
        return stats.getMax();
    }

    public double getMinValue() {
        return stats.getMin();
    }

    public String getName() {
        return name;
    }

    public double getStandardDeviation() {
        return stats.getStandardDeviation();
    }

    public BigDecimal getTotal() {
        return new BigDecimal(stats.getSum());
    }

    public void addValue(long l) {
        stats.addValue(l);
        this.lastDate = System.currentTimeMillis();
        if (this.firstDate == 0) {
            this.firstDate = lastDate;
        }
    }

    public void reset() {
        stats.clear();
    }
}
