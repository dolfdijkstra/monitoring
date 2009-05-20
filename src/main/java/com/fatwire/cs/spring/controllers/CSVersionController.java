package com.fatwire.cs.spring.controllers;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fatwire.cs.profiling.version.ProductInfo;
import com.fatwire.cs.profiling.version.ProductInfoFactory;

public class CSVersionController implements Controller, InitializingBean {
    final Log log = LogFactory.getLog(this.getClass());

    private final static String PRODUCT_NOT_FOUND = "<span class=\"notfound\">JAR NOT INSTALLED/LOADED</span>";

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final PrintWriter out = response.getWriter();
        out.println("<html><head><title>Product Versions</title>");
        out.println("<style type=\"text/css\">");
        out.println("<!--");
        out.println("table.jvm { font-size: 10pt;");
        out.println(" }");
        out.println("table.products { font-size: 10pt;");
        out.println(" }");
        out.println("h2 { font-size: 16pt;");
        out.println(" }");
        out.println(".notfound { color: red;");
        out.println(" }");

        out.println("-->");

        out.println("</head><body>");

        //  Print the product info. table
        out.print("<h2><center>Product Information</center></h2>");

        out.print("<table class=\"products\">");

        out.print("<tr>");

        out.print("<th>Product Name</th>");

        out.print("<th>Jar File</th>");

        out.print("<th>Class Name</th>");

        out.print("<th>Version</th>");

        out.print("<tr>");

        for (final ProductInfo product : productInfo) {

            out.print("<tr>");

            out.print("<td>");
            out.print(product.getProductName() == null ? "&nbsp;" : product
                    .getProductName());
            out.print("</td>");

            out.print("<td>");
            out.print(product.getProductJar());
            out.print("</td>");

            out.print("<td>");
            out.print(product.getProductVersionInfoClass());
            out.print("</td>");

            out.print("<td>");
            out
                    .print((product.getVersion() == null ? CSVersionController.PRODUCT_NOT_FOUND
                            : product.getVersion()));
            out.print("</td>");

            out.println("</tr>");
        }

        out.print("</table>");
        out.print("</body></html>");
        return null;
    }

    //*********************************************************************************
    //
    //  PRODUCT INFORMATION
    //
    //*********************************************************************************
    final List<ProductInfo> productInfo = new LinkedList<ProductInfo>();;

    public CSVersionController() {
    }

    public void afterPropertiesSet() throws Exception {
        productInfo.clear();
        ProductInfoFactory f = new ProductInfoFactory();
        productInfo.addAll(f.createList());
    }

}