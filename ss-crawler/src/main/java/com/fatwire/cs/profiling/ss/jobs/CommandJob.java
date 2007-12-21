package com.fatwire.cs.profiling.ss.jobs;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CommandJob implements Job {

    private final Set<JobChangeListener> listeners = new CopyOnWriteArraySet<JobChangeListener>();

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
    }

    public void run(ProgressMonitor monitor) {
        final JobStartedEvent event = new JobStartedEvent(this);
        for (final JobChangeListener listener : listeners) {
            listener.jobStarted(event);
        }

        command.execute(monitor);
        final JobFinishedEvent finishedEvent = new JobFinishedEvent(this);
        for (final JobChangeListener listener : listeners) {

            listener.jobFinished(finishedEvent);
        }
    }

    public void addListener(final JobChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final JobChangeListener listener) {
        listeners.remove(listener);
    }

}