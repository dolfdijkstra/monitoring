package com.fatwire.cs.profiling.jvm;

import java.lang.management.ThreadInfo;

public class LockDumper extends ThreadDumper {

    public LockDumper() {
        super();
    }

    public void createThreadDump(StringBuilder sb) {

        long[] ids = threadMXBean.findMonitorDeadlockedThreads();
        if (ids != null) {
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ids,
                    Integer.MAX_VALUE);
            for (ThreadInfo threadInfo : threadInfos) {
                printThreadInfo(threadInfo, sb);
            }
        } else {
            sb.append("No deadlocks detected");
        }
    }
}
