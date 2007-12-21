package com.fatwire.cs.profiling.ss.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

import com.fatwire.cs.profiling.ss.Link;

public class DecodingSSUriHelper extends SSUriHelper {

    public DecodingSSUriHelper(String domain) {
        super(domain);
    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.ss.util.SSUriHelper#uriToQueryString(java.net.URI)
     */
    @Override
    public Link uriToQueryString(URI uri) {

        if (log.isDebugEnabled()) {
            log.debug(uri.getQuery());
        }
        final String[] val = uri.getQuery().split("&");
        final Link map = new Link();
        for (final String v : val) {
            if (!v.startsWith("SSURI")) {
                final int t = v.indexOf('=');
                try {
                    map.addParameter(URLDecoder.decode(v.substring(0, t),
                            "UTF-8"), URLDecoder.decode(v.substring(t + 1, v

                    .length()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error(e, e);
                }
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
