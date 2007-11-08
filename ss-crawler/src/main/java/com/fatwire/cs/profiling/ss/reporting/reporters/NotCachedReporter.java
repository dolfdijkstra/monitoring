/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class NotCachedReporter extends ReportDelegatingReporter {
    public NotCachedReporter(final Report report) {
        super(report);

    }

    //com.futuretense.contentserver.pagedata.field.sscacheinfocache-disabled: false
    //com.futuretense.contentserver.pagedata.field.cscacheinfocache-disabled: false
    public static final String SS_CACHE_INFO = HelperStrings.CS_TO_SS_RESPONSE_HEADER_PREFIX
            + "sscacheinfostring";

    public static final String CS_CACHE_INFO = HelperStrings.CS_TO_SS_RESPONSE_HEADER_PREFIX
            + "cscacheinfostring";

    //com.futuretense.contentserver.pagedata.field.sscacheinfostring: true
    //com.futuretense.contentserver.pagedata.field.cscacheinfostring: true

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() != 200) {
            return; //bail out
        }

        if (page.getBody().endsWith(HelperStrings.STATUS_NOTCACHED)) {
            if (shouldCache(page.getResponseHeaders())) {
                report.addRow("not caching while we should\t" + page.getUri());
            } else {
                report.addRow("not caching \t" + page.getUri());
            }

        }
    }

    boolean shouldCache(Header[] headers) {
        int i = 0;
        int j = 0;
        for (Header h : headers) {
            if (SS_CACHE_INFO.equals(h.getName())
                    || CS_CACHE_INFO.equals(h.getName())) {
                j++;
                if ("false".equals(h.getValue())) {
                    i++;
                }
            }
        }
        return j == 2 && i == 0;
    }

}