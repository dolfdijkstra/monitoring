package com.fatwire.cs.profiling.ss.util;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.QueryString;

public class SSUriHelper {
    private final Log log = LogFactory.getLog(getClass());

    private final String domain;

    private final URLCodec urlCodec = new URLCodec();

    /**
     * @param domain
     */
    public SSUriHelper(final String domain) {
        super();
        this.domain = domain;
    }

    public String toLink(QueryString uri) {
        if (uri.getParameters().isEmpty()) {
            return null;
        }
        try {
            Map<String, String> map = new TreeMap<String, String>(uri
                    .getParameters());
            map.remove(HelperStrings.CACHECONTROL);
            map.remove(HelperStrings.RENDERMODE);
            final StringBuilder qs = new StringBuilder();
            qs.append(domain);
            qs.append("ContentServer");
            qs.append("?");
            for (final Iterator<Map.Entry<String, String>> i = map.entrySet()
                    .iterator(); i.hasNext();) {
                final Map.Entry<String, String> entry = i.next();
                qs.append(urlCodec.encode(entry.getKey()));
                qs.append("=");
                String v = entry.getValue();
                if (v != null && v.startsWith(HelperStrings.SSURI_START)) {

                    QueryString inner = linkToMap(v);
                    qs.append(urlCodec.encode(toLink(inner)));
                } else {
                    qs.append(urlCodec.encode(v));
                }
                if (i.hasNext()) {
                    qs.append("&");
                }
            }
            //qs.append(HelperStrings.SS_PAGEDATA_REQUEST);
            //qs.append("=true");
            return qs.toString();
        } catch (EncoderException e) {
            log.warn(e);
            return null;
        } catch (RuntimeException e) {
            log.warn(e);
            return null;
        }

    }

    public QueryString linkToMap(final String link) {

        final URI uri = URI.create(StringEscapeUtils.unescapeXml(link));
        log.debug(uri.getQuery());
        final String[] val = uri.getQuery().split("&");
        final QueryString map = new QueryString();
        for (final String v : val) {
            if (!v.startsWith("SSURI")) {
                final int t = v.indexOf('=');
                map.addParameter(v.substring(0, t), v.substring(t + 1, v
                        .length()));
            } else {
                if ("SSURIapptype=BlobServer".equals(v)) {
                    map.clear();
                    break;
                }
            }
        }
        return map;

    }

}