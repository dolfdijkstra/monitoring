/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;
import com.fatwire.cs.profiling.ss.util.CacheHelper;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class PageCriteriaReporter extends ReportDelegatingReporter {
    public PageCriteriaReporter(final Report report) {
        super(report);

    }

    public void addToReport(final ResultPage page) {
        if (page.getResponseCode() != 200) {
            return; //bail out
        }

        //check if this pagelet should be cached (is cacheable)
        if (page.getBody().endsWith(HelperStrings.STATUS_NOTCACHED)) {
            return;
        }else {
            if (!CacheHelper.shouldCache(page.getResponseHeaders())) {
                return; //page should not be cached based on SiteCatalog info 
            }
        }
        final Header[] headers = page.getResponseHeaders();
        for (final Header header : headers) {
            if (HelperStrings.PAGE_CRITERIA_HEADER.equals(header.getName())) {
                final List<String> pageCriteria = Arrays.asList(header
                        .getValue() == null ? new String[0] : header.getValue()
                        .split(","));
                final Map<String, String> params = new TreeMap<String, String>(
                        page.getUri().getParameters());
                //remove params that should not be part of PageCriteria
                params.remove(HelperStrings.PAGENAME);
                params.remove(HelperStrings.RENDERMODE);
                params.remove(HelperStrings.SS_CLIENT_INDICATOR);
                params.remove(HelperStrings.SS_PAGEDATA_REQUEST);
                for (final String param : params.keySet()) {
                    if (!pageCriteria.contains(param)) {
                        report.addRow(page.getPageName() + "(" + page.getUri()
                                + ") has parameter '" + param
                                + "' that is not part of the pageCriteria: "
                                + header.getValue());

                    }
                }
                break;
            }
        }
    }

}