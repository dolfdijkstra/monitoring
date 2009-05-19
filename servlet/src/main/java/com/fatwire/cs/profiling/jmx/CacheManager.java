package com.fatwire.cs.profiling.jmx;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import COM.FutureTense.Util.ftTimedHashtable;

public class CacheManager implements CacheManagerMBean {

    private String domainName;

    private CacheManagerRunnable runnable;

    private MBeanServer server;

    public void setDomainName(final String domainName) {
        this.domainName = domainName;

    }

    public void shutdown() throws Exception {
        if (runnable != null) {
            runnable.shutDown();
        }

    }

    public void start() throws Exception {
        if (domainName == null) {
            throw new java.lang.IllegalStateException("domainName is not set");

        }
        if (runnable == null) {
            server = ManagementFactory.getPlatformMBeanServer();
            runnable = new CacheManagerRunnable();
            ObjectName name = new ObjectName(domainName + ":type=CacheManager");
            server.registerMBean(this, name);
            final Thread t = new Thread(runnable, "CacheManager JMX Thread");
            t.setDaemon(true);
            t.start();
        }

    }

    class CacheManagerRunnable implements Runnable {
        private final Log log = LogFactory.getLog(this.getClass());

        private final long waitTime = 5000L;

        private boolean stop = false;

        public void run() {

            while (!stop) {
                try {
                    Thread.sleep(waitTime);
                    registerMBeans();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

        /**
         * registers ftTimedHashtable's RuntimeStats as MBeans in the MBean server
         */
        @SuppressWarnings("unchecked")
        private void registerMBeans() {
            //TODO: deregistering of MBeans when ftTimedHashtable no longer exists
            final Set<String> hashNames = ftTimedHashtable.getAllCacheNames();
            for (final String hashName : hashNames) {
                try {

                    final ObjectName name = new ObjectName(domainName
                            + ":type=runtimecachestats,name="
                            + ObjectName.quote(hashName));
                    log.debug(name);
                    if (!server.isRegistered(name)) {
                        server.registerMBean(new CacheStats(hashName), name);

                    }

                } catch (final MalformedObjectNameException e) {
                    log.error(e.getMessage(), e);
                } catch (final InstanceAlreadyExistsException e) {
                    log.error(e.getMessage(), e);
                } catch (final MBeanRegistrationException e) {
                    log.error(e.getMessage(), e);
                } catch (final NotCompliantMBeanException e) {
                    log.error(e.getMessage(), e);
                }
            }

        }

        synchronized void shutDown() {
            stop = true;

        }

    }
}
