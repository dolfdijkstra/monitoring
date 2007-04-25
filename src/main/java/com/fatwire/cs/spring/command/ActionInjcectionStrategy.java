package com.fatwire.cs.spring.command;

import javax.servlet.http.HttpServletRequest;

public interface ActionInjcectionStrategy {
    
    public void inject(Action action, HttpServletRequest request) throws Exception;

}
