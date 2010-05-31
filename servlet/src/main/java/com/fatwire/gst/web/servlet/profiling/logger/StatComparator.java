/**
 * 
 */
package com.fatwire.gst.web.servlet.profiling.logger;

import java.util.Comparator;

class StatComparator implements Comparator<Stat> {

    static String[] t = new String[] { "select", "update", "insert", "delete" };

    public int compare(Stat m, Stat f) {
        int c1 = code(m);
        int c2 = code(f);
        if (c1 < 6) {
            return c1 - c2;
        }
        if (c2 < 6) {
            return 1;
        }
        if (c1 != c2) {
            return c1 - c2;
        }
        if (m.getSubType() == null)
            return -1;
        if (f.getSubType() == null)
            return 1;
        //by now we are sql with unknown subtype, or element

        return m.getSubType().compareTo(f.getSubType());

    }

    private int code(Stat m) {
        if ("page".equals(m.getType())) {
            return 1;
        } else if ("sql".equals(m.getType())) {
            for (int i = 0; i < t.length; i++) {
                if (t[i].equals(m.getSubType())) {
                    return 2 + i;
                }
            }
            return 6;
        } else if ("element".equals(m.getType())) {
            return 7;
        } else {
            return 8;
        }

    }
}