package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.QueryString;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class NestingReporter extends ReportDelegatingReporter {

    private final int threshold;

    public NestingReporter(Report report, final int threshold) {
        super(report);
        this.threshold = threshold;

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
        report.addRow("treshold\t" + threshold);
        report.addRow("uri\tnesting");

        for (QueryString qs : tracker.getKeys()) {
            int level = tracker.getNestingLevel(qs);
            if (level >= threshold) {
                report.addRow(qs.toString() + "\t"
                        + level);
            }
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
