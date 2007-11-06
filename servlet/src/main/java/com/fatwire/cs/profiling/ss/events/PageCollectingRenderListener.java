/**
 * 
 */
package com.fatwire.cs.profiling.ss.events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;

public class PageCollectingRenderListener implements PageletRenderingListener {
    private static final Log LOG = LogFactory
            .getLog(PageCollectingRenderListener.class);

    private static final char[] CRLF = "\r\n".toCharArray();

    private final File dir;

    private final AtomicLong idGen = new AtomicLong(System.currentTimeMillis());

    public PageCollectingRenderListener(final File dir) {
        this.dir = dir;
    }

    public void renderPerformed(final PageletRenderedEvent event) {
        final ResultPage page = event.getPage();
        if (page.getResponseCode() != 200)
            return; //bail out
        final long id = idGen.incrementAndGet();
        FileWriter writer = null;

        try {
            final String p = page.getPageName() != null ? page.getPageName()
                    .replace('/', '_') : "";
            writer = new FileWriter(new File(dir, p + "-" + id + ".txt"));
            final Header[] headers = page.getResponseHeaders();
            for (final Header header : headers) {
                writer.write(header.toExternalForm());
                //writer.write(CRLF);
            }
            writer.write(PageCollectingRenderListener.CRLF);
            writer.write(page.getBody());
        } catch (final IOException e) {
            PageCollectingRenderListener.LOG.error(e, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException ignored) {
                    //ignored
                }
            }
        }

    }

    public void jobFinished() {
        // TODO Auto-generated method stub

    }
}