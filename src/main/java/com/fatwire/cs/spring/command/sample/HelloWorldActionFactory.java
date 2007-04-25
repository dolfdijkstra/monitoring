package com.fatwire.cs.spring.command.sample;

import com.fatwire.cs.spring.command.Action;
import com.fatwire.cs.spring.command.ActionFactory;

public class HelloWorldActionFactory implements ActionFactory {

    public Action createAction(String key) {
        return new HelloWorldAction();
    }

}
