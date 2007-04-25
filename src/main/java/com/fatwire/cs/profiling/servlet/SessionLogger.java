package com.fatwire.cs.profiling.servlet;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionLogger implements HttpSessionAttributeListener,
        HttpSessionBindingListener {
    private final Log log = LogFactory.getLog(this.getClass());

    public SessionLogger() {
        log.debug("SessionLogger");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(final HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log
                    .debug("sessionAttributeAdded:"
                            + event.getSession().getId() + ":"
                            + event.getName() + "="
                            + event.getValue().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(final HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("sessionAttributeRemoved:" + event.getSession().getId()
                    + ":" + event.getName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(final HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log
                    .debug("sessionAttributeReplaced:"
                            + event.getSession().getId() + ":"
                            + event.getName() + "="
                            + event.getValue().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(final HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("sessionValueBound:" + event.getSession().getId() + ":"
                    + event.getName() + "=" + event.getValue().toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(final HttpSessionBindingEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("sessionValueUnbound:" + event.getSession().getId() + ":"
                    + event.getName());
        }
    }
}
