package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ThreadPoolExecutor;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.jobs.CommandJob;
import com.fatwire.cs.profiling.ss.jobs.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.jobs.JobListener;
import com.fatwire.cs.profiling.ss.jobs.JobScheduledEvent;
import com.fatwire.cs.profiling.ss.jobs.JobStartedEvent;
import com.fatwire.cs.profiling.ss.reporting.ReportingListener;
import com.fatwire.cs.profiling.ss.reporting.reporters.InnerPageletReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.LinkCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NestingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.Non200ResponseCodeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.NumberOfInnerPageletsReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCollectingReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageCriteriaReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageRenderTimeReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.PageletOnlyReporter;
import com.fatwire.cs.profiling.ss.reporting.reporters.RootElementReporter;
import com.fatwire.cs.profiling.ss.reporting.reports.FileReport;
import com.fatwire.cs.profiling.ss.reporting.reports.ReportCollection;
import com.fatwire.cs.profiling.ss.reporting.reports.StdOutReport;
import com.fatwire.cs.profiling.ss.reporting.reports.WarningLevelLoggingReport;
import com.fatwire.cs.profiling.ss.util.TempDir;

public class App {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        //        work(
        //                "http://www.fatwire.com/cs/",
        //                "/cs/ContentServer?c=Page&cid=1141059098394&p=1141059098394&pagename=FW%2FRenderTheHomePage_US",
        //                15);

        work("http://radium.nl.fatwire.com:8080/cs/" ,
                "/cs/ContentServer?pagename=FSIIWrapper&cid=1118867611403&c=Page&p=1118867611403&childpagename=FirstSiteII/FSIILayout",
                Integer.MAX_VALUE);

    }

    private static void work(String host, String start, int maxPages) {

        final HostConfig hc = App.createHostConfig(URI.create(host));
        final ThreadPoolExecutor readerPool = new RenderingThreadPool();
        final RenderCommand command = new RenderCommand(hc, maxPages,
                readerPool);
        command.addStartUri(start);

        final File outputDir = App.getOutputDir();

        final ReportingListener reportingListener = new ReportingListener();

        reportingListener.addReporter(new LinkCollectingReporter(
                new FileReport(outputDir, "links.txt")));

        reportingListener.addReporter(new PageCollectingReporter(new File(
                outputDir, "pages")));
        reportingListener.addReporter(new PageCriteriaReporter(new FileReport(
                outputDir, "pagecriteria.txt")));
        reportingListener.addReporter(new PageRenderTimeReporter(
                new ReportCollection(new StdOutReport(), new FileReport(
                        outputDir, "timings.txt"))));

        reportingListener.addReporter(new RootElementReporter(
                new StdOutReport()));
        reportingListener.addReporter(new NumberOfInnerPageletsReporter(
                new WarningLevelLoggingReport("report"), 4));
        reportingListener.addReporter(new Non200ResponseCodeReporter(
                new WarningLevelLoggingReport("report")));
        reportingListener.addReporter(new InnerPageletReporter(
                new StdOutReport()));
        reportingListener.addReporter(new NestingReporter(new FileReport(
                outputDir, "nesting.txt"),10));
        reportingListener.addReporter(new PageletOnlyReporter(new FileReport(
                outputDir, "pagelet-only.txt")));

        
        command.addListener(reportingListener);
        final CommandJob job = new CommandJob(command);
        job.addListener(reportingListener);
        job.addListener(new JobListener() {

            public void jobFinished(JobFinishedEvent event) {
                readerPool.shutdown();

            }

            public void jobScheduled(JobScheduledEvent event) {

            }

            public void jobStarted(JobStartedEvent event) {

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

}