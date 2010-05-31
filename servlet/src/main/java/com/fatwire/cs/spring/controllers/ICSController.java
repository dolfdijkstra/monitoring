package com.fatwire.cs.spring.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import COM.FutureTense.CS.Factory;
import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IRequest;
import COM.FutureTense.Servlet.RequestFactory;

public class ICSController implements Controller, ServletContextAware {
    private String propPath;

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        final IRequest iRequest = RequestFactory.getRequestFactory(null).getRequest(
                null, request, response);
        final ICS ics = Factory.newCS(iRequest, propPath);
        final FTValList valList = new FTValList();

        valList.put("NAME", "assetName");
        valList.put("TYPE", iRequest.getAttribute("c"));
        valList.put("OBJECTID", iRequest.getAttribute("cid"));
        valList.put("OPTION", "readonly_complete");
        final String ret = ics.runTag("ASSET.LOAD", valList);
        if (ics.GetErrno() != 0) {
            return new ModelAndView("errorpage");
        } else {
            final ModelAndView mav = new ModelAndView(ics.GetVar("pagename"));
            mav.addObject("asset", ics.GetObj("assetName"));
            return mav;
        }
        
    }

    public void setServletContext(final ServletContext context) {
        propPath = context.getInitParameter("inipath");

    }

}
