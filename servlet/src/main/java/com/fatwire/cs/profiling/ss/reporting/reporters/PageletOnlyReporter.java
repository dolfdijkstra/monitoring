package com.fatwire.cs.profiling.ss.reporting.reporters;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class PageletOnlyReporter extends ReportDelegatingReporter {
    private static final String PAGELET_ONLY_HEADER = "com.futuretense.contentserver.pagedata.field.pageletonly";

    public PageletOnlyReporter(Report report) {
        super(report);
    }

    public void addToReport(ResultPage page) {
        if (page.getResponseCode() == 200) {
            for (final Header header : page.getResponseHeaders()) {
                if (PAGELET_ONLY_HEADER.equals(header.getName())) {
                    if ("F".equals(header.getValue())) {
                        report.addRow("pagelet only " + header.getValue()
                                + " for " + page.getPageName());
                    }
                    break;
                }
            }
        }

    }

}
