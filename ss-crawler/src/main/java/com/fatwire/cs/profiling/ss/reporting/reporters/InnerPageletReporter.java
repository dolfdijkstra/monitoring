package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.util.List;

import com.fatwire.cs.profiling.ss.QueryString;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class InnerPageletReporter extends ReportDelegatingReporter {

    public InnerPageletReporter(final Report report) {
        super(report);
    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            final List<QueryString> markers = page.getMarkers();
            if (!markers.isEmpty()) {

                final StringBuilder b = new StringBuilder("inner pagelets on ");
                b.append(page.getUri().toString());
                for (final QueryString qs : markers) {
                    b.append(HelperStrings.CRLF);
                    b.append("# ");
                    b.append(qs.toString());

                }
                report.addRow(b.toString());
            }
        }

    }

}
