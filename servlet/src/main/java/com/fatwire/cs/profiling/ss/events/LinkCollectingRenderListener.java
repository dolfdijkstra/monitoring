/**
 * 
 */
package com.fatwire.cs.profiling.ss.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.PageletTimingsCollector;
import com.fatwire.cs.profiling.ss.ResultPage;

public class LinkCollectingRenderListener implements PageletRenderingListener {
    private final Log LOG = LogFactory
            .getLog(LinkCollectingRenderListener.class);

    private final PageletTimingsCollector linkCollector;

    public LinkCollectingRenderListener(PageletTimingsCollector linkCollector2) {
        this.linkCollector = linkCollector2;
    }

    public void renderPerformed(final PageletRenderedEvent event) {
        final ResultPage page = event.getPage();
        if (LOG.isDebugEnabled()) {
            LOG.debug("page complete: in " + page.getReadTime() + " "
                    + page.getUri() + " with statuscode: "
                    + page.getResponseCode());
        }
        linkCollector.add(page);

    }
}