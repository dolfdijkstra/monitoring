package com.fatwire.cs.spring.command.bind;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;

public class ServletSessionDataBinder extends WebDataBinder {

    /**
     * @param target
     * @param objectName
     */
    public ServletSessionDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    /**
     * @param target
     */
    public ServletSessionDataBinder(Object target) {
        super(target);
    }

    public void bind(HttpSession session) {
        MutablePropertyValues mpvs = new MutablePropertyValues(sessionToMap(session));
        doBind(mpvs);
    }

    Map sessionToMap(HttpSession session) {
        final Map<String, Object> sessionMap = new HashMap<String, Object>();
        for (Enumeration enumeration = session.getAttributeNames(); enumeration
                .hasMoreElements();) {
            String name = (String) enumeration.nextElement();
            sessionMap.put(name, session.getAttribute(name));
        }
        return sessionMap;
    }

    /**
     * Treats errors as fatal. Use this method only if
     * it's an error if the input isn't valid.
     * This might be appropriate if all input is from dropdowns, for example.
     * @throws ServletRequestBindingException subclass of ServletException on any binding problem
     */
    public void closeNoCatch() throws ServletException {
        if (getBindingResult().hasErrors()) {
            throw new ServletRequestBindingException(
                    "Errors binding onto object '"
                            + getBindingResult().getObjectName() + "'",
                    new BindException(getBindingResult()));
        }
    }

}
