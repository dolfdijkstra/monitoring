package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.handlers.BodyHandler;
import com.fatwire.cs.profiling.ss.jobs.CommandJob;
import com.fatwire.cs.profiling.ss.jobs.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.jobs.JobListener;
import com.fatwire.cs.profiling.ss.jobs.JobScheduledEvent;
import com.fatwire.cs.profiling.ss.jobs.JobStartedEvent;
import com.fatwire.cs.profiling.ss.reporting.Reporter;
import com.fatwire.cs.profiling.ss.reporting.ReportingListener;
import com.fatwire.cs.profiling.ss.reporting.reporters.InnerLinkReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.InnerPageletReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.LinkCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NestingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.Non200ResponseCodeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NotCachedReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NumberOfInnerPageletsReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCriteriaReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageRenderTimeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageletOnlyReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageletTimingsStatisticsReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.RootElementReporter;
import com.fatwire.cs.profiling.ss.reporting.reports.FileReport;
import com.fatwire.cs.profiling.ss.reporting.reports.ReportCollection;
import com.fatwire.cs.profiling.ss.reporting.reports.StdOutReport;
import com.fatwire.cs.profiling.ss.reporting.reports.WarningLevelLoggingReport;
import com.fatwire.cs.profiling.ss.util.DecodingSSUriHelper;
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
        if (args.length == 2) {
            App.work(args[0], args[1], Integer.MAX_VALUE);
        } else {
            App.work(args[0], args[1], Integer.parseInt(args[2]));
        }

    }

    private static void work(final String host, final String start,
            final int maxPages) {

        final HostConfig hc = App.createHostConfig(URI.create(host));
        final ThreadPoolExecutor readerPool = new RenderingThreadPool();
        final RenderCommand command = new RenderCommand(hc, maxPages,
                readerPool);
        command.addStartUri(start);
        final SSUriHelper uriHelper = new DecodingSSUriHelper(hc.getDomain());
        command.setUriHelper(uriHelper);
        command.setHandler(new BodyHandler(uriHelper));

        final ReportingListener reportingListener = new ReportingListener();

        for (Reporter reporter : App.createReporters(App.getOutputDir())) {
            reportingListener.addReporter(reporter);
        }

        command.addListener(reportingListener);
        final CommandJob job = new CommandJob(command);
        job.addListener(reportingListener);
        job.addListener(new JobListener() {

            public void jobFinished(final JobFinishedEvent event) {
                readerPool.shutdown();

            }

            public void jobScheduled(final JobScheduledEvent event) {

            }

            public void jobStarted(final JobStartedEvent event) {

            }

        });
        job.schedule();
    }

    private static File getOutputDir() {
        final File outputDir = new File(TempDir.getTempDir(App.class), Long
                .toString(System.currentTimeMillis()));
        outputDir.mkdirs();
        return outputDir;
    }

    private static HostConfig createHostConfig(final URI uri) {
        final HostConfig hostConfig = new HostConfig();

        hostConfig.setHostname(uri.getHost());

        hostConfig.setPort(uri.getPort() == -1 ? 80 : uri.getPort());
        hostConfig.setDomain(uri.getPath());

        return hostConfig;

    }

    static Iterable<Reporter> createReporters(File outputDir) {

        List<Reporter> reporters = new ArrayList<Reporter>();
        reporters.add(new LinkCollectingReporter(new FileReport(outputDir,
                "links.txt")));

        reporters.add(new PageCollectingReporter(new File(outputDir, "pages")));

        reporters.add(new PageletTimingsStatisticsReporter(new FileReport(
                outputDir, "pagelet-stats.txt")));

        reporters.add(new PageCriteriaReporter(new FileReport(outputDir,
                "pagecriteria.txt")));
        reporters.add(new PageRenderTimeReporter(new ReportCollection(
                new StdOutReport(), new FileReport(outputDir, "timings.txt"))));

        reporters.add(new RootElementReporter(new FileReport(outputDir,
                "root-elements.txt")));
        reporters.add(new NumberOfInnerPageletsReporter(
                new WarningLevelLoggingReport("report"), 4));
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