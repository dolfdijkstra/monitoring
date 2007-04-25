package com.fatwire.cs.profiling.ss;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BodyLinkHandler extends AbstractBodyHandler {
    private final Log log = LogFactory.getLog(getClass());

    private final Pattern linkPattern = Pattern
            .compile("satellitescheme://SSURI/.*?#satellitefragment");

    /**
     * @param body
     * @param path
     */
    public BodyLinkHandler(String body, String path) {
        super(body, path);
    }

    protected void doWork() {
        //System.out.println(body);
        final Matcher m = linkPattern.matcher(body);
        //"<com.fatwire.satellite.page pagename="FirstSiteII/FSIILayout" cid="1118867611403" locale="1154551493541" rendermode="live" p="1118867611403" c="Page" /com.fatwire.satellite.page>
        while (m.find()) {
            log.debug(m.group());
            doLink(m.group());
        }

    }

    void doLink(final String link) {
        log.trace(link);
        try {

            final URI uri = new URI(StringEscapeUtils.unescapeXml(link));
            log.trace(uri.getQuery());
            final String[] val = uri.getQuery().split("&");
            final Map<String, String> map = new TreeMap<String, String>();
            for (final String v : val) {
                if (!v.startsWith("SSURI")) {
                    final int t = v.indexOf('=');
                    map.put(v.substring(0, t), v.substring(t + 1, v.length()));
                } else {
                    if ("SSURIapptype=BlobServer".equals(v)) {
                        map.clear();
                        break;
                    }
                }
            }
            if (!map.isEmpty()) {
                addLink(map);
            }
        } catch (final URISyntaxException e) {
            log.error(e);
        }
    }

}
