package com.fatwire.cs.profiling.ss.events;

import com.fatwire.cs.profiling.ss.jobs.Job;

public class JobStartedEvent extends EventObject<Job> {

    public JobStartedEvent(Job source) {
        super(source);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4482542445018440882L;

}
