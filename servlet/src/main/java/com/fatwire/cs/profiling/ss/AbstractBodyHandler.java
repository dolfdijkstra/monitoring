package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBodyHandler implements Callable<List<String>> {
    private final Log log = LogFactory.getLog(getClass());

    protected final String body;

    protected final String path;

    private final List<String> links = new ArrayList<String>();

    /**
     * @param body
     */
    public AbstractBodyHandler(final String body, final String path) {
        super();
        this.body = body;
        this.path = path;
    }

    public final List<String> call() throws Exception {
        doWork();
        return links;
    }

    protected abstract void doWork();

    protected final void addLink(final Map<String, String> map) {
        if (map.isEmpty()) {
            return;
        }
        map.remove("cachecontrol");
        map.remove("rendermode");
        final StringBuilder qs = new StringBuilder();
        qs.append(path);
        qs.append("ContentServer");
        qs.append("?");
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            qs.append(entry.getKey());
            qs.append("=");
            qs.append(entry.getValue());
            qs.append("&");
        }
        qs.append("ft_ss=true");
        final String url = qs.toString();
        if (log.isTraceEnabled()) {
            log.trace("adding link: " + url);
        }
        links.add(url);

    }

}
