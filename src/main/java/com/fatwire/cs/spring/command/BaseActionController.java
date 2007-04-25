package com.fatwire.cs.spring.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class BaseActionController implements Controller, InitializingBean {
    private ActionFactory actionFactory;

    private ActionInjcectionStrategy actionInjcectionStrategy;

    private String paramKey;

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String key = request.getParameter(paramKey);
        Action action = actionFactory.createAction(key);
        if (action == null)
            throw new ServletException("no action found");
        if (action instanceof ParametersAware) {
            ((ParametersAware) action).setParameters(request.getParameterMap());
        }
        //TODO other aware interfaces
        actionInjcectionStrategy.inject(action, request);
        if (action instanceof InitializingAction) {
            if (!((InitializingAction) action).afterActionPropertiesSet()) {
                throw new ServletException("Action not properly initialized.");
            }
        }
        return action.execute();
    }

    public void afterPropertiesSet() throws Exception {
        if (paramKey == null) {
            paramKey = "action";
        }
        if (actionFactory == null || actionInjcectionStrategy == null) {
            throw new IllegalStateException(
                    "actionFactory or actionInjectionStrategy are not set.");
        }

    }

}
