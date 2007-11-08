package com.fatwire.cs.profiling.ss.reporting.reports;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.reporting.Report;

public class WarningLevelLoggingReport implements Report {

    protected final Log log;

    public WarningLevelLoggingReport(String loggerName) {
        log = LogFactory.getLog(loggerName);
    }

    public void addRow(String line) {
        log.warn(line);
    }

    public void finishReport() {
        // TODO Auto-generated method stub

    }

    public void startReport() {
        // TODO Auto-generated method stub

    }

}
