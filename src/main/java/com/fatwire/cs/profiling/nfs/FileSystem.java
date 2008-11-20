/*
 * Copyright (c) 2007 FatWire Corporation. All Rights Reserved.
 * Title, ownership rights, and intellectual property rights in and
 * to this software remain with FatWire Corporation. This  software
 * is protected by international copyright laws and treaties, and
 * may be protected by other law.  Violation of copyright laws may
 * result in civil liability and criminal penalties.
 */

package com.fatwire.cs.profiling.nfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class FileSystem {
    final int numThreads;

    final String path;

    final int numFiles;

    private final byte[] data = "sssssdfjkahgsjklahgkljsdfhgksdfjhgkjlfhgkjldfhgkjhfdkjghfklsdjghdfjkhdfkljhgkldfjgh"
            .getBytes();

    /**
     * @param numThreads
     * @param path
     * @param numFiles
     */
    public FileSystem(final int numThreads, final String path,
            final int numFiles) {
        super();
        this.numThreads = numThreads;
        this.path = path;
        this.numFiles = numFiles;
    }

    public static void main(final String[] args) {
        if ((null == args) || (args.length < 3)) {
            System.out
                    .println("Usage: java TestFS <number of threads> <path> <num files>");
            return;
        }

        final Integer numThreads = Integer.valueOf(args[0]);
        final String path = args[1];
        final Integer numFiles = Integer.valueOf(args[2]);
        final FileSystem fs = new FileSystem(numThreads, path, numFiles);
        fs.doWork();
    }

    long doWork() {

        final ExecutorService service = Executors
                .newFixedThreadPool(numThreads);

        final TimeReporter reporter = new TimeReporter() {
            int totalReports = 0;

            final AtomicLong totalTime = new AtomicLong();

            public void report(String runnableName, long time) {
                totalReports++;
                totalTime.addAndGet(time);

                System.out.println(runnableName + " time=" + time);

            }

            public void reportFailure(Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();

            }

            /* (non-Javadoc)
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return new StringBuilder("totalTime: ").append(
                        this.totalTime.get()).append(", totalReports: ")
                        .append(totalReports).toString();

            }

        };
        final long startTime = System.currentTimeMillis();

        for (int x = 0; x < numThreads; x++) {
            service.submit(new FileAccessRunnable("worker-" + x, reporter));
        }

        try {
            service.shutdown();
            service.awaitTermination(60 * 60, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            // todo: handle e properly
            System.err.println(e);
            e.printStackTrace();
        }
        final long time = System.currentTimeMillis() - startTime;
        System.out.println("Total time " + (time));
        System.out.println(reporter.toString());
        return time;
    }

    interface TimeReporter {
        void report(String runnableName, long time);

        void reportFailure(Exception e);
    }

    private class FileAccessRunnable implements Runnable {

        private final TimeReporter reporter;

        private final String runnableName;

        final File dir;

        FileAccessRunnable(final String runnableName,
                final TimeReporter reporter) {
            this.runnableName = runnableName;
            this.reporter = reporter;
            // First create the subDir in path
            dir = new File(path, runnableName);
            

        }

        File getFile(int x) {
            return new File(dir, Integer.toString(x) + ".lock");
        }

        public void run() {
            dir.mkdirs();
            final long startTime = System.currentTimeMillis();

            try {
                // Create 100 files
                for (int x = 0; x < numFiles; x++) {
                    final File f = getFile(x);
                    f.createNewFile();
                    final FileOutputStream os = new FileOutputStream(f);
                    os.write(data);
                    os.close();
                }

                // read these files using a RandomAccessFile
                for (int x = 0; x < numFiles; x++) {
                    final RandomAccessFile ra = new RandomAccessFile(
                            getFile(x), "rws");
                    ra.close();
                }

                // read canonical path
                for (int x = 0; x < numFiles; x++) {
                    getFile(x).getCanonicalFile();
                }

                // delete
                for (int x = 0; x < numFiles; x++) {
                    final File delFile = getFile(x);
                    if (delFile.exists()) {
                        delFile.delete();
                    }
                }
            } catch (final Exception ex) {
                reporter.reportFailure(ex);

            }
            reporter.report(runnableName, System.currentTimeMillis()
                    - startTime);
        }
    }

}
