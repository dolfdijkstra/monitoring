package com.fatwire.cs.profiling.ss.events;

public interface JobListener {

    /**
     * Invoked after a job is scheduled
     * 
     * @param event
     */
    public void jobScheduled(JobScheduledEvent event);

    /**
     * 
     * Invoked after a job is started
     * @param event
     */

    public void jobStarted(JobStartedEvent event);

    /**
     * Invoked after a job is finished
     * @param event
     */
    public void jobFinished(JobFinishedEvent event);
}
