package com.fatwire.cs.profiling.ss.dao.jdbc;

public interface ParameterizedUpdateMapper<T> {
    Object[] toArray(T object);
}
