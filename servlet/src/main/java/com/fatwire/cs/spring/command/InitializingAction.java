package com.fatwire.cs.spring.command;


/**
 * Similar to InitializingBean. Invoked after the Action is injected by the Controller.
 * 
 * 
 * @author Dolf.Dijkstra
 * @since 24-feb-2007
 */
public interface InitializingAction {
    
    public boolean afterActionPropertiesSet();

}
