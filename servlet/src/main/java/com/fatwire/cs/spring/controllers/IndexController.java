package com.fatwire.cs.spring.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class IndexController implements Controller {

    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView("index");    }

}
