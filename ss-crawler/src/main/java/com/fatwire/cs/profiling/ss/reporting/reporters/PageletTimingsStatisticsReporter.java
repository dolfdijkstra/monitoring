/**
 * 
 */
package com.fatwire.cs.profiling.ss.reporting.reporters;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.reporting.Report;

public class PageletTimingsStatisticsReporter extends ReportDelegatingReporter {

    private final Map<String, SynchronizedSummaryStatistics> stats = new ConcurrentHashMap<String, SynchronizedSummaryStatistics>();

    private final SynchronizedSummaryStatistics total = new SynchronizedSummaryStatistics();

    private final AtomicInteger pagesDone = new AtomicInteger();

    /**
     * @param file
     */
    public PageletTimingsStatisticsReporter(final Report report) {
        super(report);

    }

    public synchronized void addToReport(final ResultPage page) {
        pagesDone.incrementAndGet();
        total.addValue(page.getReadTime());
        final String pagename = page.getPageName();
        if (pagename != null) {
            SynchronizedSummaryStatistics ss = stats.get(pagename);
            if (ss == null) {
                ss = new SynchronizedSummaryStatistics();
                stats.put(pagename, ss);
            }
            ss.addValue(page.getReadTime());
        }

    }

    @Override
    public void endCollecting() {
        report.startReport();
        //report.addRow("reporting on " + pagesDone.get() + " pages");
        final DecimalFormat df = new DecimalFormat("###0.00");
        final DecimalFormat lf = new DecimalFormat("##0");
   //     final DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        int l = 0;
        for (final String s : stats.keySet()) {
            l = Math.max(s.length(), l);
        }
        final char[] blank = new char[l];
        Arrays.fill(blank, ' ');
        final String header = "pagename" + new String(blank, 0, l - 8)
                + "\tinvocations\taverage\tmin\tmax\tstandard-deviation";
        report.addRow(header);

        for (final Map.Entry<String, SynchronizedSummaryStatistics> e : stats
                .entrySet()) {
            SynchronizedSummaryStatistics s = e.getValue();

            final String n = e.getKey()
                    + new String(blank, 0, l - e.getKey().length());
            final String line = n + "\t" + s.getN() + "\t"
                    + df.format(s.getMean()) + "\t" + lf.format(s.getMin())
                    + "\t" + lf.format(s.getMax()) + "\t"
                    + df.format(s.getStandardDeviation());
            report.addRow(line);
        }
        final String n = "total" + new String(blank, 0, l - "total".length());
        final String line = n + "\t" + total.getN() + "\t"
                + df.format(total.getMean()) + "\t"
                //+ dateFormat.format(new Date(total.getFirstDate())) + "\t"
                + lf.format(total.getMin()) + "\t" + lf.format(total.getMax())
                + "\t" + df.format(total.getStandardDeviation());
        report.addRow(line);

        report.finishReport();

    }

    @Override
    public void startCollecting() {
        stats.clear();
        pagesDone.set(0);
        total.clear();

    }

}