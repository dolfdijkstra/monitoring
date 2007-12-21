package com.fatwire.cs.profiling.ss.reporting;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.events.PageletRenderedEvent;
import com.fatwire.cs.profiling.ss.events.PageletRenderingListener;
import com.fatwire.cs.profiling.ss.jobs.JobFinishedEvent;
import com.fatwire.cs.profiling.ss.jobs.JobChangeListener;
import com.fatwire.cs.profiling.ss.jobs.JobScheduledEvent;
import com.fatwire.cs.profiling.ss.jobs.JobStartedEvent;

public class ReportingListener implements PageletRenderingListener, JobChangeListener {
    private final Log log = LogFactory.getLog(getClass());

    private final List<Reporter> reporters = new CopyOnWriteArrayList<Reporter>();

    public void addReporter(Reporter reporter) {
        reporters.add(reporter);
    }

    public void renderPerformed(PageletRenderedEvent event) {
        final ResultPage page = event.getPage();
        for (Iterator<Reporter> i = reporters.iterator(); i.hasNext();) {
            Reporter reporter = i.next();
            try {
                reporter.addToReport(page);
            } catch (Throwable e) {
                log.error(e, e);
                //i.remove(); //remove reporter if we can't use it
            }
        }

    }

    public void jobFinished(JobFinishedEvent event) {
        for (Reporter reporter : reporters) {
            try {
                reporter.endCollecting();
            } catch (Throwable e) {
                log.error(e, e);
            }
        }

    }

    public void jobScheduled(JobScheduledEvent event) {

    }

    public void jobStarted(JobStartedEvent event) {
        for (Iterator<Reporter> i = reporters.iterator(); i.hasNext();) {
            Reporter reporter = i.next();
            try {
                reporter.startCollecting();
            } catch (Throwable e) {
                log.error(e, e);
                i.remove(); //remove reporter if we can't use it
            }
        }

    }

}
