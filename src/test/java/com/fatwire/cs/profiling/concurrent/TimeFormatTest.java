package com.fatwire.cs.profiling.concurrent;

import com.fatwire.gst.web.status.TimeFormat;

import junit.framework.TestCase;

public class TimeFormatTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMinutes() {
        TimeFormat tf = new TimeFormat();

        String x = tf.format((2 * 60000L + 30 * 1000) * 1000000);
        assertEquals("2m 30s",x);
    }

    public void testSeconds() {
        TimeFormat tf = new TimeFormat();

        String x = tf.format((30 * 1001L) * 1000000);
        assertEquals("30s 030ms",x);
    }

    public void testMilliSeconds() {
        TimeFormat tf = new TimeFormat();

        String x = tf.format(301 * 1000000L);
        assertEquals("301ms",x);
    }

    public void testMicroSeconds() {
        TimeFormat tf = new TimeFormat();

        String x = tf.format(30 * 1000L);
        assertEquals("030us",x);
    }

}
