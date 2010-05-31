/**
 * 
 */
package com.fatwire.cs.profiling.support;

import com.fatwire.cs.profiling.Hierarchy;
import com.fatwire.cs.profiling.Measurement;

public final class MeasurementImpl implements Measurement {
    private final Hierarchy key;

    final long startNanoTime = System.nanoTime();

    final long startTime = System.currentTimeMillis();

    long endTime;

    boolean running = true;

    MeasurementImpl(String key) {
        this.key = new Hierarchy(key);
    }

    public MeasurementImpl(Hierarchy name) {
        this.key = name;
    }

    public long elapsed() {
        if (running)
            return System.nanoTime() - startNanoTime;
        return endTime - startTime;
    }

    public void stop() {
        endTime = System.nanoTime();
        running = false;

    }

    public Hierarchy getName() {
        return key;
    }

    public long getStartTime() {
        return startTime;
    }
}