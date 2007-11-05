package com.fatwire.cs.profiling.ss.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;

public interface SimpleJdbcWithUpdateOperations extends SimpleJdbcOperations {

    public <T> int  updateForObject(String sql, ParameterizedUpdateMapper<T> parameterizedUpdateMapper,
            T arg1) throws DataAccessException;

}
