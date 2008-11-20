package com.fatwire.cs.page;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

import com.fatwire.cs.spring.controllers.RequestModel;

public class PageController implements Controller, ServletContextAware,
        ServletConfigAware {

    private ServletContext context;

    private ServletConfig config;

    private PartRenderer<RequestModel> bodyRenderer;

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ModelAndView mav = new ModelAndView(new View() {

            public String getContentType() {
                return "text/html; charset=UTF-8";
            }

            public void render(Map model, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
                if (!response.isCommitted()) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html");
                }

                createPage(response).renderPage(createModel(request));

            }

        });
        return mav;
    }

    HtmlPage<RequestModel> createPage(final HttpServletResponse response)
            throws IOException {
        return new HtmlPage<RequestModel>(bodyRenderer, response.getWriter());
    }

    RequestModel createModel(final HttpServletRequest request) {
        return new RequestModel(request, context, config);
    }

    public void setServletContext(final ServletContext context) {
        this.context = context;

    }

    public void setServletConfig(final ServletConfig config) {
        this.config = config;

    }

    /**
     * @param bodyRenderer the bodyRenderer to set
     */
    public void setBodyRenderer(
            final PartRenderer<RequestModel> bodyRenderer) {
        this.bodyRenderer = bodyRenderer;
    }
}
