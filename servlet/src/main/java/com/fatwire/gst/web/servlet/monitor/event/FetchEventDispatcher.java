package com.fatwire.gst.web.servlet.monitor.event;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FetchEventDispatcher implements FetchEventListener {
    private static final FetchEventDispatcher self = new FetchEventDispatcher();

    private final Set<FetchEventListener> listeners = new CopyOnWriteArraySet<FetchEventListener>();

    public static void dispatch(final FetchEvent event) {
        getInstance().fetchPerformed(event);
    }

    public void fetchPerformed(final FetchEvent event) {
        for (final Iterator<FetchEventListener> itor = listeners.iterator(); itor.hasNext();) {
            itor.next().fetchPerformed(event);
        }

    }

    public void addFetchEventListener(final FetchEventListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);

    }

    public void removeFetchEventListener(final FetchEventListener listener) {
        listeners.remove(listener);

    }

    public static FetchEventDispatcher getInstance() {
        return FetchEventDispatcher.self;
    }

}
