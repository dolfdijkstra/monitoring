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

import com.fatwire.cs.core.util.BuildBase;

public class CSVersionController implements Controller,InitializingBean {
    private final Log log = LogFactory.getLog(this.getClass());

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
                    .print((product.getProductVersion() == null ? CSVersionController.PRODUCT_NOT_FOUND
                            : product.getProductVersion()));
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
    private final List<ProductInfo> productInfo = new LinkedList<ProductInfo>();;

    public CSVersionController() {
    }

    private class ProductInfo {
        private String productName;

        private String productJar;

        private String productVersionInfoClass;

        private String productVersion;

        /**
         * @param productName
         * @param productJar
         * @param productVersionInfoClass
         */
        public ProductInfo(final String productName, final String productJar,
                final String productVersionInfoClass) {
            super();
            this.productName = productName;
            this.productJar = productJar;
            this.productVersionInfoClass = productVersionInfoClass;
            collectVersion();
        }

        /**
         * @param productJar
         * @param productVersionInfoClass
         */
        public ProductInfo(final String productJar,
                final String productVersionInfoClass) {
            this("", productJar, productVersionInfoClass);
        }

        private void collectVersion() {

            String version = null;
            try {
                final Class<?> clazz = Class
                        .forName(getProductVersionInfoClass());
                if (BuildBase.class.isAssignableFrom(clazz)) {
                    final Object o = clazz.newInstance();
                    if (o instanceof BuildBase) {
                        final BuildBase bb = (BuildBase) o;
                        version = bb.version();
                    }
                }else {
                    log.warn(clazz +" is not a buildbase");
                }
                productVersion = version;
                productInfo.add(this);
            } catch (final Exception e) {
                log.warn(e.getMessage());
            }
        }

        /**
         * @return the productJar
         */
        public String getProductJar() {
            return productJar;
        }

        /**
         * @return the productName
         */
        public String getProductName() {
            return productName;
        }

        /**
         * @return the productVersion
         */
        public String getProductVersion() {
            return productVersion;
        }

        /**
         * @return the productVersionInfoClass
         */
        public String getProductVersionInfoClass() {
            return productVersionInfoClass;
        }

    }

    public void afterPropertiesSet() throws Exception {
        productInfo.clear();

        new ProductInfo("Content Server", "cs.jar",
                "COM.FutureTense.Util.FBuild");
        new ProductInfo("cs-core.jar", "com.fatwire.cs.core.util.FBuild");
        new ProductInfo("sseed.jar", "com.fatwire.sseed.util.FBuild");
        new ProductInfo("FTLDAP.jar", "COM.fatwire.ftldap.util.FBuild");
        new ProductInfo("framework.jar", "com.openmarket.framework.util.FBuild");
        new ProductInfo("ftcsntsecurity.jar",
                "com.fatwire.ftcsntsecurity.util.FBuild");
        new ProductInfo("batch.jar", "com.fatwire.batch.util.FBuild");
        new ProductInfo("ics.jar", "com.fatwire.ics.util.FBuild");
        new ProductInfo("directory.jar", "com.fatwire.directory.util.FBuild");
        new ProductInfo("logging.jar", "com.fatwire.logging.util.FBuild");
        new ProductInfo("transformer.jar",
                "com.fatwire.transformer.util.FBuild");
        new ProductInfo("CS-Direct", "xcelerate.jar",
                "com.openmarket.xcelerate.util.FBuild");
        new ProductInfo("assetmaker.jar",
                "com.openmarket.assetmaker.util.FBuild");
        new ProductInfo("basic.jar", "com.openmarket.basic.util.FBuild");
        new ProductInfo("sampleasset.jar",
                "com.openmarket.sampleasset.util.FBuild");
        new ProductInfo("gator.jar", "com.openmarket.gator.util.FBuild");
        new ProductInfo("gatorbulk.jar", "com.openmarket.gatorbulk.util.FBuild");
        new ProductInfo("visitor.jar", "com.openmarket.visitor.util.FBuild");
        new ProductInfo("assetframework.jar",
                "com.openmarket.assetframework.util.FBuild");
        new ProductInfo("basic.jar", "com.openmarket.basic.util.FBuild");
        new ProductInfo("cscommerce.jar",
                "com.openmarket.cscommerce.util.FBuild");
        new ProductInfo("Engage", "rules.jar",
                "com.openmarket.rules.util.FBuild");
        new ProductInfo("catalog.jar", "com.openmarket.catalog.util.FBuild");
        new ProductInfo("Analysis Connector", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        new ProductInfo("Database Loader", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        new ProductInfo("Queue", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        new ProductInfo("XML Exchange", "xmles.jar",
                "com.openmarket.ic.webcomm.util.FBuild");
        new ProductInfo("icutilities.jar", "com.openmarket.ic.util.FBuild");
        new ProductInfo("wl6special.jar", "com.divine.wl6special.util.Build");
        new ProductInfo("verityse.jar",
                "COM.FutureTense.Search.Verity.Util.FBuildVeritySE");
        new ProductInfo("Satellite Server", "sserve.jar",
                "com.fatwire.sserve.util.FBuild");
        
    }

}