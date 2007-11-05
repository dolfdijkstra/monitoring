package com.fatwire.cs.profiling.ss.domain;

import java.util.Collection;

public class Pagelet {

    private long id;

    private String params;

    private Collection<Pagelet> nestedPagelets;

    private Collection<Link> listedLinks;

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
     * @return the listedLinks
     */
    public Collection<Link> getListedLinks() {
        return listedLinks;
    }

    /**
     * @param listedLinks the listedLinks to set
     */
    public void setListedLinks(Collection<Link> listedLinks) {
        this.listedLinks = listedLinks;
    }

    /**
     * @return the nestedPagelets
     */
    public Collection<Pagelet> getNestedPagelets() {
        return nestedPagelets;
    }

    /**
     * @param nestedPagelets the nestedPagelets to set
     */
    public void setNestedPagelets(Collection<Pagelet> nestedPagelets) {
        this.nestedPagelets = nestedPagelets;
    }

    /**
     * @return the params
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String params) {
        this.params = params;
    }
}
