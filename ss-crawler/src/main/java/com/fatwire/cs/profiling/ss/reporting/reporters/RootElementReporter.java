package com.fatwire.cs.profiling.ss.reporting.reporters;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class RootElementReporter extends ReportDelegatingReporter {

    public static final String HEADER_NAME = "com.futuretense.contentserver.pagedata.field.rootelement";

    //com.futuretense.contentserver.pagedata.field.pagecriteria: c,cid,context,locale,p,pp,rendermode,site,sitepfx,ft_ss

    public RootElementReporter(final Report report) {
        super(report);

    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() == 200) {
            for (final Header header : page.getResponseHeaders()) {
                if (RootElementReporter.HEADER_NAME.equals(header.getName())) {
                    report.addRow("root element: " + header.getValue());
                }
            }

        }

    }

}
