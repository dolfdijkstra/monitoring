package com.fatwire.cs.profiling.ss.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatwire.cs.profiling.ss.Pagelet;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public abstract class AbstractBodyMarkerHandler extends AbstractBodyHandler {

    private static final Pattern tagPattern = Pattern.compile(" .*?=\".*?\"");

    protected Pattern getTagPattern() {
        return tagPattern;
    }

    protected abstract Pattern getPagePattern();

    public AbstractBodyMarkerHandler(final SSUriHelper uriHelper) {
        super(uriHelper);
    }

    public final void visit(ResultPage page) {
        final Matcher m = getPagePattern().matcher(page.getBody());
        //"<com.fatwire.satellite.page pagename="FirstSiteII/FSIILayout" cid="1118867611403" locale="1154551493541" rendermode="live" p="1118867611403" c="Page" /com.fatwire.satellite.page>
        while (m.find()) {
            log.debug(m.group());
            doTag(m.group(), page);
        }

    }

    private void doTag(final String tag, ResultPage page) {
        final Matcher m = getTagPattern().matcher(tag);
        final Pagelet map = new Pagelet();
        while (m.find()) {
            log.trace(m.group());
            final String x = m.group();
            final int t = x.indexOf('=');
            final String key = x.substring(0, t).trim();
            if (filter(key)) {
                String value;
                value = x.substring(t + 2, x.length() - 1);
                map.addParameter(key, value);
            }

        }
        page.addMarker(map);

    }

    protected boolean filter(String key) {
        return !"cachecontrol".equalsIgnoreCase(key);
    }

}