package com.fatwire.cs.spring.command;

import java.util.Map;


/**
 * sets the session map object
 * This also to indicate the it expects that a session is created before the action is invoked 
 * 
 * @author Dolf.Dijkstra
 * @since 24-feb-2007
 */
public interface SessionAware {
    
    public void setSession(Map session);

}
