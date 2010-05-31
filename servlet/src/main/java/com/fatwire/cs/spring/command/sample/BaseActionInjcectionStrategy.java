package com.fatwire.cs.spring.command.sample;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fatwire.cs.spring.command.Action;
import com.fatwire.cs.spring.command.ActionInjcectionStrategy;
import com.fatwire.cs.spring.command.ActionInjector;

public class BaseActionInjcectionStrategy implements ActionInjcectionStrategy {

    private ActionInjector actionInjector;

    /**
     * order is:
     * session
     * request 
     * params
     * 
     * If multiple value pools have the same property, then the last one is used.
     * @throws Exception 
     * 
     */
    public void inject(Action action, HttpServletRequest request)
            throws Exception {
        if (request == null)
            return;

        final HttpSession session = request.getSession(false);
        this.injectSession(action, session);
        this.injectRequestAttributes(action, request);
        this.injectParameters(action, request);


    }
    protected void injectSession(Action action, HttpSession session) throws Exception{
        if (session != null) {
            final Map<String, Object> sessionMap = new HashMap<String, Object>();
            for (Enumeration enumeration = session.getAttributeNames(); enumeration
                    .hasMoreElements();) {
                String name = (String) enumeration.nextElement();
                sessionMap.put(name, session.getAttribute(name));
            }

            actionInjector.inject(action, sessionMap);
        }
        
    }
    protected void injectRequestAttributes(Action action, HttpServletRequest request) throws Exception{
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        for (Enumeration enumeration = request.getAttributeNames(); enumeration
                .hasMoreElements();) {
            String name = (String) enumeration.nextElement();
            attributeMap.put(name, request.getAttribute(name));
        }

        actionInjector.inject(action, attributeMap);
        
    }

    protected void injectParameters(Action action, HttpServletRequest request) throws Exception{
        actionInjector.inject(action, request.getParameterMap());
        
    }

    /**
     * @param actionInjector the actionInjector to set
     */
    public void setActionInjector(ActionInjector actionInjector) {
        this.actionInjector = actionInjector;
    }
}
