package com.fatwire.gst.web.servlet.monitor.event.commons.math;

import com.fatwire.gst.web.servlet.monitor.commons.math.SummaryStatisticsAdapter;

import junit.framework.TestCase;

public class SummaryStatisticsAdapterTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetStandardDeviation() {
        SummaryStatisticsAdapter s = new SummaryStatisticsAdapter("test");
        s.addValue(20);
        s.addValue(30);
        s.addValue(40);
        s.addValue(50);
        s.addValue(60);
        assertEquals(15.811388300841896D, s.getStandardDeviation());
    }
    public void testLargeValues() {
        SummaryStatisticsAdapter s = new SummaryStatisticsAdapter("test");
        s.addValue(Long.MAX_VALUE);
        s.addValue(Long.MAX_VALUE);
        s.addValue(Long.MIN_VALUE);
        s.addValue(Long.MIN_VALUE);

        assertEquals(1.0650232656628343E19, s.getStandardDeviation());
    }

}
