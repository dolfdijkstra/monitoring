package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class NumberOfInnerPageletsReporter extends ReportDelegatingReporter {

    private final int treshold;

    public NumberOfInnerPageletsReporter(final Report report, final int treshold) {
        super(report);
        this.treshold = treshold;

    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            final int num = page.getMarkers().size();
            if (num >= treshold) {
                report.addRow("number of inner pagelets is " + num + " for "
                        + page.getUri());
            }
        }

    }

}
