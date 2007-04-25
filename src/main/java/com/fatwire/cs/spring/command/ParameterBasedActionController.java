package com.fatwire.cs.spring.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.ModelAndView;


public class ParameterBasedActionController extends AbstractActionController {
    public final static String DEFAULT_ACTION_PARAMETER_NAME = "action";

    private String actionParameterName = DEFAULT_ACTION_PARAMETER_NAME;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Action action = getAction(request.getParameter(actionParameterName));
        DataBinder binder = bindAndValidate(request, response,action);
        BindException errors = new BindException(binder.getBindingResult());
        return handle(action, errors);
    }

    /**
     * Template method for request handling, providing a populated and validated instance
     * of the command class, and an Errors object containing binding and validation errors.
     * <p>Call <code>errors.getModel()</code> to populate the ModelAndView model
     * with the command and the Errors instance, under the specified command name,
     * as expected by the "spring:bind" tag.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param command the populated command object
     * @param errors validation errors holder
     * @return a ModelAndView to render, or <code>null</code> if handled directly
     * @see org.springframework.validation.Errors
     * @see org.springframework.validation.BindException#getModel
     */
    protected ModelAndView handle(Action command, BindException errors)
            throws Exception {
        return command.execute();
    }

    /**
     * @return the actionParameterName
     */
    public String getActionParameterName() {
        return actionParameterName;
    }

    /**
     * @param actionParameterName the actionParameterName to set
     */
    public void setActionParameterName(String actionParameterName) {
        this.actionParameterName = actionParameterName;
    }

}
