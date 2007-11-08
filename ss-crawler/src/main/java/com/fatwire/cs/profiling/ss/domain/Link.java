package com.fatwire.cs.profiling.ss.domain;


/**
 * A link we need to crawl or crawled
 * 
 * @author Dolf.Dijkstra
 * @since Nov 1, 2007
 */
public class Link {

    private long id;

    private String url;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
