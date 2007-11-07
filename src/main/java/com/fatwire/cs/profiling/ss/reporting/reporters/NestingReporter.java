package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.QueryString;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class NestingReporter extends ReportDelegatingReporter {
    public NestingReporter(Report report) {
        super(report);

    }

    private final NestingTracker tracker = new NestingTracker();

    public void addToReport(ResultPage page) {
        if (page.getResponseCode() == 200) {
            tracker.add(page);
        }
    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.ss.reporting.reporters.ReportDelegatingReporter#endCollecting()
     */
    @Override
    public void endCollecting() {
        super.startCollecting();

        for (QueryString qs : tracker.getKeys()) {
            report.addRow(qs.toString() + " has a nesting level of "
                    + tracker.getNestingLevel(qs));
        }

        super.endCollecting();
    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.ss.reporting.reporters.ReportDelegatingReporter#startCollecting()
     */
    @Override
    public void startCollecting() {
        //do nothing, all is handled in the endCollecting call
    }

}
