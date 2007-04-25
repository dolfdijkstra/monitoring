package com.fatwire.cs.spring.command;

import java.util.Map;

public interface ActionInjector {
    
    public void inject(Action action, Map<String,Object> valueMap) throws Exception;

}
