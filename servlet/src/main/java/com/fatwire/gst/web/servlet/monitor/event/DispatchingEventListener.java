package com.fatwire.gst.web.servlet.monitor.event;

import java.util.Set;

/**
 * Forwards the event to a set of listeners 
 * 
 * @author Dolf.Dijkstra
 * @since 23-apr-2007
 */
public class DispatchingEventListener implements FetchEventListener {

    private Set<FetchEventListener> listeners;

    public void fetchPerformed(final FetchEvent event) {
        for (final FetchEventListener listener : listeners) {
            listener.fetchPerformed(event);
        }

    }

    /**
     * @param listeners the listeners to set
     */
    public void setListeners(final Set<FetchEventListener> listeners) {
        this.listeners = listeners;
    }

}
