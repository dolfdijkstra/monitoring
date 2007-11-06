package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.events.LinkCollectingRenderListener;
import com.fatwire.cs.profiling.ss.events.PageCollectingRenderListener;
import com.fatwire.cs.profiling.ss.events.PageCriteriaRenderListener;
import com.fatwire.cs.profiling.ss.jobs.CommandJob;
import com.fatwire.cs.profiling.ss.jobs.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.jobs.JobListener;
import com.fatwire.cs.profiling.ss.jobs.JobScheduledEvent;
import com.fatwire.cs.profiling.ss.jobs.JobStartedEvent;
import com.fatwire.cs.profiling.ss.util.TempDir;

public class App {
    private static final Log LOG = LogFactory.getLog(App.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {

        work(
                "http://www.fatwire.com/cs/",
                "/cs/ContentServer?c=Page&cid=1141059098394&p=1141059098394&pagename=FW%2FRenderTheHomePage_US",
                500);

        //            .create("http://radium.nl.fatwire.com:8080/cs/"));
        //
        //           .addStartUri("/cs/ContentServer?pagename=FSIIWrapper&cid=1118867611403&c=Page&p=1118867611403&childpagename=FirstSiteII/FSIILayout&"
        //                            + HelperStrings.SS_PAGEDATA_REQUEST + "=true");

    }

    private static void work(String host, String start, int maxPages) {
        //";
        final HostConfig hc = App.createHostConfig(URI.create(host));

        final RenderCommand command = new RenderCommand(hc, maxPages);
        command.addStartUri(start);
        //+"&"  + HelperStrings.SS_PAGEDATA_REQUEST + "=true");
        final File outputDir = App.getOutputDir();
        final PageletTimingsCollector timingsCollector = new PageletTimingsCollector(
                new File(outputDir, "links.txt"));
        File pageDir = new File(outputDir, "pages");
        pageDir.mkdirs();

        command.addListener(new LinkCollectingRenderListener(timingsCollector));

        command.addListener(new PageCollectingRenderListener(pageDir));
        command.addListener(new PageCriteriaRenderListener());

        final CommandJob job = new CommandJob(command);
        job.addListener(new JobListener() {

            public void jobFinished(final JobFinishedEvent event) {
                timingsCollector.report();
                try {
                    timingsCollector.close();
                } catch (IOException e) {
                    LOG.error(e, e);
                }

            }

            public void jobScheduled(final JobScheduledEvent event) {
                // TODO Auto-generated method stub

            }

            public void jobStarted(final JobStartedEvent event) {
                try {
                    timingsCollector.open();
                } catch (IOException e) {
                    LOG.error(e, e);
                }

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