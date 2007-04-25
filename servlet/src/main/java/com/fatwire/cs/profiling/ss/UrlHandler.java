package com.fatwire.cs.profiling.ss;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrlHandler implements Callable<ResultPage> {
    private final Log log = LogFactory.getLog(getClass());

    final HttpClient client;

    final String uri;

    final String path;

    /**
     * @param client
     * @param uri
     */
    public UrlHandler(final HttpClient client, final String uri,
            final String path) {
        super();
        this.client = client;
        this.uri = uri;
        this.path = path;
    }

    public ResultPage call() throws Exception {
        log.info(uri);
        ResultPage page = new ResultPage(uri);
        final GetMethod get = new GetMethod(uri);
        get.setFollowRedirects(true);

        try {
            final int iGetResultCode = client.executeMethod(get);
            //log.info(iGetResultCode);
            if (iGetResultCode == 200) {
                final String charSet = get.getResponseCharSet();
                //log.info(charSet);

                final StringBuilder builder = new StringBuilder();
                final InputStream in = get.getResponseBodyAsStream();

                final Reader reader = new InputStreamReader(in, Charset
                        .forName(charSet));
                final char[] c = new char[1024];
                int s;
                while ((s = reader.read(c)) != -1) {
                    builder.append(c, 0, s);

                }
                in.close();
                final String responseBody = builder.toString();

                if (responseBody != null) {
                    page.setBody(responseBody);
                    BodyHandler handler = new BodyHandler(page, path);

                    handler.call();

                }
            } else {
                log.error("reponse code is " + iGetResultCode);
            }

        } finally {
            get.releaseConnection();
        }
        return page;
    }

}
