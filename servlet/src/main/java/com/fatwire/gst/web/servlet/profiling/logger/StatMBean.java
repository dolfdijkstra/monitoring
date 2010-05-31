package com.fatwire.gst.web.servlet.profiling.logger;

public interface StatMBean {

    public String getType();

    public String getSubType();

    public long getMin();

    public long getMax();

    public int getCount();

    public void reset();

    public double getAverage();

}