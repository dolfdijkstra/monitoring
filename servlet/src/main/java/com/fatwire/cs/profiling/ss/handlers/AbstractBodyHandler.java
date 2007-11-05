package com.fatwire.cs.profiling.ss.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.SSUri;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public abstract class AbstractBodyHandler implements Visitor<ResultPage> {
    static final Log log = LogFactory.getLog(AbstractBodyHandler.class);


    final protected SSUriHelper uriHelper;

    /**
     * @param body
     */
    public AbstractBodyHandler(final SSUriHelper uriHelper) {
        super();
        this.uriHelper = uriHelper;
    }


    protected final void addLink(final SSUri map) {
        if (map !=null  && map.isOK()) {

            if (log.isTraceEnabled()) {
                log.trace("adding link: " + uriHelper.toLink(map));
            }
            //links.add(map);
        }

    }

}
