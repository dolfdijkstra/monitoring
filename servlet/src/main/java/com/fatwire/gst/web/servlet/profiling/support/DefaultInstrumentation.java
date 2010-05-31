package com.fatwire.gst.web.servlet.profiling.support;

import java.util.LinkedList;
import java.util.List;

import com.fatwire.gst.web.servlet.profiling.Hierarchy;
import com.fatwire.gst.web.servlet.profiling.Instrumentation;
import com.fatwire.gst.web.servlet.profiling.Measurement;

public class DefaultInstrumentation implements Instrumentation {

    private final List<Measurement> measurements = new LinkedList<Measurement>();

    public Measurement[] getMeasurements() {
        Measurement[] m = measurements.toArray(new Measurement[measurements
                .size()]);
        measurements.clear();
        return m;
    }

    public Measurement start(final String key) {

        return new MeasurementImpl(key);
    }

    public Measurement start(Hierarchy name) {
        return new MeasurementImpl(name);
    }

}
