package com.fatwire.cs.profiling.ss.jobs;

import com.fatwire.cs.profiling.ss.events.EventObject;

public class JobScheduledEvent extends EventObject<Job> {

    public JobScheduledEvent(Job source) {
        super(source);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7949941069045531614L;

}
