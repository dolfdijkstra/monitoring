package com.fatwire.cs.monitor.event;


public interface FetchEventListener extends PerformanceEventListener {

    void fetchPerformed(FetchEvent event);

}
