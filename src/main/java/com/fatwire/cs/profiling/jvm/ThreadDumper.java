package com.fatwire.cs.profiling.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadDumper {

    private final ThreadMXBean threadMXBean;

    private static final String INDENT = "    ";

    private static final String NEW_LINE = "\r\n";

    public ThreadDumper() {
        super();
        threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public void createThreadDump(StringBuilder sb) {
        ThreadInfo[] threadInfos;
        threadInfos = threadMXBean.getThreadInfo(
                threadMXBean.getAllThreadIds(), Integer.MAX_VALUE);
        
        for (ThreadInfo threadInfo : threadInfos) {
            printThreadInfo(threadInfo, sb);
        }

    }

    protected void printThreadInfo(ThreadInfo ti, final StringBuilder sb) {
        long tid = ti.getThreadId();
        sb.append("\"" + ti.getThreadName() + "\"" + " Id=" + tid + " in "
                + ti.getThreadState());
        if (ti.getLockName() != null) {
            sb.append(" on lock=" + ti.getLockName());
        }
        if (ti.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (ti.isInNative()) {
            sb.append(" (running in native)");
        }
        if (threadMXBean.isThreadCpuTimeSupported()) {
            sb.append(" total cpu time="
                    + formatNanos(threadMXBean.getThreadCpuTime(tid)));
            sb.append(" user time="
                    + formatNanos(threadMXBean.getThreadUserTime(tid)));
        }
        if (ti.getLockOwnerName() != null) {
            sb.append(INDENT + " owned by " + ti.getLockOwnerName() + " Id="
                    + ti.getLockOwnerId());

        }
        sb.append(NEW_LINE);
        for (StackTraceElement ste : ti.getStackTrace()) {
            sb.append(INDENT + "at " + ste.toString());
            sb.append(NEW_LINE);
        }
        sb.append(NEW_LINE);

    }

    static String formatNanos(long ns) {
        return String.format("%.4f ms", ns / 1000000D);
    }

}
