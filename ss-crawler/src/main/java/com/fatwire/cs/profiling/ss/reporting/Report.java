package com.fatwire.cs.profiling.ss.reporting;

public interface Report {

    void startReport();

    void addRow(String line);

    void finishReport();

}
