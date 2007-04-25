package com.fatwire.cs.satellite.filters.sessionid;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.satellite.filters.sessionid.wrappers.RequestWrapper;
import com.fatwire.cs.satellite.filters.sessionid.wrappers.ResponseWrapper;

public class SatelliteServerSessionFilter implements Filter {
    private Log log = LogFactory.getLog(this.getClass());

    private final static String INIT_BACK_END_COOKIE_NAME = "backendAppserverCookieName";

    private final static String INIT_COOKIE_PREFIX = "cookiePrefix";

    private String backendAppserverCookieName;

    private String cookiePrefix;

    private String filterName;

    public void init(FilterConfig fc) throws ServletException {
        log.info("init filter " + fc.getFilterName());
        this.filterName = fc.getFilterName();
        backendAppserverCookieName = fc
                .getInitParameter(INIT_BACK_END_COOKIE_NAME); // CSSESSIONID
        cookiePrefix = fc.getInitParameter(INIT_COOKIE_PREFIX); // SS_X_
        if (backendAppserverCookieName == null || cookiePrefix == null) {
            throw new ServletException(
                    "Configuration is not complete. Please make sure you have configured both "
                            + INIT_BACK_END_COOKIE_NAME + " and "
                            + INIT_COOKIE_PREFIX + ".");
        }
    }

    public void destroy() {
        log.info("destoy filter " + filterName);

    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws ServletException, IOException {
        log.trace("doFilter(ServletRequest,ServletResponse)");
        if (req instanceof HttpServletRequest
                && res instanceof HttpServletResponse) {
            doFilter((HttpServletRequest) req, (HttpServletResponse) res, chain);
        } else {
            chain.doFilter(req, res);
        }

    }

    public void doFilter(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {
        if (log.isTraceEnabled()) {
            log.trace("doFilter(HttpServletRequest,HttpServletResponse) "
                    + req.getRequestURI());
        }
        HttpServletRequest requestWrapper = new RequestWrapper(req,
                backendAppserverCookieName, cookiePrefix
                        + backendAppserverCookieName);
        HttpServletResponse responseWrapper = new ResponseWrapper(res,
                cookiePrefix + backendAppserverCookieName,
                backendAppserverCookieName);
        chain.doFilter(requestWrapper, responseWrapper);

    }

}
