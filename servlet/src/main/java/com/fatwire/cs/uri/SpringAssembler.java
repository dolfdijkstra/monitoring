package com.fatwire.cs.uri;

/*
 * $Logfile:$ $Revision:$ $Date:$
 *
 * Copyright (c) 2005 FatWire Corporation, All Rights Reserved.
 */

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.QueryAssembler;
import org.apache.commons.logging.Log;

import java.net.URI;
import java.util.*;

/**
 * This assembler generates a URL that is a standard query string URL,
 * unless the URL follows FirstSite II coding practices.  In particular,
 * <ul>
 * <li>pagename must be <code>[sitepfx]Wrapper</code></li>
 * <li>childpagename must be <code>[site]/[sitepfx]Layout</code></li>
 * <li>c must be specified and must be a <code>{@link String}</code></li>
 * <li>cid must be specified and must be a <code>long</code></li>
 * <li>p must be specified and it must be a <code>long</code></li>
 * <li>other params may be specified</li>
 * </ul>
 * The URL will be constructed as follows:
 * <code>...ContentServer/[site]/[sitepfx]/[c]/[cid]/[p]?[other params]</code>
 * unless c is <code>Page</code> and cid=p, in which case the URL will be constructed
 * as follows:  <code>...ContentServer/[site]/[sitepfx]/[cid]?[other params]</code>
 * <p/>
 * If the URL does not comply, it will be constructed using the standard
 * {@link QueryAssembler}.
 *
 * @author Tony Field
 * @since 10-Jul-2005 9:08:13 PM
 */
public class SpringAssembler extends QueryAssembler
{
    public SpringAssembler()  {}

    protected AssemblyContext getAssemblyContext(Definition def)
    {
        SpringAssemblyData data = SpringAssemblyData.valueOf(this, def);
        if (data == null)
        {
            getLog().trace("SpringAssembler aborting in getAssemblyContext");
            return super.getAssemblyContext(def);
        }
        return new SpringAssemblyContext(def, data);
    }

    /**
     * Wrapper over the standard assembly context object.  This version
     * hides the special params from the query string, but makes them available through the
     * SpringData object, which is stored as a context parameter.
     */
    protected static class SpringAssemblyContext extends AssemblyContext
    {
        private static final List<String> embeddedParams = new ArrayList<String>();

        static
        {
            embeddedParams.add("c");
            embeddedParams.add("cid");
            embeddedParams.add("p");
            embeddedParams.add("pagename");
            embeddedParams.add("childpagename");
        }

        protected static final String DATA = "spring assembly context data";
        final Map<String, SpringAssemblyData> ctxParams;

        SpringAssemblyContext(Definition def, SpringAssemblyData data)
        {
            super(def);
            ctxParams = new HashMap<String, SpringAssemblyData>();
            ctxParams.put(DATA, data);
        }

        public Object getContextParameter(Object key)
        {
            return ctxParams.get(key);
        }

        public String getParameter(String name)
        {
            // suppress special params
            if (embeddedParams.contains(name))
                return null;
            return super.getParameter(name);
        }

        public String[] getParameters(String name)
        {
            // suppress special params
            if (embeddedParams.contains(name))
                return null;
            return super.getParameters(name);
        }

        public Collection<?> getParameterNames()
        {
            // suppress special params
            Collection<?> result = new ArrayList();
            result.addAll(super.getParameterNames());
            synchronized (embeddedParams)
            {
                for (Iterator<String> i = embeddedParams.iterator(); i.hasNext();)
                    result.remove(i.next());
            }
            return Collections.unmodifiableCollection(result);
        }
    }

    /**
     * Internal data object representing the "special" variables
     */
    private static class SpringAssemblyData
    {
        final String c, sitepfx, site, cid, p;

        private SpringAssemblyData(String sC, String sSitepfx, String sSite, String sCid, String sP)
        {
            c = sC;
            sitepfx = sSitepfx;
            site = sSite;
            cid = sCid;
            p = sP;
        }

        /**
         * This method is responsible for the heavy lifting in this assembler.  It attempts to create
         * an {@link SpringAssemblyData} object by inspecting the input definition.  If the definition
         * meets the requirements for building a Spring-optimized URL, then the {@link SpringAssemblyData}
         * object is returned.  Otherwise, it returns null.
         * @param assembler
         * @param def
         * @return SpringAssemblyData object or null.
         */
        private static SpringAssemblyData valueOf(SpringAssembler assembler, Definition def)
        {
            Log log = assembler.getLog();
            // Get our required params out first
            String pagename, childpagename, c, cid, p;

            // Figure out our derived params
            String sitepfx, site;

            // pagename: [sitepfx]Wrapper
            pagename = def.getParameter("pagename");
            if (pagename == null || !pagename.endsWith("Wrapper"))
            {
                log.trace("pagename not specified or does not end with \"Wrapper\": " + pagename);
                return null;
            }
            sitepfx = pagename.substring(0, pagename.indexOf("Wrapper"));
            if (sitepfx.length() == 0)
            {
                log.trace("Pagename does not have a site prefix: " + pagename);
                return null;
            }

            // childpagename: [site]/[sitepfx]Tname
            childpagename = def.getParameter("childpagename");
            if (childpagename == null)
            {
                log.trace("childpagename not specified");
                return null;
            }
            int sitesep = childpagename.indexOf("/");
            if (sitesep < 1)
            {
                log.trace("childpagename does not start with '/': " + childpagename);
                return null;
            }
            site = childpagename.substring(0, sitesep);
            String tName = site + "/" + sitepfx + "Layout";
            if (!childpagename.startsWith(tName))
            {
                log.trace("childpagename not equal to '" + tName + "': " + childpagename);
                return null;
            }

            c = def.getParameter("c");
            if (c == null)
            {
                log.trace("c not specified");
                return null;
            }

            cid = def.getParameter("cid");
            if (cid == null)
            {
                log.trace("cid not specified");
                return null;
            }
            try
            {
                Long.valueOf(cid);
            }
            catch (NumberFormatException n)
            {
                log.trace("cid not valid: " + cid, n);
                return null;
            }

            p = def.getParameter("p");
            if (p == null)
            {
                log.trace("p not specified");
                return null;
            }
            try
            {
                Long.valueOf(p);
            }
            catch (NumberFormatException n)
            {
                log.trace("p not valid: " + p, n);
                return null;
            }

            // phew!
            return new SpringAssemblyData(c, sitepfx, site, cid, p);
        }


    }


    /**
     * In Spring urls, many parameters are encoded in the path.  This method is responsible for that encoding.
     * The parameter values have already been computed and stored in the {@link SpringAssemblyData} object.
     * @param ctx
     * @return the path
     */
    public String getPath(AssemblyContext ctx)
    {
        SpringAssemblyData data = (SpringAssemblyData) ctx.getContextParameter(SpringAssemblyContext.DATA);

        // not ours....
        if (data == null)
        {
            getLog().trace("SpringAssembler delegating getPath to super");
            return super.getPath(ctx);
        }

        // it's ours... path is of form
        // ...ContentServer/[site]/[sitepfx]/[cid]
        // or
        // ...ContentServer/[site]/[sitepfx]/[c]/[cid]/[p]
        StringBuilder path = new StringBuilder();
        String superPath=super.getPath(ctx);
        if (superPath != null)
            path.append(superPath);
        
            path.append("/");

        if ("Page".equals(data.c) && data.cid.equals(data.p))
        {
            path.append(data.site).append('/').append(data.sitepfx).append('/').append(data.cid);
        }
        else
        {
            path.append(data.site).append('/').append(data.sitepfx).append('/').append(data.c).append('/').append(data.cid).append('/').append(data.p);
        }

        return path.toString();
    }

    /**
     * This method tries to construct a {@link SpringDisassemblyContext} object from the
     * input URI.  If it fails, then the superclass's object is used and the foreign flag is set.
     * @param uri
     * @return DisassemblyContext object.
     */
    protected DisassemblyContext getDisassemblyContext(URI uri)
    {
        DisassemblyContext result;
        SpringDisassemblyData data = SpringDisassemblyData.valueOf(this, uri.getPath());
        if (data == null)
        {
            getLog().trace("SpringAssembler aborting in getDisassemblyContext");
            result = super.getDisassemblyContext(uri);
            result.setForeign();
        }

        return new SpringDisassemblyContext(uri, data);

    }

    protected static class SpringDisassemblyContext extends DisassemblyContext
    {
        protected static final String DATA = "spring disassembly context data";
        final Map<String, SpringDisassemblyData> ctxParams;

        SpringDisassemblyContext(URI uri, SpringDisassemblyData data)
        {
            super(uri);
            ctxParams = new HashMap<String, SpringDisassemblyData>();
            ctxParams.put(DATA, data);
        }

        public Object getContextParameter(Object key)
        {
            return ctxParams.get(key);
        }
    }

    private static class SpringDisassemblyData
    {
        private final Definition.SatelliteContext satelliteContext;
        private final String site, sitepfx, c, cid, p;

        private SpringDisassemblyData(Definition.SatelliteContext ctx, String site, String sitepfx, String c, String cid, String p)
        {
            this.satelliteContext = ctx;
            this.site = site;
            this.sitepfx = sitepfx;
            this.c = c;
            this.cid = cid;
            this.p = p;
        }

        /**
         * This method is responsible for the heavy lifting in the URL disassembly.  It takes a look at the path
         * of a URI and attempts to figure out whether or not the path corresponds to a URL that was generated by
         * this assembler.  If it was, a {@link SpringDisassemblyData} object is returned.  Otherwise it returns null.
         * @param assembler
         * @param path
         * @return SpringDisassemblyData or null.
         */
        private static SpringDisassemblyData valueOf(SpringAssembler assembler, String path)
        {
            Log log = assembler.getLog();

            Definition.SatelliteContext ctx;

            // Get the app type
            if (path == null)
            {
                log.trace("path not specified");
                return null; // not ours
            }
            else
            {

                String embeddedParams = null;
                // look for ss
                String sspath = assembler.getProperty(PROP_URIBASE_SATELLITE_SERVER, null);
                // a satellite server url willl always start with the satellite base
                if (path.startsWith(sspath))
                {
                    ctx = Definition.SatelliteContext.SATELLITE_SERVER;

                    // but it will always have pathinfo
                    if (path.length() > sspath.length())
                    {
                        embeddedParams = path.substring(sspath.length() + 1);
                    }
                    else
                    {
                        // exact match - not ours.
                        log.trace("no pathinfo present");
                        return null;
                    }
                }
                else
                {
                    ctx = Definition.SatelliteContext.CONTENT_SERVER;

                    // This assembler only supports Content Server URLs, so make sure we have a match
                    String typePath = assembler.getProperty(PROP_PATH_PREFIX + Definition.AppType.CONTENT_SERVER, null);
                    if (path.startsWith(typePath))
                    {
                        // yup, content server all right.... now make sure we have path info
                        if (path.length() > typePath.length())
                        {
                            embeddedParams = path.substring(typePath.length() + 1);
                        }
                        else
                        {
                            log.trace("no pathinfo present");
                            return null;
                        }
                    }
                }

                if (embeddedParams == null)
                {
                    log.trace("no embedded params found");
                    return null;
                }
                // now parse the required data out of the embedded params
                // "/[site]/[sitepfx]/[c]/[cid]/[p]" or
                // "/[site]/[sitepfx]/[cid]"
                String site, sitepfx, c, cid, p;

                StringTokenizer st = new StringTokenizer(embeddedParams, "/");
                int tokens = st.countTokens();
                switch (tokens)
                {
                    case 5:
                        site = st.nextToken();
                        sitepfx = st.nextToken();
                        c = st.nextToken();
                        cid = st.nextToken();
                        p = st.nextToken();
                        break;

                    case 3:
                        site = st.nextToken();
                        sitepfx = st.nextToken();
                        c = "Page";
                        cid = st.nextToken();
                        p = cid;
                        break;

                    default :
                        log.trace("incorrect number of elements in path info: " + embeddedParams);
                        return null;
                }

                // phew!
                return new SpringDisassemblyData(ctx, site, sitepfx, c, cid, p);
            }
        }

    }


    /**
     * This method uses the PathInfo object to get the AppType found
     * on the URI.
     *
     * @param ctx DisassemblyContext object containing the URI, context parameters, and
     *            a flag inidicating whether or not this assembler is capable of disassembling this
     *            URI.
     * @return the app type
     */
    protected Definition.AppType getAppType(DisassemblyContext ctx)
    {
        SpringDisassemblyData data = (SpringDisassemblyData) ctx.getContextParameter(SpringDisassemblyContext.DATA);
        if (data == null)
        {
            getLog().trace("SpringAssembler delegating getAppType to super");
            return super.getAppType(ctx);
        }
        return Definition.AppType.CONTENT_SERVER;
    }

    /**
     * This method uses the PathInfo object to get the Satellitecontext
     *
     * @param ctx DisassemblyContext object containing the URI, context parameters, and
     *            a flag inidicating whether or not this assembler is capable of disassembling this
     *            URI.
     * @return the satellite context
     */
    protected Definition.SatelliteContext getSatelliteContext(DisassemblyContext ctx)
    {
        SpringDisassemblyData data = (SpringDisassemblyData) ctx.getContextParameter(SpringDisassemblyContext.DATA);
        if (data == null)
        {
            getLog().trace("SpringAssembler delegating getSatelliteContext to super");
            return super.getSatelliteContext(ctx);
        }
        return data.satelliteContext;
    }

    /**
     * This method uses the PathInfo object to get the query parameters
     *
     * @param ctx DisassemblyContext object containing the URI, context parameters, and
     *            a flag inidicating whether or not this assembler is capable of disassembling this
     *            URI.
     * @return map of query parameters.  It also includes the embedded parameters that were stored in the
     * {@link SpringDisassemblyContext}.
     */
    protected Map<String, String[]> getQuery(DisassemblyContext ctx)
    {
        SpringDisassemblyData data = (SpringDisassemblyData) ctx.getContextParameter(SpringDisassemblyContext.DATA);
        if (data == null)
        {
            getLog().trace("SpringAssembler delegating getQuery to super");
            return super.getQuery(ctx);
        }

        // get the regular query params
        Map<String, String[]> params = super.getQuery(ctx);

        // now add the embedded ones back
        String[] pagename = {data.sitepfx + "Wrapper"};
        String[] childpagename = {data.site + '/' + data.sitepfx + "Layout"};
        String[] c = {data.c};
        String[] cid = {data.cid};
        String[] p = {data.p};

        params.put("pagename", pagename);
        params.put("childpagename", childpagename);
        params.put("c", c);
        params.put("cid", cid);
        params.put("p", p);
        return params;
    }
}
