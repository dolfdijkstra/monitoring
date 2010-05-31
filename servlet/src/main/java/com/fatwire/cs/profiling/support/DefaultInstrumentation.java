package com.fatwire.cs.profiling.support;

import java.util.LinkedList;
import java.util.List;

import com.fatwire.cs.profiling.Hierarchy;
import com.fatwire.cs.profiling.Instrumentation;
import com.fatwire.cs.profiling.Measurement;

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
