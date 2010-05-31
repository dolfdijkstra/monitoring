/**
 * 
 */
package com.fatwire.gst.web.servlet.profiling.version;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.core.util.BuildBase;

public class ProductInfo implements ProductInfoMBean {
    final static Log log = LogFactory.getLog(ProductInfo.class);

    private String productName;

    private String productJar;

    private String productVersionInfoClass;

    private String productVersion;
    
    private String shortVersion;

    /**
     * @param productName
     * @param productJar
     * @param productVersionInfoClass
     * @throws Exception 
     */
    public ProductInfo(final String productName, final String productJar,
            final String productVersionInfoClass) throws Exception {
        super();
        this.productName = productName;
        this.productJar = productJar;
        this.productVersionInfoClass = productVersionInfoClass;
        collectVersion();
    }

    private void collectVersion() throws Exception {

        String version = null;
        final Class<?> clazz = Class.forName(getProductVersionInfoClass());
        if (BuildBase.class.isAssignableFrom(clazz)) {
            final Object o = clazz.newInstance();
            if (o instanceof BuildBase) {
                final BuildBase bb = (BuildBase) o;
                version = bb.version();
                String v = bb.version();
                String[] x= v.split("\n");
                if (x.length >1){
                    shortVersion = x[x.length-1];
                    version = v.replace('\n', ' ');
                }else {
                    version=v;
                }
            }
        } else {
            log.warn(clazz + " is not a buildbase");
        }
        productVersion = version;
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
     * @return the productVersionInfoClass
     */
    public String getProductVersionInfoClass() {
        return productVersionInfoClass;
    }

    public String getProductVersionDescription() {
        return productVersion;
    }

    public String getVersion() {
        return shortVersion;
    }

}