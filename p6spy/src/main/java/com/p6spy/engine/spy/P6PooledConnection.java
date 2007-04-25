/*
 *
 * ====================================================================
 *
 * The P6Spy Software License, Version 1.1
 *
 * This license is derived and fully compatible with the Apache Software
 * license, see http://www.apache.org/LICENSE.txt
 *
 * Copyright (c) 2001-2002 Andy Martin, Ph.D. and Jeff Goke
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement:
 * "The original concept and code base for P6Spy was conceived
 * and developed by Andy Martin, Ph.D. who generously contribued
 * the first complete release to the public under this license.
 * This product was due to the pioneering work of Andy
 * that began in December of 1995 developing applications that could
 * seamlessly be deployed with minimal effort but with dramatic results.
 * This code is maintained and extended by Jeff Goke and with the ideas
 * and contributions of other P6Spy contributors.
 * (http://www.p6spy.com)"
 * Alternately, this acknowlegement may appear in the software itself,
 * if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "P6Spy", "Jeff Goke", and "Andy Martin" must not be used
 * to endorse or promote products derived from this software without
 * prior written permission. For written permission, please contact
 * license@p6spy.com.
 *
 * 5. Products derived from this software may not be called "P6Spy"
 * nor may "P6Spy" appear in their names without prior written
 * permission of Jeff Goke and Andy Martin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

/**
 *
 * Support for PooledConnection interface 
 *
 * $Log: P6PooledConnection.java,v $
 * Revision 1.2  2003/06/03 19:20:25  cheechq
 * removed unused imports
 *
 * Revision 1.1  2003/01/30 23:35:22  dlukeparker
 *
 *
 * Added support for the javax.sql operations for pooled connections. This
 * is required for support of WebSphere.
 *
 * Finished implementation of com/p6spy/engine/spy/P6DataSource.java
 *
 * Added com/p6spy/engine/spy/P6ConnectionPoolDataSource.java
 * Added com/p6spy/engine/spy/P6DataSourceFactory.java
 * Added com/p6spy/engine/spy/P6PooledConnection.java
 * Added com/p6spy/engine/spy/P6ProxyConnection.java
 *
 * Made changes in spy.properties and com/p6spy/engine/common/P6SpyOptions.java
 * to enable datasource name, driver and properties setting. Also added support
 * for specifying the JNDI context for finding the real datasource.
 *
 *
 */

package com.p6spy.engine.spy;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.PooledConnection;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.sql.*;

/***
 * This class is used to manage pooled connections. Instances are 
 * created by the P6ConnectionPoolDataSource factory class, and 
 * have a physical Connection. Instances of this class are usable
 * for as long as the physical connections is open. Calls to 
 * getConnection on this class produce instances of the P6ProxyConnection
 * class. 
 *
 * @see javax.sql.PooledConnection
 * @see javax.sql.ConnectionPoolDataSource
 * @see com.p6spy.engine.spy.P6ConnectionPoolDataSource
 * @see com.p6spy.engine.spy.P6ProxyConnection
 *
 */

public class P6PooledConnection implements PooledConnection {
    
    // a Hashtable is a cheap way to get a set with no duplicates
    private Hashtable eventTargets;
    protected Connection proxyConnection;
    protected Connection realConnection;

    public final static int EVENT_TYPE_CLOSE = 1;
    public final static int EVENT_TYPE_ERROR = 2;

    /** 
     *
     * P6ConnectionPoolDataSource creates a realConnection (a P6 wrapper
     * for the realDriver Connection) and then constructs this to contain it.
     *
     * @param connection the "real" or "physical" connection, i.e. the type of 
     *                   object that would result from a call to P6SpyDriver.connect()
     */
    public P6PooledConnection(Connection connection) {
        proxyConnection = null;
        realConnection = connection;
        eventTargets = new Hashtable(5);
    }


    /**
    * Creates a new P6ProxyConnection, associates it with the real connection, and returns it.
    * The specified behavior of this method is to return a new Connection (P6ProxyConnection)
    * one each call. The specified behavior of the PooledConnection class is that at most 
    * one Connection is associated with the PooledConnection at any time, so any existing 
    * Connection is closed. If the real connection has been closed, then SQLException 
    * will be thrown. Other SQLExceptions may be thrown from the close call to the real connection.
    *
    * @exception java.sql.SQLException
    */
    public synchronized Connection getConnection() throws SQLException {

        if(realConnection == null) {
            SQLException sqlException = 
		new SQLException("Pooled Connection has no real connection, must have been closed");
            deliverEvent(EVENT_TYPE_ERROR, sqlException);
            return null;
        }
        try {
	    // There is already a proxy, so tell it to buzz off 
            if(proxyConnection != null) {
                proxyConnection.close();
            }
	    // Make a new proxy, giving it the real connection to proxy.
            proxyConnection = new P6ProxyConnection(this, realConnection);

        } catch(SQLException sqlException) {
            deliverEvent(EVENT_TYPE_ERROR, sqlException);
            return null;
        }
        return proxyConnection;
    }
    
    /**
     * Connection Pool managers such as EJB containers call this when they want to 
     * close the real connections, as during shutdown or reconfiguration, or when
     * this instance has delivered a connectionErrorOccured event. After this call,
     * the instance can no longer be used.
     * @exception java.sql.SQLException from underlying real connection close
     */
    
    public synchronized void close() throws SQLException {
        realConnection.close();
        realConnection = null;
        deliverEvent(EVENT_TYPE_ERROR, null);
    }
    
    /**
    * Registers submitted ConnectionEventListener as a target for delivery of events 
    * of the ConnectionEvents type, either close or error events.
    * 
    * @param eventTarget listener to be notified with ConnectionEvents
    */
    

    public synchronized void addConnectionEventListener(ConnectionEventListener eventTarget) {
	// synchronized to prevent possible failfast on Hashtable during event delivery

	// By putting the eventTarget in as both the key and the value, we get a uniqified
	// set for cheap. Also makes it dead simple to remove. This call should be rare.
        if(eventTargets != null) {
            eventTargets.put(eventTarget, eventTarget);
        }    
    }

    /**
    * Removes ConnectionEventListeners from the list of targets for event delivery. 
    *
    * @param eventTarget listener to be removed
    */
    
    public synchronized void removeConnectionEventListener(ConnectionEventListener eventTarget) {
	// synchronized to prevent possible failfast on Hashtable during event delivery

        if(eventTargets != null) {
            eventTargets.remove(eventTarget);
        }    
    }
    /**
     * Creates and delivers a ConnectionEvent to all registered (as registered in 
     * addConnectionEventListener) event targets. The newly created ConnectionEvent
     * instance contains the provided sqlException.
     *
     * @param type indicating connectionClosed (P6PooledConnection.EVENT_TYPE_CLOSE) 
     *         or connectionErrorOccurred (P6PooledConnection.EVENT_TYPE_ERROR) 
     * @exception java.sql.SQLException
    */
    
    protected synchronized void deliverEvent(int type, SQLException sqlException) {
	// synchronized to prevent related methods from causeing on Hashtable
        if(eventTargets == null) {
            return;
        }
	// If the event is a close event, then the sqlException will be null
        ConnectionEvent event = new ConnectionEvent(this, sqlException);

	// Find all the target listeners, and delivery the event
        Enumeration enumeration = eventTargets.elements();
        while(enumeration.hasMoreElements()) {
            ConnectionEventListener eventTarget = (ConnectionEventListener)enumeration.nextElement();
            if (type == EVENT_TYPE_CLOSE) {
                eventTarget.connectionClosed(event);
            } else if (type == EVENT_TYPE_ERROR) {
                eventTarget.connectionErrorOccurred(event);
            }    
        }
    }

}
