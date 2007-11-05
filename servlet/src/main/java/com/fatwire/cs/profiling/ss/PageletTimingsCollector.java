/**
 * 
 */
package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.statistics.SimpleStatistics;

public class PageletTimingsCollector {
    private final Log log = LogFactory.getLog(getClass());

    private Writer writer;

    private final File file;

    private Map<String, SimpleStatistics> stats = new HashMap<String, SimpleStatistics>();

    private final AtomicInteger pagesDone = new AtomicInteger();

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
        System.out.println("reporing on " + pagesDone.get() + " pages");
        //        for (final Map.Entry<String, List<QueryString>> s : new PageNameDecorator(
        //                pagesDone).entrySet()) {
        //            log.info(s.getKey() + ":" + s.getValue().size());
        //        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DecimalFormat lf = new DecimalFormat("#,##0");
        for (final SimpleStatistics s : stats.values()) {
            String line = s.getName() + "\t" + s.getInvocations() + "\t"
                    + df.format(s.getAverage()) + "\t"
                    + new Date(s.getFirstDate()) + "\t"
                    + lf.format(s.getMaxvalue()) + "\t"
                    + df.format(s.getStandardDeviation());
            System.out.println(line);
        }

    }

    public synchronized void add(final ResultPage page) {
        //pagesDone.add(page.getUri());
        pagesDone.incrementAndGet();
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
            log.error(e.getMessage(), e);

        }
        String pagename = page.getPageName();
        if (pagename != null) {
            SimpleStatistics ss = stats.get(pagename);
            if (ss == null) {
                ss = new SimpleStatistics(pagename);
                stats.put(pagename, ss);
            }
            ss.addValue(page.getReadTime());
        }

    }
}