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

import com.fatwire.cs.profiling.ss.Link;
import com.fatwire.cs.profiling.ss.QueryString;

public class SSUriHelper {
    protected final Log log = LogFactory.getLog(getClass());

    public static final String SSURI_PREFIX = "SSURI";

    public static final String SSURI_BLOBSERVER = "SSURIapptype=BlobServer";

    private final String domain;

    private static final String UTF8 = "utf-8";

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
            return qs.toString();
        } catch (EncoderException e) {
            log.warn(e);
            return null;
        } catch (RuntimeException e) {
            log.warn(e);
            return null;
        }

    }

    public final Link linkToMap(final String link) {

        return uriToQueryString(URI.create(StringEscapeUtils.unescapeXml(link)));

    }

    public Link uriToQueryString(final URI uri) {
        String qs = uri.getQuery();
        if (log.isDebugEnabled()) {
            log.debug(qs);
        }
        final Link map = new Link();
        if (qs == null){
            return map;
        }
        final String[] val = uri.getQuery().split("&");
        
        for (final String v : val) {
            if (!v.startsWith(SSURI_PREFIX)) {
                final int t = v.indexOf('=');
                map.addParameter(v.substring(0, t), v.substring(t + 1, v
                        .length()));
            } else {
                if (SSURI_BLOBSERVER.equals(v)) {
                    map.clear();
                    break;
                }
            }
        }
        return map;

    }

    protected String getCharSet() {
        return UTF8;
    }

//    public QueryString extractParams(final URI uri) {
//        final String qs = uri.getRawQuery();
//        final QueryString map = new QueryString();
//        if (qs != null) {
//            for (final String p : qs.split("&")) {
//                final String[] nvp = p.split("=");
//                try {
//                    final String key = URLDecoder.decode(nvp[0], getCharSet());
//                    if (nvp.length > 0) {
//                        map.addParameter(key, URLDecoder.decode(nvp[1],
//                                getCharSet()));
//
//                    } else {
//                        map.addParameter(key, null);
//                    }
//                } catch (final UnsupportedEncodingException e) {
//                    log.error(e, e);
//                }
//            }
//
//        }
//        return map;
//    }

}
