package com.fatwire.cs.profiling.ss.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public abstract class AbstractBodyHandler implements Visitor<ResultPage> {
    protected final Log log = LogFactory.getLog(getClass());

    final protected SSUriHelper uriHelper;

    /**
     * 
     * @param uriHelper
     */
    public AbstractBodyHandler(final SSUriHelper uriHelper) {
        super();
        this.uriHelper = uriHelper;
    }

}
