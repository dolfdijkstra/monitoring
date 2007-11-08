package com.fatwire.cs.profiling.ss.dao;

import java.net.URI;
import java.util.Collection;

import com.fatwire.cs.profiling.ss.domain.HostConfig;

public interface HostConfigDao extends CrudDao<HostConfig,Long> {
    
    /**
     * search for a list of HostConfigs based on the uri
     * @param uri
     * @return
     */
    Collection<HostConfig> search(URI uri);


}
