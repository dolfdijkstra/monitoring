/**
 * 
 */
package com.fatwire.cs.profiling.ss.events;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class PageCriteriaRenderListener implements PageletRenderingListener {
    private static final Log LOG = LogFactory
            .getLog(PageCriteriaRenderListener.class);

    public PageCriteriaRenderListener() {

    }

    public void renderPerformed(final PageletRenderedEvent event) {
        final ResultPage page = event.getPage();
        if (page.getResponseCode() != 200)
            return; //bail out

        //check if this pagelet should be cached (is cacheable)
        if (page.getBody().endsWith(HelperStrings.STATUS_NOTCACHED)) {
            return;
        }
        final Header[] headers = page.getResponseHeaders();
        if (headers != null) {
            for (final Header header : headers) {
                if (HelperStrings.PAGE_CRITERIA_HEADER.equals(header.getName())) {
                    final List<String> pageCriteria = Arrays.asList(header
                            .getValue() == null ? new String[0] : header
                            .getValue().split(","));
                    final Map<String, String> params = new TreeMap<String, String>(page
                            .getUri().getParameters());
                    //remove params that should not be part of PageCriteria
                    params.remove(HelperStrings.PAGENAME);
                    params.remove(HelperStrings.RENDERMODE);
                    params.remove(HelperStrings.SS_CLIENT_INDICATOR);
                    params.remove(HelperStrings.SS_PAGEDATA_REQUEST);
                    for (final String param : params.keySet()) {
                        if (!pageCriteria.contains(param)) {
                            PageCriteriaRenderListener.LOG
                                    .warn(page.getPageName()
                                            + "("
                                            + page.getUri()
                                            + ") has parameter '"
                                            + param
                                            + "' that is not part of the pageCriteria: "
                                            + header.getValue());

                        }
                    }

                }
            }
        }

    }

    public void jobFinished() {
        // silent

    }
}