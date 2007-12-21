package com.fatwire.cs.profiling.ss.jobs;

public interface Job {

    void schedule();

    void run(ProgressMonitor monitor);
    
    
}
