/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.Link;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class OuterLinkCollectingReporter extends ReportDelegatingReporter {
    public OuterLinkCollectingReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        if (page.getUri() instanceof Link) {
            report.addRow(page.getUri().toString());
        }
    }

}