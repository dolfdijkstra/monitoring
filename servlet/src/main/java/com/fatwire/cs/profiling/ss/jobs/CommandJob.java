package com.fatwire.cs.profiling.ss.jobs;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.fatwire.cs.profiling.ss.events.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.events.JobListener;
import com.fatwire.cs.profiling.ss.events.JobStartedEvent;

public class CommandJob implements Job {

    private final Set<JobListener> listeners = new CopyOnWriteArraySet<JobListener>();

    private final Command command;

    /**
     * @param hostConfig
     * @param maxPages
     */
    public CommandJob(final Command command) {
        super();
        this.command = command;
    }

    public void schedule() {
        final JobStartedEvent event = new JobStartedEvent(this);
        for (final JobListener listener : listeners) {
            listener.jobStarted(event);
        }

        command.execute();
        final JobFinishedEvent finishedEvent = new JobFinishedEvent(this);
        for (final JobListener listener : listeners) {

            listener.jobFinished(finishedEvent);
        }
    }

    public void addListener(final JobListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final JobListener listener) {
        listeners.remove(listener);
    }

}