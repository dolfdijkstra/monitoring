package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter so set Cache-Control: max-age header
 * <pre>
 *     &lt;filter&gt;
 *       &lt;description&gt;Set Cache-Control:max-age header &lt;/description&gt;
 *        &lt;filter-name&gt;MaxAgeFilter&lt;/filter-name&gt;
 *        &lt;filter-class&gt;com.fatwire.gst.web.servlet.profiling.servlet.filter.ExpiresFilter&lt;/filter-class&gt;
 *        &lt;init-param&gt;
 *            &lt;description&gt;Set max-age period, accepts 's','m','h' and 'd' as postfix.&lt;/description&gt;
 *            &lt;param-name&gt;period&lt;/param-name&gt;
 *            &lt;param-value&gt;5d&lt;/param-value&gt;
 *        &lt;/init-param&gt;
 *        &lt;/filter&gt;
 *    &lt;filter-mapping&gt;
 *        &lt;filter-name&gt;MaxAgeFilter&lt;/filter-name&gt;
 *        &lt;url-pattern&gt;*.jpg&lt;/url-pattern&gt;
 *        &lt;dispatcher&gt;REQUEST&lt;/dispatcher&gt;
 *    &lt;/filter-mapping&gt;
 *</pre>
 * 
 * 
 * @author Dolf.Dijkstra
 * @since Dec 18, 2009
 */
public class ExpiresFilter implements Filter {

    private String cc = "max-age=60,public";

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            doFilter((HttpServletRequest) request,
                    (HttpServletResponse) response, chain);
        }

    }

    public void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if ("GET".equalsIgnoreCase(request.getMethod())
                && "HTTP/1.1".equals(request.getProtocol())) {
            response.setHeader("Cache-Control", cc);
        }
        chain.doFilter(request, response);
    }

    /**
     * reads filter configuration
     * 
     * 
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        String p = filterConfig.getInitParameter("period");
        cc = "max-age=" + parsePeriod(p, 60 * 60 * 24) + ",public";//24h by default
    }

    public int parsePeriod(String param, int dephault) {
        int period = dephault;
        if (param != null) {
            String p = param.trim().toLowerCase();
            if (p.endsWith("s")) {
                period = Integer.parseInt(p.substring(0, p.length() - 1));
            } else if (p.endsWith("m")) {
                period = Integer.parseInt(p.substring(0, p.length() - 1)) * 60;
            } else if (p.endsWith("h")) {
                period = Integer.parseInt(p.substring(0, p.length() - 1)) * 60 * 60;
            } else if (p.endsWith("d")) {
                period = Integer.parseInt(p.substring(0, p.length() - 1)) * 60 * 60 * 24;
            }else {
                throw new IllegalArgumentException("'"+ param +"' can not be parsed");
            }
        }
        return period;
    }
}
