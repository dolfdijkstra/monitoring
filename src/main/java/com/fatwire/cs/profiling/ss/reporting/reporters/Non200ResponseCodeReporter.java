package com.fatwire.cs.profiling.ss.reporting.reporters;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class Non200ResponseCodeReporter extends ReportDelegatingReporter {

    public Non200ResponseCodeReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() != 200) {
            report.addRow(page.getUri().toString()
                    + " reported a response status " + page.getResponseCode());
        }

    }

}
