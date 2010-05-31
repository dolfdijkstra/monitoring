package com.fatwire.cs.satellite.filters.sessionid.wrappers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestWrapper extends HttpServletRequestWrapper {
    private Log log = LogFactory.getLog(this.getClass());

    private final String targetCookieName;

    private final String sourceCookieName;

    private final static String HOST_SS = "$$Host_SS";

    public RequestWrapper(HttpServletRequest req, String sourceCookie,
            String targetCookie) {
        super(req);
        this.sourceCookieName = sourceCookie; //JSESSIONID
        this.targetCookieName = targetCookie; //SS_X_JESSIONID
        saveToSession();
    }

    private void saveToSession() {
        Cookie[] cookies = super.getCookies();
        HttpSession ses = super.getSession(true);
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (sourceCookieName.equals(cookies[i].getName())) {
                    log.debug("Found " + sourceCookieName + ". Saving to "
                            + HOST_SS);
                    String value = cookies[i].getValue();
                    String name = cookies[i].getName();
                    ses.setAttribute(HOST_SS, value);
                    String value2 = new StringBuffer(100).append("expires=")
                            .append(cookies[i].getMaxAge()).append("&name=")
                            .append(name).append("&value=").append(value)
                            .append("&uriHelper=%2F").toString();
                    ses.setAttribute(name, value2);
                    break;
                }
            }
        }
    }

    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies == null)
            return null;
        Cookie[] cookies_new = new Cookie[cookies.length + 1];
        boolean found = false;
        int pos = 0;
        for (int i = 0; i < cookies.length; i++) {
            if (sourceCookieName.equals(cookies[i].getName())) {
                log.debug("Adding " + targetCookieName
                        + " in request.getCookies().");

                found = true;
                cookies_new[pos++] = new Cookie(targetCookieName, cookies[i]
                        .getValue());
            }
            cookies_new[pos++] = cookies[i];
        }

        if (found)
            return cookies_new;
        else
            return cookies;

    }

}
