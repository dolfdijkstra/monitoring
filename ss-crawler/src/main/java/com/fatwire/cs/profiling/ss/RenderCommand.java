package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.domain.HostConfig;
import com.fatwire.cs.profiling.ss.events.PageletRenderedEvent;
import com.fatwire.cs.profiling.ss.events.PageletRenderingListener;
import com.fatwire.cs.profiling.ss.handlers.BodyHandler;
import com.fatwire.cs.profiling.ss.jobs.Command;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class RenderCommand implements Command {
    private static final Log LOG = LogFactory.getLog(RenderCommand.class);

    private final List<String> queue = new ArrayList<String>();

    private int maxPages;

    private HostConfig hostConfig;

    private SSUriHelper uriHelper;

    private BodyHandler handler;

    private final ThreadPoolExecutor executor;

    private final Set<PageletRenderingListener> listeners = new CopyOnWriteArraySet<PageletRenderingListener>();

    /**
     * @param hostConfig
     * @param maxPages
     */
    public RenderCommand(final HostConfig hostConfig, final int maxPages,
            final ThreadPoolExecutor executor) {
        super();
        this.hostConfig = hostConfig;
        this.maxPages = maxPages;
        this.executor = executor;
    }

    public RenderCommand(final HostConfig hostConfig,
            final ThreadPoolExecutor readerPool) {
        this(hostConfig, Integer.MAX_VALUE, readerPool);
    }

    public void addStartUri(final String uri) {
        queue.add(uri);
    }

    public void execute() {
        if ((executor == null) || (hostConfig == null)
                || (handler == null) || (uriHelper == null)
                || (maxPages < 1) || queue.isEmpty()) {
            throw new IllegalStateException(
                    "One or more dependancies are not set or not set correctly");
        }
        final URLReaderService reader = new URLReaderService(executor);

        reader.setHostConfig(hostConfig);
        reader.setHandler(handler);
        reader.setUriHelper(uriHelper);
        reader.setMaxPages(maxPages);

        reader.addStartUris(queue);

        final PageletRenderingListener readerListener = new PageletRenderingListener() {

            public void renderPerformed(final PageletRenderedEvent event) {
                for (final PageletRenderingListener listener : listeners) {
                    listener.renderPerformed(event);
                }

            }

        };

        reader.addListener(readerListener);

        reader.start();
        reader.removeListener(readerListener);

    }

    public void addListener(final PageletRenderingListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final PageletRenderingListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return the handler
     */
    public BodyHandler getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(final BodyHandler handler) {
        this.handler = handler;
    }

    /**
     * @return the hostConfig
     */
    public HostConfig getHostConfig() {
        return hostConfig;
    }

    /**
     * @param hostConfig the hostConfig to set
     */
    public void setHostConfig(final HostConfig hostConfig) {
        this.hostConfig = hostConfig;
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
    public void setUriHelper(final SSUriHelper uriHelper) {
        this.uriHelper = uriHelper;
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
    public void setMaxPages(final int maxPages) {
        this.maxPages = maxPages;
    }

}