package com.fatwire.cs.spring.command;

import org.springframework.web.servlet.ModelAndView;

public interface Action {
    
    public ModelAndView execute() throws Exception;

}
