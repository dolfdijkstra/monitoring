import java.io.IOException;
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

public class CacheControlFilter implements Filter {

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

                /* (non-Javadoc)
                 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
                 */
                @Override
                public void setHeader(String name, String value) {
                    if ("Last-Modified".equalsIgnoreCase(name)) {
                        if (ttl > 0) {
                            super.setHeader("Cache-Control", "max-age=" + ttl);
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
        ttlMap.put("FSII/Article/Full", 300);
        ttlMap.put("FSII/Document/Full", 900);

    }

    private int getCacheControlTTL(HttpServletRequest request) {
        //here we are using the pagename as the selector
        //other more advanced implementations are also possible
        if (request.getParameter("pagename") != null) {
            return ttlMap.get(request.getParameter("pagename"));
        }
        return -1;
    }
}
