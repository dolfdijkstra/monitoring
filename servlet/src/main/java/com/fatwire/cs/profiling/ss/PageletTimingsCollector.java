/**
 * 
 */
package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.util.PageNameDecorator;

public class PageletTimingsCollector {
    private static final Log LOG = LogFactory
            .getLog(PageletTimingsCollector.class);

    private Writer writer;

    private final File file;

    final Set<String> pagesDone = new HashSet<String>();

    /**
     * @param file
     */
    public PageletTimingsCollector(final File file) {
        super();
        this.file = file;
    }

    public synchronized void open() throws IOException {
        writer = new FileWriter(file);
    }

    public synchronized void close() throws IOException {

        writer.close();
    }

    public synchronized void report() {
        for (final Map.Entry<String, List<String>> s : new PageNameDecorator(
                pagesDone).entrySet()) {
            LOG.info(s.getKey() + ":" + s.getValue().size());
        }
    }

    public synchronized void add(final ResultPage page) {
        pagesDone.add(page.getUri());
        try {
            final StringBuilder msg = new StringBuilder();
            msg.append(page.getPageName());
            msg.append("\t");
            msg.append(page.getReadTime());
            msg.append("\t");
            msg.append(page.getUri());
            msg.append("\r\n");
            writer.write(msg.toString());
        } catch (final IOException e) {
            LOG.error(e.getMessage(), e);

        }

    }
}