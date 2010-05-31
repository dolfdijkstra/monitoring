package com.fatwire.cs.satellite.filters.sessionid.wrappers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final Log log = LogFactory.getLog(getClass());

    private final String targetCookieName;

    final private String sourceCookieName;

    public ResponseWrapper(final HttpServletResponse res, final String sourceCookie,
            final String targetCookie) {
        super(res);
        sourceCookieName = sourceCookie; //SS_X_JSESSIONID
        targetCookieName = targetCookie; //JSESSIONID
    }

    public void addCookie(final Cookie cookie) {
        log.debug("adding Cookie: "+ cookie.getName());
        if (sourceCookieName.equals(cookie.getName())) {
            log
                    .debug("Found " + sourceCookieName
                            + " in response.addCookie().");

            final Cookie targetCookie = new Cookie(targetCookieName, cookie
                    .getValue());
            if (cookie.getPath() != null) {
                targetCookie.setPath(cookie.getPath());
            }
            if (cookie.getDomain() != null) {
                targetCookie.setDomain(cookie.getDomain());
            }
            targetCookie.setMaxAge(cookie.getMaxAge());
            targetCookie.setSecure(cookie.getSecure());
            super.addCookie(targetCookie);
        }
        super.addCookie(cookie);
    }
}
