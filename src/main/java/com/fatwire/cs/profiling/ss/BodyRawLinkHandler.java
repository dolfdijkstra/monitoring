package com.fatwire.cs.profiling.ss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BodyRawLinkHandler extends AbstractBodyHandler {

    private final Log log = LogFactory.getLog(getClass());

    private final Pattern rawLinkPattern = Pattern
            .compile("/cs/Satellite.*?[\"']");

    public BodyRawLinkHandler(final String body, final String path) {
        super(body, path);

    }

    @Override
    protected void doWork() {

        final Matcher m = rawLinkPattern.matcher(body);

        while (m.find()) {
            log.info(m.group());
            //doTag(m.group());
        }

    }

}
