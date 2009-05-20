package com.fatwire.cs.profiling.servlet.jmx;

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

}
