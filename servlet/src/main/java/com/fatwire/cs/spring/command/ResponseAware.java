package com.fatwire.cs.spring.command;

import javax.servlet.http.HttpServletResponse;

public interface ResponseAware {
    
    
    public void setResponse(HttpServletResponse response);

}
