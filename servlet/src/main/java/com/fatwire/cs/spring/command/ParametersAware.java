package com.fatwire.cs.spring.command;

import java.util.Map;

/**
 * 
 * Set's the request parameters
 * 
 * @author Dolf.Dijkstra
 * @since 24-feb-2007
 */
public interface ParametersAware {
    
    public void setParameters(Map parameters);

}
