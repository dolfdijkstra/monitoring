package com.fatwire.cs.profiling;

public interface Measurement {

    Hierarchy getName();
    
    long getStartTime();

    void stop();

    /*
     * elapsed time in nanoseconds since start or elepsed time between start() and stop()
     */
    long elapsed();

}
