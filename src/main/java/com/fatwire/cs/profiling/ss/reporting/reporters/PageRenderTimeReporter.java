/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class PageRenderTimeReporter extends ReportDelegatingReporter {

    /**
     * @param report
     */
    public PageRenderTimeReporter(final Report report) {
        super(report);

    }

    public void addToReport(final ResultPage page) {
        report.addRow("page " + page.getPageName() + " complete in "
                + page.getReadTime() + " ms with statuscode: "
                + page.getResponseCode() + " [" + page.getUri() + "]");

    }

}