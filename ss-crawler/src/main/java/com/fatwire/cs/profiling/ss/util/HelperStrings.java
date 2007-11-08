package com.fatwire.cs.profiling.ss.util;


public class HelperStrings {
    public static final String SS_CLIENT_INDICATOR = "ft_ss"; // true if client is ss, false/null if not

    public static final String SS_PAGEDATA_REQUEST = "please_send_pagedata_to_ft_ss"; // true / false

    public static final String PAGENAME = "pagename";

    public static final String RENDERMODE = "rendermode";

    public static final String CS_TO_SS_RESPONSE_HEADER_PREFIX = "com.futuretense.contentserver.pagedata.field.";
    public static final String SS_CACHEINFO_HEADER =CS_TO_SS_RESPONSE_HEADER_PREFIX +"sscacheinfocache-disabled";
    public static final String CS_CACHEINFO_HEADER =CS_TO_SS_RESPONSE_HEADER_PREFIX +"cscacheinfocache-disabled";

    public static final String PAGE_CRITERIA_HEADER = CS_TO_SS_RESPONSE_HEADER_PREFIX +"pagecriteria";

    public static final char[] CRLF = "\r\n".toCharArray();
    
    public static final String CONTENTSERVER="ContentServer";
    
    public static final String SSURI_START = "satellitescheme://SSURI";

    public static final String CACHECONTROL = "cachecontrol";
    

    public static final String STATUS_NOTCACHED = "<!--FTCACHE-0-->";
    public static final String STATUS_CACHED = "<!--FTCACHE-1-->";

}
