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
 * Description: JDBC Driver Extension implementing PreparedStatement.
 *
 * $Author: cheechq $
 * $Revision: 1.5 $
 * $Date: 2003/06/03 19:20:25 $
 *
 * $Id: P6ResultSet.java,v 1.5 2003/06/03 19:20:25 cheechq Exp $
 * $Source: /cvsroot/p6spy/p6spy/com/p6spy/engine/spy/P6ResultSet.java,v $
 * $Log: P6ResultSet.java,v $
 * Revision 1.5  2003/06/03 19:20:25  cheechq
 * removed unused imports
 *
 * Revision 1.4  2003/01/03 20:33:43  aarvesen
 * Added getJDBC() method to return the underlying jdbc object.
 *
 * Revision 1.3  2002/12/06 22:43:44  aarvesen
 * Extend P6Base.
 * New factory registration in the constructor.
 * jdk 1.4
 *
 * Revision 1.2  2002/10/06 18:23:25  jeffgoke
 * no message
 *
 * Revision 1.1  2002/05/24 07:31:13  jeffgoke
 * version 1 rewrite
 *
 * Revision 1.3  2002/05/16 04:58:40  jeffgoke
 * Viktor Szathmary added multi-driver support.
 * Rewrote P6SpyOptions to be easier to manage.
 * Fixed several bugs.
 *
 * Revision 1.2  2002/04/15 05:13:32  jeffgoke
 * Simon Sadedin added timing support.  Fixed bug where batch execute was not
 * getting logged.  Added result set timing.  Updated the log format to include
 * categories, and updated options to control the categories.  Updated
 * documentation.
 *
 * Revision 1.1  2002/04/10 04:24:26  jeffgoke
 * added support for callable statements and fixed numerous bugs that allowed the real class to be returned
 *
 *
 *
 */

package com.p6spy.engine.spy;

import com.p6spy.engine.common.P6LogQuery;
import java.io.*;
import java.sql.*;
import java.math.*;
import java.util.*;

public class P6ResultSet extends P6Base implements ResultSet {
    
    private static final String CATEGORY ="resultset";
    protected ResultSet passthru;
    protected P6Statement statement;
    protected String query;
    protected String preparedQuery;
    private Map resultMap = new TreeMap();
    private int currRow = -1;
    private boolean catChecked = false;
    private boolean isCatOk = false;
    public P6ResultSet(P6Factory factory, ResultSet resultSet, P6Statement statement, String preparedQuery, String query) {
        setP6Factory(factory);
        this.passthru = resultSet;
        this.statement = statement;
        this.query = query;
        this.preparedQuery = preparedQuery;
    }
    
    /**
     * This gets overloaded in the P6LogResultSet, but may need to do what that class does
     */
    public boolean next() throws SQLException {
        // only dump the data on subsequent calls to next
        if (currRow > -1){
            
            if (!catChecked){
                catChecked=true;
                isCatOk = P6LogQuery.isCategoryOk(CATEGORY);
            }
            if(isCatOk){
                StringBuffer buffer = new StringBuffer();
                for (Iterator itr = resultMap.keySet().iterator(); itr.hasNext();){
                    String index = (String)itr.next();
                    buffer.append(index);
                    buffer.append(" = ");
                    buffer.append((String)resultMap.get(index));
                    buffer.append(", ");
                }
                P6LogQuery.log("resultset", query, buffer.substring(0, buffer.length() - 2).toString());
            }
            resultMap.clear();
        }
        
        currRow++;
        return passthru.next();
    }
    
    public int getRow() throws SQLException {
        return passthru.getRow();
    }
    
    public byte[] getBytes(String p0) throws SQLException {
        return passthru.getBytes(p0);
    }
    
    public byte[] getBytes(int p0) throws SQLException {
        return getBytes(passthru.getMetaData().getColumnName(p0));
    }
    
    public boolean getBoolean(int p0) throws SQLException {
        return getBoolean(passthru.getMetaData().getColumnName(p0));
    }
    
    public boolean getBoolean(String p0) throws SQLException {
        return passthru.getBoolean(p0);
    }
    
    public int getType() throws SQLException {
        return passthru.getType();
    }
    
    public long getLong(int p0) throws SQLException {
        return getLong(passthru.getMetaData().getColumnName(p0));
    }
    
    public long getLong(String p0) throws SQLException {
        return passthru.getLong(p0);
    }
    
    public boolean previous() throws SQLException {
        return passthru.previous();
    }
    
    public void close() throws SQLException {
        passthru.close();
    }
    
    public Object getObject(String p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0,p1);
    }
    
    public Object getObject(int p0) throws SQLException {
        return getObject(passthru.getMetaData().getColumnName(p0));
    }
    
    public Object getObject(String p0) throws SQLException {
        return passthru.getObject(p0);
    }
    
    public Object getObject(int p0, java.util.Map p1) throws SQLException {
        return passthru.getObject(p0,p1);
    }
    
    public Ref getRef(String p0) throws SQLException {
        return passthru.getRef(p0);
    }
    
    public Ref getRef(int p0) throws SQLException {
        return getRef(passthru.getMetaData().getColumnName(p0));
    }
    
    public Time getTime(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0,p1);
    }
    
    public Time getTime(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTime(p0,p1);
    }
    
    public Time getTime(String p0) throws SQLException {
        return passthru.getTime(p0);
    }
    
    public Time getTime(int p0) throws SQLException {
        return getTime(passthru.getMetaData().getColumnName(p0));
    }
    
    public java.sql.Date getDate(int p0) throws SQLException {
        return getDate(passthru.getMetaData().getColumnName(p0));
    }
    
    public java.sql.Date getDate(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0);
    }
    
    public java.sql.Date getDate(String p0) throws SQLException {
        return passthru.getDate(p0);
    }
    
    public java.sql.Date getDate(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getDate(p0,p1);
    }
    
    public boolean wasNull() throws SQLException {
        return passthru.wasNull();
    }
    
    public String getString(String p0) throws SQLException {
        String result = passthru.getString(p0);
        resultMap.put(p0, result);
        return result;
    }
    
    public String getString(int p0) throws SQLException {
        return getString(passthru.getMetaData().getColumnName(p0));
    }
    
    public byte getByte(String p0) throws SQLException {
        return passthru.getByte(p0);
    }
    
    public byte getByte(int p0) throws SQLException {
        return getByte(passthru.getMetaData().getColumnName(p0));
    }
    
    public short getShort(String p0) throws SQLException {
        short result = passthru.getShort(p0);
        resultMap.put(p0, String.valueOf(result));
        return result;
    }
    
    public short getShort(int p0) throws SQLException {
        return getShort(passthru.getMetaData().getColumnName(p0));
    }
    
    public int getInt(int p0) throws SQLException {
        return getInt(passthru.getMetaData().getColumnName(p0));
    }
    
    public int getInt(String p0) throws SQLException {
        int result = passthru.getInt(p0);
        resultMap.put(p0, String.valueOf(result));
        return result;
    }
    
    public float getFloat(String p0) throws SQLException {
        return passthru.getFloat(p0);
    }
    
    public float getFloat(int p0) throws SQLException {
        return getFloat(passthru.getMetaData().getColumnName(p0));
    }
    
    public double getDouble(int p0) throws SQLException {
        return getDouble(passthru.getMetaData().getColumnName(p0));
    }
    
    public double getDouble(String p0) throws SQLException {
        return passthru.getDouble(p0);
    }
    
    public BigDecimal getBigDecimal(String p0) throws SQLException {
        return passthru.getBigDecimal(p0);
    }
    
    public BigDecimal getBigDecimal(int p0) throws SQLException {
        return getBigDecimal(passthru.getMetaData().getColumnName(p0));
    }
    
    public BigDecimal getBigDecimal(int p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0,p1);
    }
    
    public BigDecimal getBigDecimal(String p0, int p1) throws SQLException {
        return passthru.getBigDecimal(p0,p1);
    }
    
    public Timestamp getTimestamp(String p0) throws SQLException {
        return passthru.getTimestamp(p0);
    }
    
    public Timestamp getTimestamp(String p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0,p1);
    }
    
    public Timestamp getTimestamp(int p0) throws SQLException {
        return getTimestamp(passthru.getMetaData().getColumnName(p0));
    }
    
    public Timestamp getTimestamp(int p0, java.util.Calendar p1) throws SQLException {
        return passthru.getTimestamp(p0,p1);
    }
    
    public InputStream getAsciiStream(String p0) throws SQLException {
        return passthru.getAsciiStream(p0);
    }
    
    public InputStream getAsciiStream(int p0) throws SQLException {
        return getAsciiStream(passthru.getMetaData().getColumnName(p0));
    }
    
    public InputStream getUnicodeStream(int p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }
    
    public InputStream getUnicodeStream(String p0) throws SQLException {
        return passthru.getUnicodeStream(p0);
    }
    
    public InputStream getBinaryStream(int p0) throws SQLException {
        return getBinaryStream(passthru.getMetaData().getColumnName(p0));
    }
    
    public InputStream getBinaryStream(String p0) throws SQLException {
        return passthru.getBinaryStream(p0);
    }
    
    public SQLWarning getWarnings() throws SQLException {
        return passthru.getWarnings();
    }
    
    public void clearWarnings() throws SQLException {
        passthru.clearWarnings();
    }
    
    public String getCursorName() throws SQLException {
        return passthru.getCursorName();
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        return getP6Factory().getResultSetMetaData(passthru.getMetaData());
    }
    
    public int findColumn(String p0) throws SQLException {
        return passthru.findColumn(p0);
    }
    
    public Reader getCharacterStream(String p0) throws SQLException {
        return passthru.getCharacterStream(p0);
    }
    
    public Reader getCharacterStream(int p0) throws SQLException {
        return getCharacterStream(passthru.getMetaData().getColumnName(p0));
    }
    
    public boolean isBeforeFirst() throws SQLException {
        return passthru.isBeforeFirst();
    }
    
    public boolean isAfterLast() throws SQLException {
        return passthru.isAfterLast();
    }
    
    public boolean isFirst() throws SQLException {
        return passthru.isFirst();
    }
    
    public boolean isLast() throws SQLException {
        return passthru.isLast();
    }
    
    public void beforeFirst() throws SQLException {
        passthru.beforeFirst();
    }
    
    public void afterLast() throws SQLException {
        passthru.afterLast();
    }
    
    public boolean first() throws SQLException {
        return passthru.first();
    }
    
    public boolean last() throws SQLException {
        return passthru.last();
    }
    
    public boolean absolute(int p0) throws SQLException {
        return passthru.absolute(p0);
    }
    
    public boolean relative(int p0) throws SQLException {
        return passthru.relative(p0);
    }
    
    public void setFetchDirection(int p0) throws SQLException {
        passthru.setFetchDirection(p0);
    }
    
    public int getFetchDirection() throws SQLException {
        return passthru.getFetchDirection();
    }
    
    public void setFetchSize(int p0) throws SQLException {
        passthru.setFetchSize(p0);
    }
    
    public int getFetchSize() throws SQLException {
        return passthru.getFetchSize();
    }
    
    public int getConcurrency() throws SQLException {
        return passthru.getConcurrency();
    }
    
    public boolean rowUpdated() throws SQLException {
        return passthru.rowUpdated();
    }
    
    public boolean rowInserted() throws SQLException {
        return passthru.rowInserted();
    }
    
    public boolean rowDeleted() throws SQLException {
        return passthru.rowDeleted();
    }
    
    public void updateNull(int p0) throws SQLException {
        passthru.updateNull(p0);
    }
    
    public void updateNull(String p0) throws SQLException {
        passthru.updateNull(p0);
    }
    
    public void updateBoolean(int p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0,p1);
    }
    
    public void updateBoolean(String p0, boolean p1) throws SQLException {
        passthru.updateBoolean(p0,p1);
    }
    
    public void updateByte(String p0, byte p1) throws SQLException {
        passthru.updateByte(p0,p1);
    }
    
    public void updateByte(int p0, byte p1) throws SQLException {
        passthru.updateByte(p0,p1);
    }
    
    public void updateShort(int p0, short p1) throws SQLException {
        passthru.updateShort(p0,p1);
    }
    
    public void updateShort(String p0, short p1) throws SQLException {
        passthru.updateShort(p0,p1);
    }
    
    public void updateInt(int p0, int p1) throws SQLException {
        passthru.updateInt(p0,p1);
    }
    
    public void updateInt(String p0, int p1) throws SQLException {
        passthru.updateInt(p0,p1);
    }
    
    public void updateLong(int p0, long p1) throws SQLException {
        passthru.updateLong(p0,p1);
    }
    
    public void updateLong(String p0, long p1) throws SQLException {
        passthru.updateLong(p0,p1);
    }
    
    public void updateFloat(String p0, float p1) throws SQLException {
        passthru.updateFloat(p0,p1);
    }
    
    public void updateFloat(int p0, float p1) throws SQLException {
        passthru.updateFloat(p0,p1);
    }
    
    public void updateDouble(int p0, double p1) throws SQLException {
        passthru.updateDouble(p0,p1);
    }
    
    public void updateDouble(String p0, double p1) throws SQLException {
        passthru.updateDouble(p0,p1);
    }
    
    public void updateBigDecimal(String p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0,p1);
    }
    
    public void updateBigDecimal(int p0, BigDecimal p1) throws SQLException {
        passthru.updateBigDecimal(p0,p1);
    }
    
    public void updateString(String p0, String p1) throws SQLException {
        passthru.updateString(p0,p1);
    }
    
    public void updateString(int p0, String p1) throws SQLException {
        passthru.updateString(p0,p1);
    }
    
    public void updateBytes(int p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0,p1);
    }
    
    public void updateBytes(String p0, byte[] p1) throws SQLException {
        passthru.updateBytes(p0,p1);
    }
    
    public void updateDate(int p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0,p1);
    }
    
    public void updateDate(String p0, java.sql.Date p1) throws SQLException {
        passthru.updateDate(p0,p1);
    }
    
    public void updateTime(String p0, Time p1) throws SQLException {
        passthru.updateTime(p0,p1);
    }
    
    public void updateTime(int p0, Time p1) throws SQLException {
        passthru.updateTime(p0,p1);
    }
    
    public void updateTimestamp(int p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0,p1);
    }
    
    public void updateTimestamp(String p0, Timestamp p1) throws SQLException {
        passthru.updateTimestamp(p0,p1);
    }
    
    public void updateAsciiStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0,p1,p2);
    }
    
    public void updateAsciiStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateAsciiStream(p0,p1,p2);
    }
    
    public void updateBinaryStream(int p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0,p1,p2);
    }
    
    public void updateBinaryStream(String p0, InputStream p1, int p2) throws SQLException {
        passthru.updateBinaryStream(p0,p1,p2);
    }
    
    public void updateCharacterStream(int p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0,p1,p2);
    }
    
    public void updateCharacterStream(String p0, Reader p1, int p2) throws SQLException {
        passthru.updateCharacterStream(p0,p1,p2);
    }
    
    public void updateObject(int p0, Object p1) throws SQLException {
        passthru.updateObject(p0,p1);
    }
    
    public void updateObject(int p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0,p1,p2);
    }
    
    public void updateObject(String p0, Object p1) throws SQLException {
        passthru.updateObject(p0,p1);
    }
    
    public void updateObject(String p0, Object p1, int p2) throws SQLException {
        passthru.updateObject(p0,p1,p2);
    }
    
    public void insertRow() throws SQLException {
        passthru.insertRow();
    }
    
    public void updateRow() throws SQLException {
        passthru.updateRow();
    }
    
    public void deleteRow() throws SQLException {
        passthru.deleteRow();
    }
    
    public void refreshRow() throws SQLException {
        passthru.refreshRow();
    }
    
    public void cancelRowUpdates() throws SQLException {
        passthru.cancelRowUpdates();
    }
    
    public void moveToInsertRow() throws SQLException {
        passthru.moveToInsertRow();
    }
    
    public void moveToCurrentRow() throws SQLException {
        passthru.moveToCurrentRow();
    }
    
    public Statement getStatement() throws SQLException {
        return this.statement;
    }
    
    public Blob getBlob(int p0) throws SQLException {
        return getBlob(passthru.getMetaData().getColumnName(p0));
    }
    
    public Blob getBlob(String p0) throws SQLException {
        return passthru.getBlob(p0);
    }
    
    public Clob getClob(String p0) throws SQLException {
        return passthru.getClob(p0);
    }
    
    public Clob getClob(int p0) throws SQLException {
        return getClob(passthru.getMetaData().getColumnName(p0));
    }
    
    public Array getArray(int p0) throws SQLException {
        return getP6Factory().getArray(passthru.getArray(p0),statement,preparedQuery,query);
    }
    
    public Array getArray(String p0) throws SQLException {
        return getP6Factory().getArray(passthru.getArray(p0),statement,preparedQuery,query);
    }
    
    // Since JDK 1.4
    public java.net.URL getURL(int p0) throws SQLException {
        return passthru.getURL(p0);
    }
    
    // Since JDK 1.4
    public java.net.URL getURL(String p0) throws SQLException {
        return passthru.getURL(p0);
    }
    
    // Since JDK 1.4
    public void updateRef(int p0, Ref p1) throws SQLException {
        passthru.updateRef(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateRef(String p0, Ref p1) throws SQLException {
        passthru.updateRef(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateBlob(int p0, Blob p1) throws SQLException {
        passthru.updateBlob(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateBlob(String p0, Blob p1) throws SQLException {
        passthru.updateBlob(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateClob(int p0, Clob p1) throws SQLException {
        passthru.updateClob(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateClob(String p0, Clob p1) throws SQLException {
        passthru.updateClob(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateArray(int p0, Array p1) throws SQLException {
        passthru.updateArray(p0, p1);
    }
    
    // Since JDK 1.4
    public void updateArray(String p0, Array p1) throws SQLException {
        passthru.updateArray(p0, p1);
    }
    
    /**
     * Returns the underlying JDBC object (in this case, a
     * java.sql.ResultSet)
     * @return the wrapped JDBC object
     */
    public ResultSet getJDBC() {
        ResultSet wrapped = (passthru instanceof P6ResultSet) ?
        ((P6ResultSet) passthru).getJDBC() :
            passthru;
            
            return wrapped;
    }
}

