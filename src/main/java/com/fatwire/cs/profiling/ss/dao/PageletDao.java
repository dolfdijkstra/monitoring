package com.fatwire.cs.profiling.ss.dao;

import java.util.Collection;

import com.fatwire.cs.profiling.ss.domain.Pagelet;

public interface PageletDao extends CrudDao<Pagelet,Long>{
    
    Collection<Pagelet> search(String param);
    

}
