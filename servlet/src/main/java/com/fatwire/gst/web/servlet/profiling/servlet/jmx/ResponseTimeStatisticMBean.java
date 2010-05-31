package com.fatwire.gst.web.servlet.profiling.servlet.jmx;

import java.math.BigDecimal;

public interface ResponseTimeStatisticMBean {

    /**
     * 
     * @return the number of values in thi statistic
     */
    int getCount();

    /**
     * 
     * @return the total time of the values in this statistic
     */
    BigDecimal getTotalTime();

    /**
     * 
     * 
     * @return the average of the values in this statistic
     */
    double getAverage();

    /**
     * @return the minTime
     */
    long getMinTime();

    /**
     * @return the maxTime
     */
    long getMaxTime();

    /**
     * 
     * @return the total number of time a thread has been blocked during the execution of this request
     * @see java.lang.management.ThreadInfo#getBlockedCount()
     */
    public long getBlockCount();

    /**
     * 
     * @return the total number of time a thread has been waiting during the execution of this request
     * @see java.lang.management.ThreadInfo#getWaitedCount()
     */

    public long getWaitCount();
    /**
     * 
     * @return the total time a thread has spend in system mode
     * @see java.lang.management.ThreadMXBean#getCurrentThreadCpuTime()
     * @see java.lang.management.ThreadMXBean#getCurrentThreadUserTime()
     */

    public BigDecimal getTotalSystemTime();

}
