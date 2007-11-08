package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.util.List;

import com.fatwire.cs.profiling.ss.QueryString;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class InnerLinkReporter extends ReportDelegatingReporter {

    public InnerLinkReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            final List<QueryString> links = page.getLinks();
            if (!links.isEmpty()) {

                final StringBuilder b = new StringBuilder("links on ");
                b.append(page.getUri().toString());
                for (final QueryString qs : links) {
                    b.append(HelperStrings.CRLF);
                    b.append("# ");
                    b.append(qs.toString());

                }
                report.addRow(b.toString());
            }
        }

    }

}
