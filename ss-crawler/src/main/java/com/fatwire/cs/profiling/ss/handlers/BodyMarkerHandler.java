package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Pattern;

import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class BodyMarkerHandler extends AbstractBodyMarkerHandler {

    private static final String tagName = "com\\.fatwire\\.satellite\\.page";
    private static final Pattern pattern = Pattern.compile("(<" + tagName
            + ")(\\s(\\w*=\".*?\")?)*(/" + tagName + ">)");

    

    public BodyMarkerHandler(final SSUriHelper uriHelper) {
        super(uriHelper);
    }

    
    @Override
    protected Pattern getPagePattern(){
        return pattern;
    }

}
