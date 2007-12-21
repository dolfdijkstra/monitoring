package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.fatwire.cs.profiling.ss.reporting.Reporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.InnerLinkReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.InnerPageletReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.LinkCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NestingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.Non200ResponseCodeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NotCachedReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NumberOfInnerPageletsReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.OuterLinkCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCriteriaReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageRenderTimeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageletOnlyReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageletTimingsStatisticsReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.RootElementReporter;
import com.fatwire.cs.profiling.ss.reporting.reports.FileReport;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;
import com.fatwire.cs.profiling.ss.util.TempDir;

public class App {


    /**
     * @param args
     */
    public static void main(final String[] args) {

        if (args.length < 2) {
            throw new IllegalArgumentException();
        }

        Crawler crawler = new Crawler();
        crawler.setHost(args[0]);
        crawler.setStartUri(URI.create(args[1]));
        if (args.length > 2) {
            crawler.setMaxPages(Integer.parseInt(args[2]));
        }
        final ThreadPoolExecutor readerPool = new RenderingThreadPool();

        crawler.setExecutor(readerPool);
        crawler.setReporters(createReporters(App.getOutputDir()));
        crawler.setUriHelper(new SSUriHelper(URI.create(args[0]).getPath()));
        crawler.work();
        readerPool.shutdown();

    }
    private static File getOutputDir() {
        final File outputDir = new File(TempDir.getTempDir(App.class), Long
                .toString(System.currentTimeMillis()));
        outputDir.mkdirs();
        return outputDir;
    }


    private static List<Reporter> createReporters(File outputDir) {

        List<Reporter> reporters = new ArrayList<Reporter>();
        reporters.add(new LinkCollectingReporter(new FileReport(outputDir,
                "links.txt")));
        reporters.add(new OuterLinkCollectingReporter(new FileReport(outputDir,
        "outer-links.txt")));

        reporters.add(new PageCollectingReporter(new File(outputDir, "pages")));

        reporters.add(new PageletTimingsStatisticsReporter(new FileReport(
                outputDir, "pagelet-stats.txt")));

        reporters.add(new PageCriteriaReporter(new FileReport(outputDir,
                "pagecriteria.txt")));
//        reporters.add(new PageRenderTimeReporter(new ReportCollection(
//                new StdOutReport(), new FileReport(outputDir, "timings.txt"))));
        reporters.add(new PageRenderTimeReporter(new FileReport(outputDir, "timings.txt")));

        reporters.add(new RootElementReporter(new FileReport(outputDir,
                "root-elements.txt")));
        reporters.add(new NumberOfInnerPageletsReporter(
                new FileReport(outputDir, "num-inner-pagelets.txt"), 12));
        reporters.add(new Non200ResponseCodeReporter(new FileReport(outputDir,
                "non-200.txt")));
        reporters.add(new InnerPageletReporter(new FileReport(outputDir,
                "inner-pagelets.txt")));

        reporters.add(new InnerLinkReporter(new FileReport(outputDir,
                "inner-links.txt")));

        reporters.add(new NestingReporter(new FileReport(outputDir,
                "nesting.txt"), 10));
        reporters.add(new PageletOnlyReporter(new FileReport(outputDir,
                "pagelet-only.txt")));
        reporters.add(new NotCachedReporter(new FileReport(outputDir,
                "not-cached.txt")));
        return reporters;
    }



}