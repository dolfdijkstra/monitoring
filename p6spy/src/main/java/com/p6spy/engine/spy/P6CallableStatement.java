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
 * Description: JDBC Driver Extension implementing CallableStatement.
 *
 * $Author: cheechq $
 * $Revision: 1.4 $
 * $Date: 2003/06/03 19:20:24 $
 *
 * $Id: P6CallableStatement.java,v 1.4 2003/06/03 19:20:24 cheechq Exp $
 * $Source: /cvsroot/p6spy/p6spy/com/p6spy/engine/spy/P6CallableStatement.java,v $
 * $Log: P6CallableStatement.java,v $
 * Revision 1.4  2003/06/03 19:20:24  cheechq
 * removed unused imports
 *
 * Revision 1.3  2003/01/03 20:33:42  aarvesen
 * Added getJDBC() method to return the underlying jdbc object.
 *
 * Revision 1.2  2002/12/06 22:40:13  aarvesen
 * Extend P6Base.
 * New factory registration in the constructor.
 *
 * Revision 1.1  2002/05/24 07:31:13  jeffgoke
 * version 1 rewrite
 *
 * Revision 1.4  2002/04/18 06:54:39  jeffgoke
 * added batch statement logging support
 *
 * Revision 1.3  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.2  2002/04/11 04:18:03  jeffgoke
 * fixed bug where callable & prepared were not passing their ancestors the correct constructor information
 *
 * Revision 1.1  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 * Revision 1.1.1.1  2002/04/07 04:52:25  jeffgoke
 * no message
 *
 * Revision 1.2  2001-08-05 09:16:04-05  andy
 * final version on the website
 *
 * Revision 1.1  2001-08-02 07:52:43-05  andy
 * <>
 *
 * Revision 1.0  2001-08-02 06:37:42-05  andy
 * Initial revision
 *
 *
 */

package com.p6spy.engine.spy;

import java.sql.*;

public class P6CallableStatement extends P6PreparedStatement implements java.sql.CallableStatement {
    
    
    protected CallableStatement callStmtPassthru;
    protected String callableQuery;
    
    public P6CallableStatement(P6Factory factory, CallableStatement statement, P6Connection conn, String query) {
        super(factory, statement, conn, query);
        this.callableQuery = query;
        this.callStmtPassthru = statement;
    }
    
    public String getString(int p0) throws SQLException {
        return callStmtPassthru.getString(p0);
    }
    
    public void registerOutParameter(int p0, int p1) throws SQLException {
        callStmtPassthru.registerOutParameter(p0, p1);
    }
    
    public void registerOutParameter(int p0, int p1, int p2) throws SQLException {
        callStmtPassthru.registerOutParameter(p0, p1, p2);
    }
    
    public void registerOutParameter(int p0, int p1, String p2) throws SQLException {
        callStmtPassthru.registerOutParameter(p0, p1, p2);
    }
    
    public boolean wasNull() throws SQLException {
        return callStmtPassthru.wasNull();
    }
    
    public java.sql.Array getArray(int p0) throws java.sql.SQLException {
        return getP6Factory().getArray(callStmtPassthru.getArray(p0),this,callableQuery,getQueryFromPreparedStatement());
    }
    
    public java.math.BigDecimal getBigDecimal(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getBigDecimal(p0);
    }
    
    public java.math.BigDecimal getBigDecimal(int p0, int p1) throws java.sql.SQLException {
        return callStmtPassthru.getBigDecimal(p0,p1);
    }
    
    public java.sql.Blob getBlob(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getBlob(p0);
    }
    
    public boolean getBoolean(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getBoolean(p0);
    }
    
    public byte getByte(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getByte(p0);
    }
    
    public byte[] getBytes(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getBytes(p0);
    }
    
    public java.sql.Clob getClob(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getClob(p0);
    }
    
    public java.sql.Date getDate(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getDate(p0);
    }
    
    public java.sql.Date getDate(int p0, java.util.Calendar calendar) throws java.sql.SQLException {
        return callStmtPassthru.getDate(p0,calendar);
    }
    
    public double getDouble(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getDouble(p0);
    }
    
    public float getFloat(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getFloat(p0);
    }
    
    public int getInt(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getInt(p0);
    }
    
    public long getLong(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getLong(p0);
    }
    
    public Object getObject(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getObject(p0);
    }
    
    public Object getObject(int p0, java.util.Map p1) throws java.sql.SQLException {
        return callStmtPassthru.getObject(p0, p1);
    }
    
    public java.sql.Ref getRef(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getRef(p0);
    }
    
    public short getShort(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getShort(p0);
    }
    
    public java.sql.Time getTime(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getTime(p0);
    }
    
    public java.sql.Time getTime(int p0, java.util.Calendar p1) throws java.sql.SQLException {
        return callStmtPassthru.getTime(p0,p1);
    }
    
    public java.sql.Timestamp getTimestamp(int p0) throws java.sql.SQLException {
        return callStmtPassthru.getTimestamp(p0);
    }
    
    public java.sql.Timestamp getTimestamp(int p0, java.util.Calendar p1) throws java.sql.SQLException {
        return callStmtPassthru.getTimestamp(p0,p1);
    }

    // Since JDK 1.4
    public void registerOutParameter(String p0, int p1) throws java.sql.SQLException {
        callStmtPassthru.registerOutParameter(p0, p1);
    }

    // Since JDK 1.4
    public void registerOutParameter(String p0, int p1, int p2) throws java.sql.SQLException {
        callStmtPassthru.registerOutParameter(p0, p1, p2);
    }

    // Since JDK 1.4
    public void registerOutParameter(String p0, int p1, String p2) throws java.sql.SQLException {
        callStmtPassthru.registerOutParameter(p0, p1, p2);
    }
    
    // Since JDK 1.4
    public java.net.URL getURL(int p0) throws java.sql.SQLException {
        return(callStmtPassthru.getURL(p0));
    }

    // Since JDK 1.4
    public void setURL(String p0, java.net.URL p1) throws java.sql.SQLException {
        callStmtPassthru.setURL(p0, p1);
    }

    // Since JDK 1.4
    public void setNull(String p0, int p1) throws java.sql.SQLException {
        callStmtPassthru.setNull(p0, p1);
    }

    // Since JDK 1.4
    public void setBoolean(String p0, boolean p1) throws java.sql.SQLException {
        callStmtPassthru.setBoolean(p0, p1);
    }

    // Since JDK 1.4
    public void setByte(String p0, byte p1) throws java.sql.SQLException {
        callStmtPassthru.setByte(p0, p1);
    }

    // Since JDK 1.4
    public void setShort(String p0, short p1) throws java.sql.SQLException {
        callStmtPassthru.setShort(p0, p1);
    }

    // Since JDK 1.4
    public void setInt(String p0, int p1) throws java.sql.SQLException {
        callStmtPassthru.setInt(p0, p1);
    }

    // Since JDK 1.4
    public void setLong(String p0, long p1) throws java.sql.SQLException {
        callStmtPassthru.setLong(p0, p1);
    }

    // Since JDK 1.4
    public void setFloat(String p0, float p1) throws java.sql.SQLException {
        callStmtPassthru.setFloat(p0, p1);
    }

    // Since JDK 1.4
    public void setDouble(String p0, double p1) throws java.sql.SQLException {
        callStmtPassthru.setDouble(p0, p1);
    }

    // Since JDK 1.4
    public void setBigDecimal(String p0, java.math.BigDecimal p1) throws java.sql.SQLException {
        callStmtPassthru.setBigDecimal(p0, p1);
    }

    // Since JDK 1.4
    public void setString(String p0, String p1) throws java.sql.SQLException {
        callStmtPassthru.setString(p0, p1);
    }

    // Since JDK 1.4
    public void setBytes(String p0, byte p1[]) throws java.sql.SQLException {
        callStmtPassthru.setBytes(p0, p1);
    }

    // Since JDK 1.4
    public void setDate(String p0, java.sql.Date p1) throws java.sql.SQLException {
        callStmtPassthru.setDate(p0, p1);
    }

    // Since JDK 1.4
    public void setTime(String p0, java.sql.Time p1) throws java.sql.SQLException {
        callStmtPassthru.setTime(p0, p1);
    }

    // Since JDK 1.4
    public void setTimestamp(String p0, java.sql.Timestamp p1) throws java.sql.SQLException {
        callStmtPassthru.setTimestamp(p0, p1);
    }

    // Since JDK 1.4
    public void setAsciiStream(String p0, java.io.InputStream p1, int p2) throws java.sql.SQLException {
        callStmtPassthru.setAsciiStream(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setBinaryStream(String p0, java.io.InputStream p1, int p2) throws java.sql.SQLException {
        callStmtPassthru.setBinaryStream(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setObject(String p0, Object p1, int p2, int p3) throws java.sql.SQLException {
        callStmtPassthru.setObject(p0, p1, p2, p3);
    }

    // Since JDK 1.4
    public void setObject(String p0, Object p1, int p2) throws java.sql.SQLException {
        callStmtPassthru.setObject(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setObject(String p0, Object p1) throws java.sql.SQLException {
        callStmtPassthru.setObject(p0, p1);
    }

    // Since JDK 1.4
    public void setCharacterStream(String p0, java.io.Reader p1, int p2) throws java.sql.SQLException {
        callStmtPassthru.setCharacterStream(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setDate(String p0, java.sql.Date p1, java.util.Calendar p2) throws java.sql.SQLException {
        callStmtPassthru.setDate(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setTime(String p0, java.sql.Time p1, java.util.Calendar p2) throws java.sql.SQLException {
        callStmtPassthru.setTime(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setTimestamp(String p0, java.sql.Timestamp p1, java.util.Calendar p2) throws java.sql.SQLException {
        callStmtPassthru.setTimestamp(p0, p1, p2);
    }

    // Since JDK 1.4
    public void setNull(String p0, int p1, String p2) throws java.sql.SQLException {
        callStmtPassthru.setNull(p0, p1, p2);
    }

    // Since JDK 1.4
    public String getString(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getString(p0));
    }

    // Since JDK 1.4
    public boolean getBoolean(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getBoolean(p0));
    }

    // Since JDK 1.4
    public byte getByte(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getByte(p0));
    }

    // Since JDK 1.4
    public short getShort(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getShort(p0));
    }

    // Since JDK 1.4
    public int getInt(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getInt(p0));
    }

    // Since JDK 1.4
    public long getLong(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getLong(p0));
    }

    // Since JDK 1.4
    public float getFloat(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getFloat(p0));
    }

    // Since JDK 1.4
    public double getDouble(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getDouble(p0));
    }

    // Since JDK 1.4
    public byte[] getBytes(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getBytes(p0));
    }

    // Since JDK 1.4
    public java.sql.Date getDate(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getDate(p0));
    }

    // Since JDK 1.4
    public java.sql.Time getTime(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getTime(p0));
    }

    // Since JDK 1.4
    public java.sql.Timestamp getTimestamp(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getTimestamp(p0));
    }

    // Since JDK 1.4
    public Object getObject(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getObject(p0));
    }

    // Since JDK 1.4
    public java.math.BigDecimal getBigDecimal(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getBigDecimal(p0));
    }

    // Since JDK 1.4
    public Object getObject(String p0, java.util.Map p1) throws java.sql.SQLException {
        return(callStmtPassthru.getObject(p0, p1));
    }

    // Since JDK 1.4
    public java.sql.Ref getRef(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getRef(p0));
    }

    // Since JDK 1.4
    public java.sql.Blob getBlob(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getBlob(p0));
    }

    // Since JDK 1.4
    public java.sql.Clob getClob(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getClob(p0));
    }

    // Since JDK 1.4
    public java.sql.Array getArray(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getArray(p0));
    }

    // Since JDK 1.4
    public java.sql.Date getDate(String p0, java.util.Calendar p1) throws java.sql.SQLException {
        return(callStmtPassthru.getDate(p0, p1));
    }

    // Since JDK 1.4
    public java.sql.Time getTime(String p0, java.util.Calendar p1) throws java.sql.SQLException {
        return(callStmtPassthru.getTime(p0, p1));
    }

    // Since JDK 1.4
    public java.sql.Timestamp getTimestamp(String p0, java.util.Calendar p1) throws java.sql.SQLException {
        return(callStmtPassthru.getTimestamp(p0, p1));
    }

    // Since JDK 1.4
    public java.net.URL getURL(String p0) throws java.sql.SQLException {
        return(callStmtPassthru.getURL(p0));
    }
    /**
     * Returns the underlying JDBC object (in this case, a
     * java.sql.CallableStatement).
     * <p>
     * The returned object is a java.sql.Statement due
     * to inheritance reasons, so you'll need to cast 
     * appropriately.
     *
     * @return the wrapped JDBC object 
     */
    public Statement getJDBC() {
	Statement wrapped = (callStmtPassthru instanceof P6Statement) ?
	    ((P6Statement) callStmtPassthru).getJDBC() :
	    callStmtPassthru;

	return wrapped;
    }

}
