package com.fatwire.cs.profiling.ss.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.handlers.BodyLinkHandler;

public class UriUtil {
    private static final Log log = LogFactory.getLog(BodyLinkHandler.class);

    private static final String PAGENAME_KEY = "pagename=";

    private static final String UTF8 = "utf-8";

    public static String extractPageName(final String uri) {
        final String qs = URI.create(uri).getRawQuery();
        if (qs != null) {
            for (final String p : qs.split("&")) {
                if (p.startsWith(UriUtil.PAGENAME_KEY)) {
                    try {
                        return URLDecoder.decode(p.substring(UriUtil.PAGENAME_KEY
                                .length()), UriUtil.UTF8);
                    } catch (final UnsupportedEncodingException e) {
                        UriUtil.log.error(e, e);
                    }
                }
            }

        }
        return null;

    }

    public static Map<String, String> extractParams(final String uri) {
        final String qs = URI.create(uri).getRawQuery();
        final Map<String, String> map = new TreeMap<String, String>();
        if (qs != null) {
            for (final String p : qs.split("&")) {
                final String[] nvp = p.split("=");
                try {
                    final String key = URLDecoder.decode(nvp[0], UriUtil.UTF8);
                    if (nvp.length > 0) {
                        map.put(key, URLDecoder.decode(nvp[1], UriUtil.UTF8));

                    } else {
                        map.put(key, null);
                    }
                } catch (final UnsupportedEncodingException e) {
                    UriUtil.log.error(e, e);
                }
            }

        }
        return map;
    }
}
