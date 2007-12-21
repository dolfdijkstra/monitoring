package com.fatwire.cs.profiling.ss.jobs;

public class StdOutProgressMonitor implements ProgressMonitor {

    private int totalWork;

    private String taskName;

    private boolean cancelled;

    private String subTask;

    public void beginTask(String name, int totalWork) {
        this.taskName = name;
        this.totalWork = totalWork;
        System.out.println(name);

    }

    public void done() {
        System.out.println(taskName +" is finished");

    }

    public void internalWorked(double work) {
        System.out.print("#");

    }

    public boolean isCanceled() {
        return cancelled;
    }

    public void setCanceled(boolean value) {
        this.cancelled = value;

    }

    public void setTaskName(String name) {
        this.taskName = name;
        System.out.println(name);

    }

    public void subTask(String name) {
        this.subTask = name;
        System.out.println(name);

    }

    public void worked(int work) {
        System.out.print("#");

    }

}
