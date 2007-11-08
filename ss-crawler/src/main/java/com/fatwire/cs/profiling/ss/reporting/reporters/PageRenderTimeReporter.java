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
        report.addRow("page\t" + page.getPageName() + "\tcomplete in\t"
                + page.getReadTime() + "\tms with statuscode:\t"
                + page.getResponseCode() + "\t[" + page.getUri() + "]");

    }

}