package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.Header;

import com.fatwire.cs.profiling.ss.util.UriUtil;

public class ResultPage {

    private final String uri;

    private String body;

    private Header[] responseHeaders;

    private final List<SSUri> links;

    private final List<SSUri> markers;

    private long readTime = -1;

    private String pageName;

    /**
     * @param uri
     */
    public ResultPage(final String uri) {
        super();
        this.uri = uri;
        pageName = UriUtil.extractPageName(uri);
        links = new ArrayList<SSUri>();
        markers = new ArrayList<SSUri>();
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the links
     */
    public List<SSUri> getLinks() {
        return links;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    public void addLink(SSUri uri) {
        this.links.add(uri);

    }

    public void addLinks(Collection<SSUri> uris) {
        this.links.addAll(uris);

    }

    public void addMarker(SSUri uri) {
        this.markers.add(uri);

    }

    public void addMarkers(Collection<SSUri> uris) {
        this.markers.addAll(uris);

    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ResultPage other = (ResultPage) obj;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }

    /**
     * @return the markers
     */
    public List<SSUri> getMarkers() {
        return markers;
    }

    /**
     * @return the readTime
     */
    public long getReadTime() {
        return readTime;
    }

    /**
     * @param readTime the readTime to set
     */
    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    /**
     * @return the responseHeaders
     */
    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * @param responseHeaders the responseHeaders to set
     */
    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    /**
     * @return the pageName
     */
    public String getPageName() {
        return pageName;
    }

}
