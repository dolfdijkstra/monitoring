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
 * Support for PooledConnection interface 
 *
 * $Author: cheechq $
 *
 * $Log: P6ProxyConnection.java,v $
 * Revision 1.4  2003/06/03 19:20:25  cheechq
 * removed unused imports
 *
 * Revision 1.3  2003/01/31 17:31:54  jeffgoke
 * updated proxy connection with the new 1.4 methods
 *
 * Revision 1.2  2003/01/30 23:38:49  dlukeparker
 *
 *
 * Added cvs keywords
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

import java.sql.*;

/***
 * This class proxies for a real connection in support of pooled connections.
 * Instances are created by calling P6PooledConnection.getConnection, which is 
 * done by P6PooledConnectionDataSource. Each instance is uniquely associated 
 * with the PooledConnection that created it, and with the real JDBC Connection 
 * that is associated with that object. 
 * 
 * After instantiation objects of this class are valid until the close method is 
 * called. This method does not close the read connection, rather it generates
 * and delivers a ConnectionEvent message of type close by calling the PooledConnection
 * object. This lets the ultimate user of these classes (probably an EJB Container)
 * know that the PooledConnection is avaiable for a new assignment.
 *
 * @see javax.sql.PooledConnection
 * @see javax.sql.ConnectionPoolDataSource
 * @see com.p6spy.engine.spy.P6PooledConnection
 * @see com.p6spy.engine.spy.P6ConnectionPoolDataSource
 *
 */

public class P6ProxyConnection implements Connection {

    private P6PooledConnection pooledConnection = null;
    private Connection connection = null;
    private String stateError = "The (pooled) connection is not valid because it has been closed";

    /** 
     * Take the given real connection and construct a proxy connection to represent it.
     * Associate the proxy with the PooledConnection instance that "owns" it.
     *
     * @param pooledConnection reference to pooledConnection that instantiated this object
     * @param realConnection P6Spy wrapper connection for real connection to db
     * @exception java.sql.SQLException
     */

    public P6ProxyConnection(P6PooledConnection pooledConnection, 
			     Connection realConnection) throws SQLException {
	this.pooledConnection = pooledConnection;
	this.connection = realConnection;
    }

    /**
    * 
    * Dissassociate this instance from the real connection, and notify any concerned
    * parties that the client is done with the associated pooled connection, so that
    * it may be returned to the pool. The state of the real connection is not affected by
    * this method.
    * 
    * @exception java.sql.SQLException
    */
     
    public synchronized void close() throws SQLException {
        if(connection == null) {
            return;
        }
        pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_CLOSE, null);
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
   
    public void clearWarnings() throws java.sql.SQLException  {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.clearWarnings();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public void commit() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.commit();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.Statement createStatement() throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.createStatement();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.createStatement(resultSetType, resultSetConcurrency);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public boolean getAutoCommit() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getAutoCommit();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public String getCatalog() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getCatalog();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getMetaData();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public int getTransactionIsolation() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getTransactionIsolation();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    
    public java.util.Map getTypeMap() throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getTypeMap();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getWarnings();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */


    public boolean isReadOnly() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.isReadOnly();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public String nativeSQL(String sql) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.nativeSQL(sql);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.CallableStatement prepareCall(String sql) 
    throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareCall(sql);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.CallableStatement prepareCall(String sql, 
                                                  int resultSetType,
                                                  int resultSetConcurrency) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public java.sql.PreparedStatement prepareStatement(String sql) 
    throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.PreparedStatement prepareStatement(String sql, 
                                                       int resultSetType,
                                                       int resultSetConcurrency) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public void rollback() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    
    public void setAutoCommit(boolean autoCommit) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setAutoCommit(autoCommit);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public void setCatalog(String catalog) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setCatalog(catalog);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public void  setReadOnly(boolean readOnly) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setReadOnly(readOnly);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public void setTransactionIsolation(int level) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setTransactionIsolation(level);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    
    public void setTypeMap(java.util.Map map) throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setTypeMap(map);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public boolean isClosed() throws java.sql.SQLException {
	if (connection == null) {
	    return true;
	}
	return false;
    }

    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public void setHoldability(int holdability) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.setHoldability(holdability);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public int getHoldability() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.getHoldability();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public Savepoint setSavepoint() throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.setSavepoint();
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public Savepoint setSavepoint(String name) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.setSavepoint(name);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public void releaseSavepoint(Savepoint savepoint) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.releaseSavepoint(savepoint);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    public void rollback(Savepoint savepoint) throws java.sql.SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                connection.rollback(savepoint);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }


    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */
    
    public java.sql.Statement createStatement(int resultSetType,
		    				int resultSetConcurrency,
						int resultSetHoldability)
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.createStatement(resultSetType, 
						resultSetConcurrency,
						resultSetHoldability);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.PreparedStatement prepareStatement(String sql, 
                                                       int resultSetType,
                                                       int resultSetConcurrency,
						       	int resultSetHoldability) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.CallableStatement prepareCall(String sql, 
                                                  int resultSetType,
                                                  int resultSetConcurrency, 
						  int resultSetHoldability) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.PreparedStatement prepareStatement(String sql, 
                                                  int autoGeneratedKeys) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql, autoGeneratedKeys);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.PreparedStatement prepareStatement(String sql, 
                                                  int[] columnIndexes) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql, columnIndexes);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
    /**
    * Pass through to P6 wrapper connection. Catches and re-throws exceptions after
    * delivering ConnectionEvents through P6PooledConnection.deliverEvent.
    * 
    * @exception java.sql.SQLException
    */

    public java.sql.PreparedStatement prepareStatement(String sql, 
                                                  String[] columnNames) 
    throws SQLException {
        if(connection == null) {
            throw new SQLException(stateError);
        } else {    
            try {
                return connection.prepareStatement(sql, columnNames);
            } catch (SQLException sqlException) {
                pooledConnection.deliverEvent(P6PooledConnection.EVENT_TYPE_ERROR, sqlException);
                throw sqlException;
            }       
        }    
    }
}
