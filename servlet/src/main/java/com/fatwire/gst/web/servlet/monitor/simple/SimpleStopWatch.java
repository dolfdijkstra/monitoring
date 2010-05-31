package com.fatwire.gst.web.servlet.monitor.simple;

import java.util.HashSet;
import java.util.Set;

import com.fatwire.gst.web.servlet.monitor.StopWatch;

public class SimpleStopWatch implements StopWatch {

    private long startTime;

    private long elapsed;

    private final Set<Mark> marks = new HashSet<Mark>();

    public long getTimeElapsedInMillis() {
        return elapsed;
    }

    public void start() {
        if (startTime != 0) {
            throw new IllegalStateException("StopWatch is already started");
        }
        startTime = System.currentTimeMillis();//.nanoTime();
        elapsed = 0;
        marks.clear();

    }

    public void stop() {
        if (startTime == 0) {
            throw new IllegalStateException("StopWatch is not started");
        }
        elapsed = System.currentTimeMillis() - startTime;// (System.nanoTime() - startTime) / 1000000L;
        startTime = 0;

    }

    public Mark[] getMarks() {
        return marks.toArray(new Mark[marks.size()]);
    }

    public void mark(String name) {
        if (startTime == 0) {
            throw new IllegalStateException("StopWatch is not started");
        }
        marks.add(new Mark(name, System.currentTimeMillis() - startTime));
    }
}
