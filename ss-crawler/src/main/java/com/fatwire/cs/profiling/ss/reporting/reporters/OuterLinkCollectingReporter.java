/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.Link;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class OuterLinkCollectingReporter extends ReportDelegatingReporter {
    private final SSUriHelper uriHelper;

    public OuterLinkCollectingReporter(final Report report,
            SSUriHelper uriHelper) {
        super(report);
        this.uriHelper = uriHelper;
    }

    public void addToReport(final ResultPage page) {
        if (page.getUri() instanceof Link) {
            report.addRow(uriHelper.toLink(page.getUri()));
        }
    }

}