package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.gst.web.servlet.profiling.Hierarchy;
import com.fatwire.gst.web.servlet.profiling.Measurement;
import com.fatwire.gst.web.servlet.profiling.support.MeasurementImpl;

public class ResponseTimeFilter extends HttpFilter implements Filter {
    public static final String INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";

    public static final String INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";

    public static final String INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";

    public static final String TEMP_PATH = "javax.servlet.context.tempdir";

    protected String[] specialParams;

    protected String filenamePrefix = "responsetimes-";

    protected String filenameDateFormat = "yyyyMMdd-HHmmss";

    protected long periodLength = 1000L;

    protected long period;

    private final ConcurrentLinkedQueue<ResponseTimeEvent> queue = new ConcurrentLinkedQueue<ResponseTimeEvent>();

    private Timer timer;

    private String namefile;

    private final SimpleDateFormat fmt = new SimpleDateFormat(
            filenameDateFormat);

    private ServletContext context;

    private File tempDir;

    long nextRoll;

    long linesInFile = 0;

    private final ThreadLocal<Nesting> local = new ThreadLocal<Nesting>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected Nesting initialValue() {
            return new Nesting();
        }

    };

    @Override
    public void destroy() {
        drain();
        timer.cancel();
        timer = null;
        context = null;

    }

    @Override
    protected void doFilter(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final Hierarchy name = getName(request);

        final Measurement t = new MeasurementImpl(name);
        final int level = local.get().start();
        try {

            chain.doFilter(request, response);
        } finally {
            final Nesting n = local.get();
            n.stop();

            t.stop();
            final ResponseTimeEvent e = new ResponseTimeEvent(period, t
                    .getStartTime(), name, t.elapsed() / 1000, level,
                    n.counter, request.getRequestedSessionId(), request
                            .getRemoteAddr(), request.getRemotePort());

            dispatchEvent(e);

        }
    }

    protected void dispatchEvent(final ResponseTimeEvent e) {
        queue.add(e);
    }

    Hierarchy getName(final HttpServletRequest request) {
        Hierarchy name = new Hierarchy("request");
        name = name.build(request.getMethod());
        name = name.build(request.getRequestURI());
        for (int i = 0; i < specialParams.length; i++) {
            final String special = specialParams[i];
            final String value = request.getParameter(special);
            if (value != null) {
                name = name.build(special + "=" + value);
            }
        }
        if (request.getAttribute(INCLUDE_REQUEST_URI) != null) {
            name = name.build(String.valueOf(request
                    .getAttribute(INCLUDE_REQUEST_URI)));
        }

        return name;

    }

    /**
     * Look for a semicolon seperated initParameter called specialParams to add
     * these http request parameters for inclusion in the StopWatch name
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        context = filterConfig.getServletContext();
        tempDir = new File(String.valueOf(context.getAttribute(TEMP_PATH)));
        final String sp = filterConfig.getInitParameter("specialParams");
        if (sp != null) {
            specialParams = sp.split(";");
        } else {
            specialParams = new String[] { "pagename", "blobtable" };
        }
        roll();
        final TimerTask task = new TimerTask() {

            @Override
            public void run() {
                drain();

            }

        };

        timer = new Timer("ResponseTimeLogger", true);
        timer.scheduleAtFixedRate(task, 0, periodLength);
    }

    /**
     * the consumer operation
     * 
     */
    void drain() {
        period++;
        int s = queue.size();
        if (s > 0) {
            if (linesInFile > 1000000) {
                roll();
            } else if (System.currentTimeMillis() > this.nextRoll) {
                roll();
            }
            Writer w = null;
            try {
                w = new FileWriter(new File(tempDir, namefile), true);
                while (s > 0) {
                    final ResponseTimeEvent e = queue.poll();
                    if (e == null) {
                        break;
                    }
                    s--;
                    w.write(Long.toString(e.period));
                    w.write('\t');
                    w.write(e.name.toString());
                    w.write('\t');
                    w.write(Long.toString(e.elapsed));
                    w.write('\t');
                    w.write(Long.toString(e.level));
                    w.write('\t');
                    w.write(Long.toString(e.counter));
                    w.write('\n');
                    linesInFile++;
                }
            } catch (final IOException e1) {
                log.error(e1 + " on " + namefile);
            } finally {
                if (w != null) {
                    try {
                        w.close();
                    } catch (final IOException e) {
                        //ignore
                    }
                }
            }

        }
    }

    void roll() {
        linesInFile = 0;
        requestCounter.set(0);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        nextRoll = cal.getTimeInMillis();

        namefile = filenamePrefix + fmt.format(new Date()) + ".log";
        Writer w = null;
        try {
            w = new FileWriter(new File(tempDir, namefile), true);
            w.write("period");
            w.write('\t');
            w.write("name");
            w.write('\t');
            w.write("elapsed");
            w.write('\t');
            w.write("nesting");
            w.write('\t');
            w.write("counter");
            w.write('\n');
        } catch (final IOException e1) {
            log.error(e1 + " on " + namefile);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (final IOException e) {
                    //ignore
                }
            }
        }

    }

    private final AtomicLong requestCounter = new AtomicLong();

    class Nesting {
        int level = 0;

        long counter = 0;

        int start() {
            if (level == 0) {
                counter = requestCounter.incrementAndGet();
            }
            level++;
            return level;
        }

        void stop() {
            level--;
        }
    }

}

class ResponseTimeEvent {
    final long period;

    final long startTime;

    final Hierarchy name;

    final long elapsed;

    final int level;

    final long counter;

    final String sessionid;

    final String remoteIP;

    final int remoteport;

    /**
     * @param period
     * @param startTime
     * @param name
     * @param elapsed
     * @param remotePort 
     * @param remoteIP 
     * @param sessionid 
     */
    public ResponseTimeEvent(final long period, final long startTime,
            final Hierarchy name, final long elapsed, final int level,
            final long counter, final String sessionid, final String remoteIP,
            final int remotePort) {
        super();
        this.period = period;
        this.startTime = startTime;
        this.name = name;
        this.elapsed = elapsed;
        this.level = level;
        this.counter = counter;
        this.sessionid = sessionid;
        this.remoteIP = remoteIP;
        this.remoteport = remotePort;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder(Long.toString(period)).append('-')
                .append(name).append('-').append(elapsed).toString();

    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (counter ^ counter >>> 32);
        result = prime * result + (int) (elapsed ^ elapsed >>> 32);
        result = prime * result + level;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (int) (period ^ period >>> 32);
        result = prime * result + (int) (startTime ^ startTime >>> 32);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ResponseTimeEvent)) {
            return false;
        }
        final ResponseTimeEvent other = (ResponseTimeEvent) obj;
        if (counter != other.counter) {
            return false;
        }
        if (elapsed != other.elapsed) {
            return false;
        }
        if (level != other.level) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (period != other.period) {
            return false;
        }
        if (startTime != other.startTime) {
            return false;
        }
        return true;
    }

}