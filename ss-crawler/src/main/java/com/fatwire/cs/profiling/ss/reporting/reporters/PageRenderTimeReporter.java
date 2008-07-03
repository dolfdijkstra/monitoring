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
        report.addRow(page.getPageName() + "\t"
                + page.getReadTime() + "\t"
                + page.getResponseCode() + "\t" + page.getUri());

    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.ss.reporting.reporters.ReportDelegatingReporter#startCollecting()
     */
    @Override
    public void startCollecting() {
        super.startCollecting();
        report.addRow("pagename\tdownload time\tstatuscode\targuments");
                
        
    }

}