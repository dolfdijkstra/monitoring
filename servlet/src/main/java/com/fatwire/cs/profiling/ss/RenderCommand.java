package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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

    private final Set<PageletRenderingListener> listeners = new CopyOnWriteArraySet<PageletRenderingListener>();

    private final PageletRenderingListener pageletRenderingListener = new PageletRenderingListener() {

        public void renderPerformed(final PageletRenderedEvent event) {
            for (final PageletRenderingListener listener : listeners) {
                listener.renderPerformed(event);
            }

        }

    };

    /**
     * @param hostConfig
     * @param maxPages
     */
    public RenderCommand(final HostConfig hostConfig, int maxPages) {
        super();
        this.hostConfig = hostConfig;
        this.maxPages = maxPages;
    }

    public RenderCommand(final HostConfig hostConfig) {
        this(hostConfig, Integer.MAX_VALUE);
    }

    public void addStartUri(String uri) {
        queue.add(uri);
    }

    public void execute() {

        final RenderingThreadPool readerPool = new RenderingThreadPool();

        final URLReaderService reader = new URLReaderService(readerPool);

        reader.setHostConfig(hostConfig);
        reader.setMaxPages(maxPages);

        for (String uri : queue) {
            reader.addStartUri(uri);
        }

        reader.addListener(pageletRenderingListener);

        readerPool.addListener(new RenderingThreadPool.FinishedListener() {

            public void finished() {
                LOG.info("finished");
                Thread t = new Thread(new Runnable() {
                    public void run() {

                        try {

                            //Thread.sleep(1000L);

                            while (readerPool.getActiveCount() > 0) {
                                Thread.sleep(1000L);
                                LOG.info(readerPool.getQueue().size());

                            }

                        } catch (final InterruptedException e) {
                            LOG.warn(e, e);
                        }
                        if (readerPool.getActiveCount() == 0) {
                            readerPool.shutdown();
                        }
                    }
                }, "JobTimeout-Thread-" + RenderCommand.this.hashCode());
                t.start();

            }

        });

        reader.start();

    }

    public void addListener(final PageletRenderingListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(final PageletRenderingListener listener) {
        this.listeners.remove(listener);
    }

}