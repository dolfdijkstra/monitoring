package com.fatwire.cs.spring.command.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.fatwire.cs.spring.command.Action;

public class HelloWorldAction implements Action {
    Log log = LogFactory.getLog(this.getClass());

    /**
     * 
     */
    public HelloWorldAction() {
        super();
        log.info("HelloWorldAction():" + this.hashCode());
    }

    private String userName;

    public ModelAndView execute() throws Exception {
        ModelAndView mav = new ModelAndView("HelloWorld");
        mav.addObject("userName", userName);
        return mav;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
