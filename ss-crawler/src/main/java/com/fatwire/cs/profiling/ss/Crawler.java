package com.fatwire.cs.profiling.ss;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executor;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.handlers.BodyHandler;
import com.fatwire.cs.profiling.ss.jobs.StdOutProgressMonitor;
import com.fatwire.cs.profiling.ss.reporting.Reporter;
import com.fatwire.cs.profiling.ss.reporting.ReportingListener;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class Crawler {

    private String host;

    private URI startUri;

    private int maxPages = Integer.MAX_VALUE;

    private List<Reporter> reporters;

    private SSUriHelper uriHelper;

    private Executor executor;

    public void work() {

        final HostConfig hc = createHostConfig(URI.create(host));
        final RenderCommand command = new RenderCommand(hc, maxPages, executor);
        command.addStartUri(uriHelper.uriToQueryString(startUri));
        command.setUriHelper(uriHelper);
        command.setHandler(new BodyHandler(uriHelper));

        final ReportingListener reportingListener = new ReportingListener();

        for (Reporter reporter : reporters) {
            reportingListener.addReporter(reporter);
        }

        command.addListener(reportingListener);

        for (Reporter reporter : reporters) {
            reporter.startCollecting();
        }

        command.execute(new StdOutProgressMonitor());

        for (Reporter reporter : reporters) {
            reporter.endCollecting();
        }

    }

    private HostConfig createHostConfig(final URI uri) {
        final HostConfig hostConfig = new HostConfig();

        hostConfig.setHostname(uri.getHost());

        hostConfig.setPort(uri.getPort() == -1 ? 80 : uri.getPort());
        hostConfig.setDomain(uri.getPath());

        return hostConfig;

    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the maxPages
     */
    public int getMaxPages() {
        return maxPages;
    }

    /**
     * @param maxPages the maxPages to set
     */
    public void setMaxPages(int max) {
        this.maxPages = max;
    }

    /**
     * @return the startUri
     */
    public URI getStartUri() {
        return startUri;
    }

    /**
     * @param startUri the startUri to set
     */
    public void setStartUri(URI startUri) {
        this.startUri = startUri;
    }

    /**
     * @return the reporters
     */
    public List<Reporter> getReporters() {
        return reporters;
    }

    /**
     * @param reporters the reporters to set
     */
    public void setReporters(List<Reporter> reporters) {
        this.reporters = reporters;
    }

    /**
     * @return the executor
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * @param executor the executor to set
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    /**
     * @return the uriHelper
     */
    public SSUriHelper getUriHelper() {
        return uriHelper;
    }

    /**
     * @param uriHelper the uriHelper to set
     */
    public void setUriHelper(SSUriHelper uriHelper) {
        this.uriHelper = uriHelper;
    }

}