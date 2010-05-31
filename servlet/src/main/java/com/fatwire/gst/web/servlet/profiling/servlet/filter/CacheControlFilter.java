package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheControlFilter implements Filter {
    private static Log log = LogFactory.getLog(CacheControlFilter.class);

    private static final String CACHE_CONTROL = "Cache-Control";

    private Map<String, Integer> ttlMap = new HashMap<String, Integer>();

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse
                && request instanceof HttpServletRequest) {
            final int ttl = getCacheControlTTL((HttpServletRequest) request);
            chain.doFilter(request, new HttpServletResponseWrapper(
                    (HttpServletResponse) response) {

                private String cc = null;

                /* (non-Javadoc)
                 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
                 */
                @Override
                public void setHeader(String name, String value) {
                    if (CACHE_CONTROL.equalsIgnoreCase(name)) {
                        cc = value;
                    } else if (cc == null
                            && "Last-Modified".equalsIgnoreCase(name)) {
                        if (ttl > 0) {
                            super.setHeader(CACHE_CONTROL, "max-age=" + ttl);
                        }
                    }
                    super.setHeader(name, value);
                }

            });
        } else {
            chain.doFilter(request, response);
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        //alternative is to read from a config file
        String uri = filterConfig.getInitParameter("uri");
        if (uri == null || uri.length() == 0) {
            log
                    .warn("uri filter parameter is not set, the filter can not configure itself");
        } else {
            URI u = URI.create(uri);
            InputStream in = null;
            try {
                in = u.toURL().openStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String s = null;
                while ((s = reader.readLine()) != null) {
                    String[] parts = s.split("=");
                    ttlMap.put(parts[0], Integer.parseInt(parts[1]));
                    //ttlMap.put("FSII/Article/Full", 300);
                    //ttlMap.put("FSII/Document/Full", 900);

                }

            } catch (MalformedURLException e) {
                ttlMap.clear();
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                ttlMap.clear();
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                ttlMap.clear();
                log.error(e.getMessage(), e);

            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            }

        }

    }

    private int getCacheControlTTL(HttpServletRequest request) {
        //here we are using the pagename as the selector
        //other more advanced implementations are also possible
        if (request.getPathInfo() /*.getParameter("pagename") */!= null) {
            return ttlMap.get(request.getPathInfo() /*.getParameter("pagename")*/);
        }
        return -1;
    }
}
