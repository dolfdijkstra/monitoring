package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyRawLinkHandler extends AbstractBodyHandler {

    private final Log log = LogFactory.getLog(getClass());

    private final Pattern rawLinkPattern = Pattern
            .compile("Satellite\\?.*?[\"']");

    public BodyRawLinkHandler(final SSUriHelper uriHelper) {
        super(uriHelper);

    }

    public void visit(ResultPage page) {

        final Matcher m = rawLinkPattern.matcher(page.getBody());

        while (m.find()) {
            log.warn(page.getUri() + " has a raw link: " + m.group());
            //todo add as a link
        }

    }

}
