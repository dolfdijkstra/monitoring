package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatwire.cs.profiling.ss.Link;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyLinkHandler extends AbstractBodyHandler {

    private final Pattern linkPattern = Pattern
            .compile("satellitescheme://SSURI/.*?#satellitefragment");

    /**
     * @param body
     * @param uriHelper
     */
    public BodyLinkHandler(final SSUriHelper uriHelper) {
        super(uriHelper);
    }

    public void visit(ResultPage page) {
        final Matcher m = linkPattern.matcher(page.getBody());
        while (m.find()) {
            log.debug(m.group());
            Link map = uriHelper.linkToMap(m.group());
            if (map.isOK()) {
                page.addLink(map);
            }
        }
    }

}
