package com.fatwire.cs.spring.command.bind;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.fatwire.cs.spring.command.Action;


public interface ActionRequestBinder {

    /**
     * Bind the parameters of the given request to the given command object.
     * @param request current HTTP request
     * @param command the command to bind onto
     * @return the ServletRequestDataBinder instance for additional custom validation
     * @throws Exception in case of invalid state or arguments
     */
    public ServletRequestDataBinder bindAndValidate(HttpServletRequest request,
            Action action, String commandName) throws Exception;

}