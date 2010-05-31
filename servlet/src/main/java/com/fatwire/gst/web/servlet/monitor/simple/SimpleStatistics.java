package com.fatwire.gst.web.servlet.monitor.simple;

import java.math.BigDecimal;

import com.fatwire.gst.web.servlet.monitor.Statistics;

public class SimpleStatistics implements Statistics {

    //private final Log log = LogFactory.getLog(getClass());
    private final String name;

    private volatile BigDecimal total = new BigDecimal(0);

    private volatile BigDecimal totalPower = new BigDecimal(0);

    private volatile double max = Double.MIN_VALUE;

    private volatile double min = Double.MAX_VALUE;

    private volatile long counts;

    private long startDate;

    private volatile long lastDate;

    /**
     * 
     */
    public SimpleStatistics(final String name) {
        super();
        this.name = name;
        reset();

    }

    public void addValue(final double num) {
        counts++;
        lastDate = System.currentTimeMillis();
        total = total.add(new BigDecimal(num));
        totalPower = totalPower.add(new BigDecimal(num * num));
        if (num > max) {
            max = num;
        }
        if (num < min) {
            min = num;
        }

    }

    public double getAverage() {
        if (counts < 2) {
            return 0;
        } else {
            return total.divide(new BigDecimal(counts),
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    public long getInvocations() {
        return counts;
    }

    public double getMaxvalue() {
        if (counts < 1) {
            return 0D;
        }
        return max;
    }

    public double getMinValue() {
        if (counts < 1) {
            return 0D;
        }
        return min;
    }

    public double getStandardDeviation() {
        if (counts < 3) {
            return 0D;
        }
        final BigDecimal powerTotal = total.multiply(total);
        final BigDecimal bovenDeStreep = totalPower.multiply(
                new BigDecimal(counts)).subtract(powerTotal);
        return Math.sqrt(bovenDeStreep
                .divide(new BigDecimal(counts * (counts - 1)),
                        BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public BigDecimal getTotal() {
        return total.add(new BigDecimal(0));
    }

    public long getFirstDate() {
        return startDate;
    }

    public long getLastDate() {
        return lastDate;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void reset() {
        counts = 0;
        total = new BigDecimal(0);

        totalPower = new BigDecimal(0);

        max = Double.MIN_VALUE;

        min = Double.MAX_VALUE;
        startDate = System.currentTimeMillis();
    }

}
