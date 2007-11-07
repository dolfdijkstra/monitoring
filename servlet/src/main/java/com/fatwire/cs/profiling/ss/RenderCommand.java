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
import com.fatwire.cs.profiling.ss.jobs.Command;

public class RenderCommand implements Command {
    private static final Log LOG = LogFactory.getLog(RenderCommand.class);

    private final List<String> queue = new ArrayList<String>();

    private final HostConfig hostConfig;

    private int maxPages = 500;

    final ThreadPoolExecutor executor;

    private final Set<PageletRenderingListener> listeners = new CopyOnWriteArraySet<PageletRenderingListener>();

    /**
     * @param hostConfig
     * @param maxPages
     */
    public RenderCommand(final HostConfig hostConfig, int maxPages,
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

    public void addStartUri(String uri) {
        queue.add(uri);
    }

    public void execute() {

        final URLReaderService reader = new URLReaderService(executor);

        reader.setHostConfig(hostConfig);
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
        this.listeners.add(listener);
    }

    public void removeListener(final PageletRenderingListener listener) {
        this.listeners.remove(listener);
    }

}