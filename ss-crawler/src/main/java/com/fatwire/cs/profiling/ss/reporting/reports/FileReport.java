package com.fatwire.cs.profiling.ss.reporting.reports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.fatwire.cs.profiling.ss.reporting.Report;

public class FileReport implements Report {

    private PrintWriter writer;

    private final File file;

    /**
     * @param file
     
     */
    public FileReport(final File file) {
        super();
        this.file = file;

    }

    public FileReport(final File parentDir, String name) {
        super();
        this.file = new File(parentDir, name);

    }

    public synchronized void addRow(String line) {
        writer.println(line);

    }

    public void finishReport() {
        writer.close();
        writer = null;
    }

    public void startReport() {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new RuntimeException("Can't create parent folder for "
                        + file);
            }
        } else {
            if (!file.getParentFile().isDirectory()) {
                throw new RuntimeException("parent folder for " + file
                        + " is not a directory");
            }

        }
        try {
            this.writer = new PrintWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
