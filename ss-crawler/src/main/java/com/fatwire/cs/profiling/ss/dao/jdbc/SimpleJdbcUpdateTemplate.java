package com.fatwire.cs.profiling.ss.dao.jdbc;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SimpleJdbcUpdateTemplate extends SimpleJdbcTemplate implements
        SimpleJdbcWithUpdateOperations {

    public SimpleJdbcUpdateTemplate(final DataSource dataSource) {
        super(dataSource);
    }

    public <T> int updateForObject(final String sql,
            final ParameterizedUpdateMapper<T> parameterizedUpdateMapper, final T object)
            throws DataAccessException {

        return update(sql, parameterizedUpdateMapper.toArray(object));
    }

}
