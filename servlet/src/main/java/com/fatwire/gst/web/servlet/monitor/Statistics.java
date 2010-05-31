package com.fatwire.gst.web.servlet.monitor;



import java.math.BigDecimal;

public interface Statistics {

    public long getInvocations();

    public double getAverage();

    public double getStandardDeviation();

    public double getMaxvalue();

    public double getMinValue();

    public BigDecimal getTotal();

    public long getFirstDate();

    public long getLastDate();

    public String getName();
}
