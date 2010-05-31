package com.fatwire.gst.web.servlet.profiling;

public interface Instrumentation {

    /*
     * Create a new measurement with the supplied name and start it
     * 
     */

    Measurement start(String name);

    Measurement start(Hierarchy name);

    Measurement[] getMeasurements();

}
