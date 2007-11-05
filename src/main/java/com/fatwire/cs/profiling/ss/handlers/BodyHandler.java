package com.fatwire.cs.profiling.ss.handlers;

import java.util.List;
import java.util.concurrent.Callable;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.SSUri;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyHandler implements Callable<ResultPage> {

    private final ResultPage body;

    private final SSUriHelper uriHelper;

    /**
     * @param body
     */
    public BodyHandler(final ResultPage body, final SSUriHelper uriHelper) {
        super();
        this.body = body;
        this.uriHelper=uriHelper;
    }

    public ResultPage call() throws Exception {

        final Callable<List<SSUri>> t1 = new BodyMarkerHandler(body.getBody(),
                uriHelper);
        final Callable<List<SSUri>> t2 = new ShortBodyMarkerHandler(body
                .getBody(), uriHelper);

        final Callable<List<SSUri>> t3 = new BodyLinkHandler(body.getBody(),
                uriHelper);
        final Callable<List<SSUri>> t4 = new BodyRawLinkHandler(
                body.getBody(), uriHelper);
        final Callable<List<SSUri>> t5 = new SSUnqualifiedBodyLinkHandler(body
                .getBody(), uriHelper);

        body.addMarkers(t1.call());
        body.addMarkers(t2.call());
        body.addLinks(t3.call());
        body.addLinks(t4.call());
        body.addLinks(t5.call());
        return body;
    }

}
