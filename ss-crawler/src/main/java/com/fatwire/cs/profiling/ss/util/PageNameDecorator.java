package com.fatwire.cs.profiling.ss.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import com.fatwire.cs.profiling.ss.QueryString;

public class PageNameDecorator extends TreeMap<String, List<QueryString>> {

    /**
     * 
     */
    private static final long serialVersionUID = 846247321423661559L;

    /**
     * @param c
     */
    public PageNameDecorator(final Collection<? extends QueryString> c) {
        super();
        for (final QueryString s : c) {
            String pageName = s.getParameters().get(HelperStrings.PAGENAME);
            if (pageName != null) {
                String p = "pagename=" + pageName;
                List<QueryString> l = null;
                if (containsKey(p)) {
                    l = get(p);

                } else {
                    l = new ArrayList<QueryString>();
                    put(p, l);
                }
                l.add(s);
            }
        }

    }

}
