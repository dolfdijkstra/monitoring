package com.fatwire.cs.spring.controllers;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class Log4jController implements Controller {

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final String logName = request.getParameter("log");
        if (null != logName) {
            final Logger log = ("".equals(logName) ? Logger.getRootLogger() : Logger
                    .getLogger(logName));
            log.setLevel(Level.toLevel(request.getParameter("level"),
                    Level.INFO));
        }
        final ModelAndView mav = new ModelAndView("log4j");

        mav.addObject("rootLogger", Logger.getRootLogger());
        mav.addObject("loggerRepository", Logger.getRootLogger()
                .getLoggerRepository());
        final Set<String> loggerNames = new TreeSet<String>();
        for (final Enumeration enumeration = Logger.getRootLogger()
                .getLoggerRepository().getCurrentLoggers(); enumeration
                .hasMoreElements();) {
            final Logger logger = (Logger) enumeration.nextElement();
            loggerNames.add(logger.getName());
        }
        final List<Logger> loggers = new LinkedList<Logger>();
        for (final Iterator itor=loggerNames.iterator();itor.hasNext(); ){
            loggers.add(Logger.getLogger((String)itor.next()));
        }
        mav.addObject("loggers", loggers);
        return mav;

    }
}
