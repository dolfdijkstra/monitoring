package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class RootElementReporter extends ReportDelegatingReporter {

    public static final String HEADER_NAME = HelperStrings.CS_TO_SS_RESPONSE_HEADER_PREFIX +"rootelement";

    private final Set<String> elements = new CopyOnWriteArraySet<String>();

    public RootElementReporter(final Report report) {
        super(report);

    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            for (final Header header : page.getResponseHeaders()) {
                if (RootElementReporter.HEADER_NAME.equals(header.getName())) {
                    String element = header.getValue();

                    if (!elements.contains(element)) {
                        elements.add(element);
                        report.addRow(element);
                    }
                }
            }

        }

    }

}
