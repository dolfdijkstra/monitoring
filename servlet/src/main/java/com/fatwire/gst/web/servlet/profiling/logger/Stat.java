/**
 * 
 */
package com.fatwire.gst.web.servlet.profiling.logger;

import java.math.BigDecimal;

import javax.management.ObjectName;

public class Stat implements StatMBean {
    private String type;

    private String subType;

    private long min = Long.MAX_VALUE;

    private long max = Long.MIN_VALUE;

    private int count = 0;

    private BigDecimal total = BigDecimal.valueOf(0);
    private ObjectName name;

    public Stat(){};

    synchronized void update(long t) {
        count++;
        total = total.add(BigDecimal.valueOf(t));
        min = Math.min(min, t);
        max = Math.max(max, t);

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getType()
     */
    public String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getSubType()
     */
    public String getSubType() {
        return subType;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getMin()
     */
    public long getMin() {
        return count == 0 ? 0 : min;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getMax()
     */
    public long getMax() {
        return count == 0 ? 0 : max;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getCount()
     */
    public int getCount() {
        return count;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#reset()
     */
    public void reset() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        count = 0;
        total = BigDecimal.valueOf(0);
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.StatMBean#getAverage()
     */
    public double getAverage() {
        if (count == 0)
            return Double.NaN;
        return total.divide(BigDecimal.valueOf(count), 2,
                BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public ObjectName getName() {
        return name;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param subType the subType to set
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * @param name the name to set
     */
    public void setName(ObjectName name) {
        this.name = name;
    }
}