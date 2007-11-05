package com.fatwire.cs.profiling.ss.dao.jdbc;

import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public abstract class AbstractJdbcCrud<E, U> implements InitializingBean {
    protected SimpleJdbcUpdateTemplate simpleJdbcTemplate;

    protected KeyGenerator keyGenerator;

    private final String tableName;

    protected final String getTableName() {
        return tableName;
    }

    private final String[] cols;

    public AbstractJdbcCrud(String tableName, String[] columns) {
        super();
        this.tableName = tableName;
        this.cols = columns;
        keyGenerator = new KeyGenerator() {
            private volatile AtomicLong id = new AtomicLong(System
                    .currentTimeMillis());

            public long getNextKey() {
                return id.incrementAndGet();
            }

        };
    }

    protected final String[] getColumnNames() {
        return cols;
    }

    protected final String getColumnNamesAsString() {
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < cols.length; i++) {
            if (i > 0)
                b.append(',');
            b.append(cols[i]);

        }
        return b.toString();
    }

    public final void setDataSource(final DataSource dataSource) {
        simpleJdbcTemplate = new SimpleJdbcUpdateTemplate(dataSource);
    }

    protected String getReadSql() {
        return "select " + this.getColumnNamesAsString() + " from "
                + getTableName() + " where id = ?";

    }

    protected String getInsertSql() {
        StringBuilder b = new StringBuilder();
        b.append("insert into ").append(getTableName());
        b.append(" (").append(getColumnNamesAsString());
        b.append(") values (");
        for (int i = 0; i < this.getColumnNames().length; i++) {
            if (i > 1)
                b.append(',');

            b.append("? ");
        }
        b.append(")");
        return b.toString();
    }

    protected String getDeleteSql() {
        return "delete from " + getTableName() + " where id=?";

    }

    protected String getUpdateSql() {
        StringBuilder b = new StringBuilder();
        b.append("update ").append(getTableName());
        b.append(" set ");
        for (int i = 1; i < this.getColumnNames().length; i++) {
            if (i > 1)
                b.append(',');
            b.append(this.getColumnNames()[i]);
            b.append("= ? ");
        }
        b.append("where id = ?");
        return b.toString();

    }

    protected abstract ParameterizedUpdateMapper<E> getParameterizedUpdateMapper();

    protected abstract ParameterizedUpdateMapper<E> getParameterizedCreateMapper();

    protected abstract ParameterizedRowMapper<E> getMapper();

    public final E read(final U id) {
        return simpleJdbcTemplate.queryForObject(this.getReadSql(),
                getMapper(), id);
    }

    public final void update(final E e) {
        simpleJdbcTemplate.updateForObject(getUpdateSql(),
                getParameterizedUpdateMapper(), e);
    }

    public final void deleteById(final U id) {
        simpleJdbcTemplate.update(getDeleteSql(), id);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (simpleJdbcTemplate == null)
            throw new NullPointerException("dataSource not set");
        if (tableName == null)
            throw new NullPointerException("tableName not set");
        if (cols == null)
            throw new NullPointerException("cols not set");
        if (cols.length < 1)
            throw new IllegalStateException("not enough columns");

    }

}