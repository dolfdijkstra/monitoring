package com.fatwire.cs.profiling.ss.events;

import com.fatwire.cs.profiling.ss.jobs.Job;



public class JobFinishedEvent extends EventObject<Job> {

    public JobFinishedEvent(Job source) {
        super(source);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5574306919853993009L;

}
