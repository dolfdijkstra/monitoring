package com.fatwire.cs.profiling.ss.dao;


/**
 * Base interface for CRUD operations
 * 
 * @author Dolf.Dijkstra
 * @since 14-jun-2007
 * @param <E> the domain object to work with
 * @param <K> the type of the unique id of the object
 */
public interface CrudDao<E,K> {
    
    void create(E e);
    
    E read(K id);
    
    void update(E e);
    
    void delete(E e);
    
    void deleteById(K id);


}
