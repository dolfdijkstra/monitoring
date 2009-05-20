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

    //private String domainName;

    private ObjectName managerName;

    private CacheManagerRunnable runnable;

    private MBeanServer server;

    private int count;

    private Object signal = new Object();


    public int getNumberOfCaches() {
        return count;
    }

    public void shutdown() throws Exception {
        if (runnable != null) {
            runnable.shutDown();
        }

    }

    public void start() throws Exception {
        if (managerName == null) {
            throw new java.lang.IllegalStateException("managerName is not set");

        }
        if (runnable == null) {
            server = ManagementFactory.getPlatformMBeanServer();
            runnable = new CacheManagerRunnable();
            //managerName = new ObjectName(domainName + ":type=CacheManager");
            server.registerMBean(this, managerName);
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
                    synchronized(signal){
                        signal.wait(waitTime);
                    }
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

                    final ObjectName name = new ObjectName(managerName
                            .getCanonicalName()
                            + ",name=" + ObjectName.quote(hashName));
                    //log.debug(name);
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
            synchronized(signal){
                signal.notifyAll();
            }

        }

    }

    /**
     * @return the managerName
     */
    public ObjectName getManagerName() {
        return managerName;
    }

    /**
     * @param managerName the managerName to set
     */
    public void setManagerName(ObjectName managerName) {
        this.managerName = managerName;
    }

}
