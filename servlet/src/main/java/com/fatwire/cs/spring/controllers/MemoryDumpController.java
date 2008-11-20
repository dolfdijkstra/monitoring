package com.fatwire.cs.spring.controllers;

import java.io.PrintWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

public class MemoryDumpController implements Controller {

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        return mav;
    }

    ModelAndView mav = new ModelAndView(new View() {

        public String getContentType() {
            return "text/html";
        }

        public void render(Map model, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            final PrintWriter writer = response.getWriter();
            writer
                    .write("<html><head><title>Memory Overview</title><head><body><pre>");

            renderBody(writer);
            writer.write("</pre></body></html>");

        }

        protected void renderBody(final PrintWriter pw) {
            try {
                pw.println("\nDUMPING MEMORY INFO\n");
                // Read MemoryMXBean
                final MemoryMXBean memorymbean = ManagementFactory
                        .getMemoryMXBean();

                pw.println("Heap Memory Usage: "
                        + memorymbean.getHeapMemoryUsage());
                pw.println("Non-Heap Memory Usage: "
                        + memorymbean.getNonHeapMemoryUsage());

                // Read Garbage Collection information
                final List<GarbageCollectorMXBean> gcmbeans = ManagementFactory
                        .getGarbageCollectorMXBeans();
                for (final GarbageCollectorMXBean gcmbean : gcmbeans) {
                    pw.println();
                    pw.println("Collection Name: " + gcmbean.getName());
                    pw.println("Collection count: "
                            + gcmbean.getCollectionCount());
                    pw.println("Collection time: "
                            + gcmbean.getCollectionTime());
                    pw.println("Memory Pools: ");
                    final String[] memoryPoolNames = gcmbean
                            .getMemoryPoolNames();
                    for (int i = 0; i < memoryPoolNames.length; i++) {
                        pw.println("\t" + memoryPoolNames[i]);
                    }
                }

                // Read Memory Pool Information
                pw.println("Memory Pools Info");
                final List<MemoryPoolMXBean> mempoolsmbeans = ManagementFactory
                        .getMemoryPoolMXBeans();
                for (final MemoryPoolMXBean mempoolmbean : mempoolsmbeans) {
                    pw.println("\nName: " + mempoolmbean.getName());
                    pw.println("Usage: " + mempoolmbean.getUsage());
                    pw.println("Collection Usage: "
                            + mempoolmbean.getCollectionUsage());
                    pw.println("Peak Usage: " + mempoolmbean.getPeakUsage());
                    pw.println("Type: " + mempoolmbean.getType());
                    pw.println("Memory Manager Names: ");
                    final String[] memManagerNames = mempoolmbean
                            .getMemoryManagerNames();
                    for (int i = 0; i < memManagerNames.length; i++) {
                        pw.println("\t" + memManagerNames[i]);
                    }
                    pw.println("\n");
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    });

}
