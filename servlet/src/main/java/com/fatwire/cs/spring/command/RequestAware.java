package com.fatwire.cs.spring.command;

import javax.servlet.http.HttpServletRequest;


/**
 * set's the request object
 * 
 * @author Dolf.Dijkstra
 * @since 24-feb-2007
 */
public interface RequestAware {

    public void setRequest(HttpServletRequest request);
}
