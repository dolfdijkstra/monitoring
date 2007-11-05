package com.fatwire.cs.profiling.ss;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.events.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.events.JobListener;
import com.fatwire.cs.profiling.ss.events.JobScheduledEvent;
import com.fatwire.cs.profiling.ss.events.JobStartedEvent;
import com.fatwire.cs.profiling.ss.events.LinkCollectingRenderListener;
import com.fatwire.cs.profiling.ss.events.PageCollectingRenderListener;
import com.fatwire.cs.profiling.ss.events.PageCriteriaRenderListener;
import com.fatwire.cs.profiling.ss.jobs.CommandJob;
import com.fatwire.cs.profiling.ss.util.HelperStrings;
import com.fatwire.cs.profiling.ss.util.TempDir;

public class App {
    private static final Log LOG = LogFactory.getLog(App.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {
        //";
        final HostConfig hc = App.createHostConfig(URI
                .create("http://radium.nl.fatwire.com:8080/cs/"));

        final RenderCommand command = new RenderCommand(hc, 500);
        command
                .addStartUri("/cs/ContentServer?pagename=FSIIWrapper&cid=1118867611403&c=Page&p=1118867611403&childpagename=FirstSiteII/FSIILayout&"
                        + HelperStrings.SS_PAGEDATA_REQUEST + "=true");
        final File outputDir = App.getOutputDir();
        final PageletTimingsCollector timingsCollector = new PageletTimingsCollector(
                new File(outputDir, "links.txt"));
        try {
            timingsCollector.open();
            command.addListener(new LinkCollectingRenderListener(
                    timingsCollector));

        } catch (final IOException e) {
            App.LOG.error(e, e);
        }

        command.addListener(new PageCollectingRenderListener(outputDir));
        command.addListener(new PageCriteriaRenderListener());

        final CommandJob job = new CommandJob(command);
        job.addListener(new JobListener() {

            public void jobFinished(final JobFinishedEvent event) {
                timingsCollector.report();

            }

            public void jobScheduled(final JobScheduledEvent event) {
                // TODO Auto-generated method stub

            }

            public void jobStarted(final JobStartedEvent event) {
                // TODO Auto-generated method stub

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