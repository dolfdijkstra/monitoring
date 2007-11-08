package com.fatwire.cs.profiling.ss.util;

import java.io.File;

public class TempDir {
    private TempDir() {
    }

    public static File getTempDir() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public static File getTempDir(Class c) {
        final File f = new File(getTempDir(), c.getName());
        f.mkdirs();
        return f;
    }

}
