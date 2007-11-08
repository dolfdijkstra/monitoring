package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Pattern;

import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class ShortBodyMarkerHandler extends AbstractBodyMarkerHandler {

    private static final String tagName = "page";

    private static final Pattern pattern = Pattern.compile("(<" + tagName
            + ")(\\s(\\w*=\".*?\")?)*(/" + tagName + ">)");

    private static final Pattern tagPattern = Pattern.compile(" .*?=\".*?\"");

    public ShortBodyMarkerHandler(final SSUriHelper uriHelper) {
        super(uriHelper);
    }

    @Override
    protected Pattern getPagePattern() {
        return pattern;
    }

    @Override
    protected Pattern getTagPattern() {
        return tagPattern;
    }

}
