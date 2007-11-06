/**
 * 
 */
package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        int l = 0;
        for (final String s : stats.keySet()) {
            l = Math.max(s.length(), l);
        }
        char[] blank = new char[l];
        Arrays.fill(blank, ' ');
        String header = "pagename"+ new String(blank,0,l-8)+"\tinvocations\taverage\tfirst\tmax\tstandard-deviation";
        System.out.println(header);

        for (final SimpleStatistics s : stats.values()) {
            String n = s.getName() + new String(blank,0,l-s.getName().length());
            String line = n + "\t" + s.getInvocations() + "\t"
                    + df.format(s.getAverage()) + "\t"
                    + dateFormat.format(new Date(s.getFirstDate())) + "\t"
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