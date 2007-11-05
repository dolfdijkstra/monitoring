package com.fatwire.cs.profiling.ss.handlers;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.SSUri;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyLinkHandler extends AbstractBodyHandler {
    static final Log log = LogFactory.getLog(BodyLinkHandler.class);

    private final Pattern linkPattern = Pattern
            .compile("satellitescheme://SSURI/.*?#satellitefragment");

    /**
     * @param body
     * @param uriHelper
     */
    public BodyLinkHandler(String body, final SSUriHelper uriHelper) {
        super(body, uriHelper);
    }

    protected void doWork() {
        final Matcher m = linkPattern.matcher(body);
        while (m.find()) {
            log.debug(m.group());
            doLink(m.group());
        }

    }

    void doLink(final String link) {
        log.trace(link);
        try {
            SSUri map = uriHelper.linkToMap(link);
            if (map.isOK()) {
                addLink(map);
            }
        } catch (final URISyntaxException e) {
            log.error(e);
        }
    }
}
