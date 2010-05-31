package com.fatwire.gst.web.servlet.monitor;

public interface StopWatch {
    
    public class Mark{
        private final String name;
        private final long elapsed;
        /**
         * @param name
         * @param elapsed
         */
        public Mark(String name, long elapsed) {
            super();
            this.name = name;
            this.elapsed = elapsed;
        }
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * @return the elapsed
         */
        public long getElapsed() {
            return elapsed;
        }
        
    }

    public void start();

    public void stop();

    /**
     * 
     * 
     * @return a long representing the time elapsed between start and stop in milliseconds
     */

    public long getTimeElapsedInMillis();

    /**
     * set a named mark
     * @param name the name of the mark
     */
    public void mark(String name);

    public Mark[] getMarks();

}
