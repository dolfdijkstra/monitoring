package com.fatwire.cs.profiling.ss.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class PageNameDecorator extends TreeMap<String, List<String>> {

    /**
     * 
     */
    private static final long serialVersionUID = 846247321423661559L;

    /**
     * @param c
     */
    public PageNameDecorator(final Collection<? extends String> c) {
        super();
        for (final String s : c) {
            String pageName = UriUtil.extractPageName(s);
            if (pageName != null) {
                String p = "pagename=" + pageName;
                List<String> l = null;
                if (containsKey(p)) {
                    l = get(p);

                } else {
                    l = new ArrayList<String>();
                    put(p, l);
                }
                l.add(s);
            }
        }

    }

}
