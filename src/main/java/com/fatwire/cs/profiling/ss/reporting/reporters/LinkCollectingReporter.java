/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class LinkCollectingReporter extends ReportDelegatingReporter {
    public LinkCollectingReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        final StringBuilder msg = new StringBuilder();
        msg.append(page.getPageName());
        msg.append("\t");
        msg.append(page.getUri());
        report.addRow(msg.toString());
    }

}