package com.fatwire.gst.web.servlet.profiling.version;

import java.util.LinkedList;
import java.util.List;

public class ProductInfoFactory {
    private final List<ProductInfo> productInfo = new LinkedList<ProductInfo>();;

    public List<ProductInfo> createList() {

        if (!productInfo.isEmpty())
            return productInfo;

        addProductInfo("Content Server", "cs.jar",
                "COM.FutureTense.Util.FBuild");
        addProductInfo("Content Server", "cs-core.jar",
                "com.fatwire.gst.web.servlet.core.util.FBuild");
        addProductInfo("Content Server", "sseed.jar",
                "com.fatwire.sseed.util.FBuild");
        addProductInfo("Content Server", "FTLDAP.jar",
                "COM.fatwire.ftldap.util.FBuild");
        addProductInfo("Content Server", "framework.jar",
                "com.openmarket.framework.util.FBuild");
        addProductInfo("Content Server", "ftcsntsecurity.jar",
                "com.fatwire.ftcsntsecurity.util.FBuild");
        addProductInfo("Content Server", "batch.jar",
                "com.fatwire.batch.util.FBuild");
        addProductInfo("Content Server", "ics.jar",
                "com.fatwire.ics.util.FBuild");
        addProductInfo("Content Server", "directory.jar",
                "com.fatwire.directory.util.FBuild");
        addProductInfo("Content Server", "logging.jar",
                "com.fatwire.logging.util.FBuild");
        addProductInfo("Content Server", "transformer.jar",
                "com.fatwire.transformer.util.FBuild");
        addProductInfo("CS-Direct", "xcelerate.jar",
                "com.openmarket.xcelerate.util.FBuild");
        addProductInfo("CS-Direct", "assetmaker.jar",
                "com.openmarket.assetmaker.util.FBuild");
        addProductInfo("CS-Direct", "basic.jar",
                "com.openmarket.basic.util.FBuild");
        addProductInfo("CS-Direct", "sampleasset.jar",
                "com.openmarket.sampleasset.util.FBuild");
        addProductInfo("CS-Direct", "gator.jar",
                "com.openmarket.gator.util.FBuild");
        addProductInfo("CS-Direct", "gatorbulk.jar",
                "com.openmarket.gatorbulk.util.FBuild");
        addProductInfo("CS-Direct", "visitor.jar",
                "com.openmarket.visitor.util.FBuild");
        addProductInfo("CS-Direct", "assetframework.jar",
                "com.openmarket.assetframework.util.FBuild");
        addProductInfo("CS-Direct", "cscommerce.jar",
                "com.openmarket.cscommerce.util.FBuild");
        addProductInfo("Engage", "rules.jar",
                "com.openmarket.rules.util.FBuild");
        addProductInfo("Engage", "catalog.jar",
                "com.openmarket.catalog.util.FBuild");
        addProductInfo("Analysis Connector", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        addProductInfo("Database Loader", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        addProductInfo("Queue", "commercedata.jar",
                "com.openmarket.commercedata.util.FBuild");
        //        addProductInfo("XML Exchange", "xmles.jar",
        //                "com.openmarket.ic.webcomm.util.FBuild");
        //        addProductInfo("icutilities.jar", "com.openmarket.ic.util.FBuild");
        addProductInfo("WebLogic jsp manager", "wl6special.jar",
                "com.divine.wl6special.util.Build");
        //        addProductInfo("verityse.jar",
        //                "COM.FutureTense.Search.Verity.Util.FBuildVeritySE");
        addProductInfo("Satellite Server", "sserve.jar",
                "com.fatwire.sserve.util.FBuild");
        return productInfo;
    }


    private void addProductInfo(String s, String s2, String s3) {
        try {
            ProductInfo p = new ProductInfo(s, s2, s3);
            this.productInfo.add(p);
        } catch (Exception e) {
            //ignore
        }
    }

}
