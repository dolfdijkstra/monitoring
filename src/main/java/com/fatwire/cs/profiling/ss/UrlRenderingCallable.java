package com.fatwire.cs.profiling.ss;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.handlers.BodyHandler;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class UrlRenderingCallable implements Callable<ResultPage> {
    private final Log log = LogFactory.getLog(getClass());

    final HttpClient client;

    final String uri;

    final SSUriHelper uriHelper;

    /**
     * @param client
     * @param uri
     */
    public UrlRenderingCallable(final HttpClient client, final String uri,
            final SSUriHelper uriHelper) {
        super();
        this.client = client;
        this.uri = uri;
        this.uriHelper = uriHelper;
    }

    public ResultPage call() throws Exception {
        if (log.isDebugEnabled())
            log.debug("downloading " + uri);
        final long startTime = System.currentTimeMillis();
        final ResultPage page = new ResultPage(uri);
        final GetMethod httpGet = new GetMethod(uri);
        httpGet.setFollowRedirects(true);

        try {
            final int responseCode = client.executeMethod(httpGet);
            //log.info(iGetResultCode);
            if (responseCode == 200) {

                page.setResponseHeaders(httpGet.getResponseHeaders());
                final String charSet = httpGet.getResponseCharSet();
                //log.info(charSet);

                final InputStream in = httpGet.getResponseBodyAsStream();
                if (in != null) {
                    final Reader reader = new InputStreamReader(in, Charset
                            .forName(charSet));
                    final String responseBody = copy(reader);
                    in.close();
                    page.setReadTime(System.currentTimeMillis() - startTime);

                    if (responseBody != null) {
                        if (log.isTraceEnabled()) {
                            log.trace(responseBody);
                        }
                        page.setBody(responseBody);
                        final BodyHandler handler = new BodyHandler(page, uriHelper);

                        handler.call();

                    }
                }
            } else {
                log.error("reponse code is " + responseCode + " for "
                        + httpGet.getURI().toString());
            }

        } finally {
            httpGet.releaseConnection();
        }
        return page;
    }

    /**
     * @param builder
     * @param reader
     * @throws IOException
     */
    private String copy(final Reader reader) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final char[] c = new char[1024];
        int s;
        while ((s = reader.read(c)) != -1) {
            builder.append(c, 0, s);

        }
        return builder.toString();
    }

}
