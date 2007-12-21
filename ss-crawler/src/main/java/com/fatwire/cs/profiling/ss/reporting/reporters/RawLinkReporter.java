package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class RawLinkReporter extends ReportDelegatingReporter {

    private final Pattern rawLinkPattern = Pattern
            .compile("Satellite\\?.*?[\"']");

    public RawLinkReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() != 200) {
            String body = page.getBody();
            if (body != null) {

                Matcher m = this.rawLinkPattern.matcher(body);
                while (m.find()) {
                    report.addRow(page.getUri() + "\thas a raw link\t"
                            + m.group());
                }
            }
        }

    }

}
