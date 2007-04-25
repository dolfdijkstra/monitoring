package com.fatwire.cs.monitor.simple;

import java.util.Random;

import junit.framework.TestCase;

public class SimpleStatisticsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetStandardDeviation() {
        SimpleStatistics s = new SimpleStatistics("test");
        s.addValue(20);
        s.addValue(30);
        s.addValue(40);
        s.addValue(50);
        s.addValue(60);
        System.out.println(s.getStandardDeviation());

    }

    public void testGetStandardDeviation2() {

        SimpleStatistics s = new SimpleStatistics("test");
        Random rnd = new Random();
        for (int i = 0; i < 2000000; i++) {
            s.addValue(rnd.nextInt(50000));
        }
        System.out.println(s.getAverage());
        System.out.println(s.getStandardDeviation());

    }

}
