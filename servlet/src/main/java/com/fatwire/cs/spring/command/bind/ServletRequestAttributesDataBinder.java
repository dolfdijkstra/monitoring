package com.fatwire.cs.spring.command.bind;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;

public class ServletRequestAttributesDataBinder extends WebDataBinder {

    /**
     * @param target
     * @param objectName
     */
    public ServletRequestAttributesDataBinder(final Object target, final String objectName) {
        super(target, objectName);
    }

    /**
     * @param target
     */
    public ServletRequestAttributesDataBinder(final Object target) {
        super(target);
    }

    public void bind(final HttpServletRequest request) {
        final MutablePropertyValues mpvs = new MutablePropertyValues(
                attributesToMap(request));
        doBind(mpvs);
    }

    Map attributesToMap(final HttpServletRequest request) {
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        for (final Enumeration enumeration = request.getAttributeNames(); enumeration
                .hasMoreElements();) {
            final String name = (String) enumeration.nextElement();
            attributeMap.put(name, request.getAttribute(name));
        }

        return attributeMap;
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
