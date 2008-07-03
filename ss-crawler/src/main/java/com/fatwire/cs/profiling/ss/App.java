package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.xml.DOMConfigurator;

import com.fatwire.cs.profiling.ss.reporting.Reporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.DefaultArgumentsAsPageCriteriaReporter;
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
import com.fatwire.cs.profiling.ss.util.UriHelperFactory;

public class App {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(final String[] args) throws Exception {

        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Usage: java "
                            + App.class.getName()
                            + " -startUri \"http://localhost:8080/cs/ContentServer?pagename=...\" -reportDir <report dir> -max <max pages>");
        }
        DOMConfigurator.configure("conf/log4j.xml");
        Crawler crawler = new Crawler();
        File path = null;
        String factory = null;
        URI startUri = null;
        for (int i = 0; i < args.length; i++) {
            if ("-host".equals(args[i])) {
                crawler.setHost(args[++i]);
            } else if ("-startUri".equals(args[i])) {
                startUri = URI.create(args[++i]);
            } else if ("-reportDir".equals(args[i])) {
                path = new File(args[++i]);
            } else if ("-max".equals(args[i])) {
                crawler.setMaxPages(Integer.parseInt(args[++i]));
            } else if ("-uriHelperFactory".equals(args[i])) {
                factory = args[++i];
            }

        }
        if (startUri == null)
            throw new IllegalArgumentException("startUri is not set");
        int t = startUri.toASCIIString().indexOf("ContentServer");
        crawler.setHost(startUri.toASCIIString().substring(0, t));
        crawler.setStartUri(new URI(null, null, null, -1,
                startUri.getRawPath(), startUri.getRawQuery(), startUri
                        .getFragment()));

        if (path == null) {
            path = App.getOutputDir();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
        path = new File(path, df.format(new Date()));
        path.mkdirs();
        SSUriHelper helper = null;

        if (factory != null) {
            UriHelperFactory f = (UriHelperFactory) (Class.forName(factory)
                    .newInstance());
            helper = f.create(crawler.getStartUri().getPath());
        } else {
            helper = new SSUriHelper(crawler.getStartUri().getPath());
        }
        final ThreadPoolExecutor readerPool = new RenderingThreadPool(5);

        crawler.setExecutor(readerPool);
        crawler.setReporters(createReporters(path, helper));
        crawler.setUriHelper(helper);
        crawler.work();
        readerPool.shutdown();

    }

    private static File getOutputDir() {
        
        final File outputDir = new File("./reports");//TempDir.getTempDir(App.class);
        outputDir.mkdirs();
        return outputDir;
    }

    private static List<Reporter> createReporters(File outputDir,
            SSUriHelper helper) {

        List<Reporter> reporters = new ArrayList<Reporter>();
        reporters.add(new LinkCollectingReporter(new FileReport(outputDir,
                "pagelets.txt")));
        reporters.add(new OuterLinkCollectingReporter(new FileReport(outputDir,
                "browsable-links.txt"), helper));

        reporters.add(new PageCollectingReporter(new File(outputDir, "pages")));

        reporters.add(new PageletTimingsStatisticsReporter(new FileReport(
                outputDir, "pagelet-stats.xls")));

        reporters.add(new PageCriteriaReporter(new FileReport(outputDir,
                "pagecriteria.txt")));
        reporters.add(new PageRenderTimeReporter(new FileReport(outputDir,
                "pagelet-timings.txt")));

        reporters.add(new RootElementReporter(new FileReport(outputDir,
                "root-elements.txt")));
        reporters.add(new NumberOfInnerPageletsReporter(new FileReport(
                outputDir, "num-inner-pagelets.txt"), 12));
        reporters.add(new Non200ResponseCodeReporter(new FileReport(outputDir,
                "non-200-repsonse.txt")));
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
        reporters.add(new DefaultArgumentsAsPageCriteriaReporter(
                new FileReport(outputDir,
                        "defaultArgumentsAsPageCriteriaReporter.txt")));
        return reporters;
    }

}