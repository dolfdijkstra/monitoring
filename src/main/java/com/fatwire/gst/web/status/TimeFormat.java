package com.fatwire.gst.web.status;

public class TimeFormat {
    static final long MILLI = 1000000L;

    static final long SECOND = 1000L * MILLI;

    static final long MINUTE = 60 * TimeFormat.SECOND;

    static final long MS_SECOND = 1000L;

    static final long MS_MINUTE = 60 * TimeFormat.MS_SECOND;

    public String format(final long t) {
        final int ms = (int) (t / TimeFormat.MILLI);

        if (t > TimeFormat.MINUTE) {
            final long m = t / TimeFormat.MINUTE;
            final long s = (t / TimeFormat.SECOND) - (m * 60);
            final StringBuilder b = new StringBuilder();
            b.append(m).append("m ");
            pad2(b, s);
            b.append("s");
            return b.toString();
        } else if (t > TimeFormat.SECOND) {
            final StringBuilder b = new StringBuilder();
            pad2(b, ms / 1000);
            b.append("s ");

            final int rms = (ms % 1000);
            pad3(b, rms);
            b.append("ms");
            return b.toString();
        } else if (t < TimeFormat.MILLI) {
            final StringBuilder b = new StringBuilder();
            pad3(b, (int) (t / 1000));
            b.append("us");
            return b.toString();
        } else {
            final StringBuilder b = new StringBuilder();
            pad3(b, ms);
            b.append("ms");
            return b.toString();

        }
    }

    public String formatMilli(final long t) {
        int ms = (int) t;

        if (t > TimeFormat.MS_MINUTE) {
            final long m = t / TimeFormat.MS_MINUTE;
            final long s = (t / TimeFormat.MS_SECOND) - (m * 60);
            final StringBuilder b = new StringBuilder();
            b.append(m).append("m ");
            pad2(b, s);
            b.append("s");
            return b.toString();
        } else if (t > TimeFormat.MS_SECOND) {
            final StringBuilder b = new StringBuilder();
            pad2(b, ms / 1000);
            b.append("s ");

            final int rms = (ms % 1000);
            pad3(b, rms);
            b.append("ms");
            return b.toString();
        } else if (t < 1000) {
            final StringBuilder b = new StringBuilder();
            pad3(b, (int) (t / 1000));
            b.append("us");
            return b.toString();
        } else {
            final StringBuilder b = new StringBuilder();
            pad3(b, ms);
            b.append("ms");
            return b.toString();

        }
    }

    private void pad3(final StringBuilder b, final long v) {
        if (v < 10) {
            b.append("00");
        } else if (v < 100) {
            b.append("0");
        }
        b.append(v);

    }

    private void pad2(final StringBuilder b, final long v) {
        if (v < 10) {
            b.append("0");
        }
        b.append(v);

    }

}
