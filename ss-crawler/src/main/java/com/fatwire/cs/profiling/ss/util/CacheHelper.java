package com.fatwire.cs.profiling.ss.util;

import org.apache.commons.httpclient.Header;

public class CacheHelper {
    //com.futuretense.contentserver.pagedata.field.sscacheinfocache-disabled: false
    //com.futuretense.contentserver.pagedata.field.cscacheinfocache-disabled: false
    public static final String SS_CACHE_INFO = HelperStrings.CS_TO_SS_RESPONSE_HEADER_PREFIX
            + "sscacheinfostring";

    public static final String CS_CACHE_INFO = HelperStrings.CS_TO_SS_RESPONSE_HEADER_PREFIX
            + "cscacheinfostring";

    //com.futuretense.contentserver.pagedata.field.sscacheinfostring: true
    //com.futuretense.contentserver.pagedata.field.cscacheinfostring: true

    public static boolean shouldCache(Header[] headers) {
        int i = 0;
        int j = 0;
        for (Header h : headers) {
            if (SS_CACHE_INFO.equals(h.getName())
                    || CS_CACHE_INFO.equals(h.getName())) {
                j++;
                if ("false".equals(h.getValue())) {
                    i++;
                }
            }
        }
        return j == 2 && i == 0;
    }

}
