package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class NumberOfInnerPageletsReporter extends ReportDelegatingReporter {

    private final int threshold;

    public NumberOfInnerPageletsReporter(final Report report, final int treshold) {
        super(report);
        this.threshold = treshold;

    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            final int num = page.getMarkers().size();
            if (num >= threshold) {
                report.addRow(num + "\t" + page.getUri());
            }
        }
    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.ss.reporting.reporters.ReportDelegatingReporter#startCollecting()
     */
    @Override
    public void startCollecting() {
        super.startCollecting();
        report.addRow("treshold\t" + threshold);
        report.addRow("inner pagelets\turi");

    }

}
