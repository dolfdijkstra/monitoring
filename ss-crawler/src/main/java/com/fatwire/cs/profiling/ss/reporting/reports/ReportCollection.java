package com.fatwire.cs.profiling.ss.reporting.reports;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.reporting.Report;

public class ReportCollection implements Report {
    private final Log log = LogFactory.getLog(getClass());

    private final List<Report> reports = new CopyOnWriteArrayList<Report>();

    public ReportCollection() {

    }

    public ReportCollection(Report... reports) {
        for (Report report : reports) {
            addReport(report);
        }
    }

    public void addReport(Report reporter) {
        reports.add(reporter);
    }

    public void addRow(String line) {
        for (Iterator<Report> i = reports.iterator(); i.hasNext();) {
            Report report = i.next();
            try {
                report.addRow(line);
            } catch (Throwable e) {
                log.error(e, e);
                i.remove(); //remove reporter if we can't use it
            }
        }

    }

    public void finishReport() {
        for (Iterator<Report> i = reports.iterator(); i.hasNext();) {
            Report report = i.next();
            try {
                report.finishReport();
            } catch (Throwable e) {
                log.error(e, e);
            }
        }

    }

    public void startReport() {
        for (Iterator<Report> i = reports.iterator(); i.hasNext();) {
            Report report = i.next();
            try {
                report.startReport();
            } catch (Throwable e) {
                log.error(e, e);
                i.remove(); //remove reporter if we can't use it
            }
        }

    }

}
