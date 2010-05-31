package com.fatwire.cs.profiling.version;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.jmx.UnregisterMBeansCommand;

public class VersionListener implements ServletContextListener {
    private final Log log = LogFactory.getLog(this.getClass());

    private final List<ProductInfo> productInfo = new LinkedList<ProductInfo>();

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            UnregisterMBeansCommand.unregister("com.fatwire.cs:type=Version,*");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    public void contextInitialized(ServletContextEvent sce) {

        productInfo.clear();
        ProductInfoFactory f = new ProductInfoFactory();
        productInfo.addAll(f.createList());
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        for (ProductInfo info : productInfo) {
            try {
                server.registerMBean(info, new ObjectName(
                        "com.fatwire.cs:type=Version,product="
                                + info.getProductName() + ",jar="
                                + info.getProductJar()));
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }

    }

}
