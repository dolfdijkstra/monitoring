package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyRawLinkHandler extends AbstractBodyHandler {

    private final Log log = LogFactory.getLog(getClass());

    private final Pattern rawLinkPattern = Pattern
            .compile("Satellite\\?.*?[\"']");

    public BodyRawLinkHandler(final String body, final SSUriHelper uriHelper) {
        super(body, uriHelper);

    }

    @Override
    protected void doWork() {

        final Matcher m = rawLinkPattern.matcher(body);

        while (m.find()) {
            log.debug(m.group());
            //doTag(m.group());
        }

    }

}
