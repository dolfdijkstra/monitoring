package com.fatwire.cs.profiling.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResultPage {

    private final String uri;

    private String body;

    private final List<String> links;

    private final List<String> markers;

    /**
     * @param uri
     */
    public ResultPage(final String uri) {
        super();
        this.uri = uri;
        links = new ArrayList<String>();
        markers = new ArrayList<String>();
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
    public List<String> getLinks() {
        return links;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    public void addLink(String uri) {
        this.links.add(uri);

    }

    public void addLinks(Collection<String> uris) {
        this.links.addAll(uris);

    }
    public void addMarker(String uri) {
        this.markers.add(uri);

    }

    public void addMarkers(Collection<String> uris) {
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
    public List<String> getMarkers() {
        return markers;
    }

}
