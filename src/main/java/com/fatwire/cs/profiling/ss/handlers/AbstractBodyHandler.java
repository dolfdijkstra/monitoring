package com.fatwire.cs.profiling.ss.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.SSUri;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public abstract class AbstractBodyHandler implements Callable<List<SSUri>> {
    static final Log log = LogFactory.getLog(AbstractBodyHandler.class);

    protected final String body;

    private final List<SSUri> links = new ArrayList<SSUri>();

    final protected SSUriHelper uriHelper;

    /**
     * @param body
     */
    public AbstractBodyHandler(final String body, final SSUriHelper uriHelper) {
        super();
        this.body = body;
        this.uriHelper = uriHelper;
    }

    public final List<SSUri> call() throws Exception {
        doWork();
        return links;
    }

    protected abstract void doWork();

    protected final void addLink(final SSUri map) {
        if (map !=null  && map.isOK()) {

            if (log.isTraceEnabled()) {
                log.trace("adding link: " + uriHelper.toLink(map));
            }
            links.add(map);
        }

    }

}
