package com.fatwire.cs.profiling.ss;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BodyMarkerHandler extends AbstractBodyHandler {
    private final Log log = LogFactory.getLog(getClass());


    private final String tagStart = "<com.fatwire.satellite.page";

    private final Pattern pattern = Pattern.compile(tagStart
            + " .* /com.fatwire.satellite.page>");
    private final Pattern tagPattern = Pattern.compile(" .*?=\".*?\"");
    private final URLCodec urlCodec = new URLCodec();
    


    public BodyMarkerHandler(final String body,final String path) {
        super(body,path);
    }


    protected void doWork() {
        //System.out.println(body);
        final Matcher m = pattern.matcher(body);
        //"<com.fatwire.satellite.page pagename="FirstSiteII/FSIILayout" cid="1118867611403" locale="1154551493541" rendermode="live" p="1118867611403" c="Page" /com.fatwire.satellite.page>
        while (m.find()) {
            log.info(m.group());
            doTag(m.group());
        }

    }
    private void doTag(final String tag) {
        final Matcher m = tagPattern.matcher(tag);
        final Map<String, String> map = new TreeMap<String, String>();
        while (m.find()) {
            log.trace(m.group());
            final String x = m.group();
            final int t = x.indexOf('=');
            final String key = x.substring(0, t).trim();
            String value;
            try {
                value = urlCodec.encode(x.substring(t + 2, x.length() - 1));
                map.put(key, value);
            } catch (final EncoderException e) {
                log.error(e);
            }

        }
        addLink(map);
    }

}
