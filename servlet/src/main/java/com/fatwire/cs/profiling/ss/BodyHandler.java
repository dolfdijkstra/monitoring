package com.fatwire.cs.profiling.ss;

import java.util.List;
import java.util.concurrent.Callable;

public class BodyHandler implements Callable<ResultPage> {

    private final ResultPage body;

    private final String path;

    /**
     * @param body
     */
    public BodyHandler(final ResultPage body, final String path) {
        super();
        this.body = body;
        this.path = path;
    }

    public ResultPage call() throws Exception {
        final Callable<List<String>> t1 = new BodyMarkerHandler(body.getBody(),
                path);
        final Callable<List<String>> t2 = new BodyLinkHandler(body.getBody(),
                path);
        final Callable<List<String>> t3 = new BodyRawLinkHandler(
                body.getBody(), path);

        body.addMarkers(t1.call());
        body.addLinks(t2.call());
        body.addLinks(t3.call());
        return body;
    }

}
