package com.fatwire.cs.spring.command.sample;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.fatwire.cs.spring.command.Action;
import com.fatwire.cs.spring.command.ResponseAware;

public class NullAction implements Action, ResponseAware {
    private HttpServletResponse response;

    public ModelAndView execute() throws Exception {
        response.sendError(404);
        return null;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;

    }

}
