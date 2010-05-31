package com.fatwire.cs.spring.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import COM.FutureTense.Util.ftTimedHashtable;

import com.fatwire.cs.core.cache.RuntimeCacheStats;

public class FTTimedHashtableController implements Controller {

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ModelAndView mav = new ModelAndView("cache");

        final DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        final Date now = new Date();
        final Set<String> hashes = ftTimedHashtable.getAllCacheNames();

        final Map<String, Map<String, String>> rtMap = new TreeMap<String, Map<String, String>>();
        for (final String key:hashes) {


            final ftTimedHashtable ht = ftTimedHashtable.findHash(key);
            if (ht != null) {
                final Map<String, String> statMap = new HashMap<String, String>();
                final RuntimeCacheStats stats = ht.getRuntimeStats();
                statMap.put("size", Integer.toString(ht.size()));
                statMap.put("hits", Long.toString(stats.getHits()));
                statMap.put("misses", Long.toString(stats.getMisses()));
                statMap.put("removeCount", Long
                        .toString(stats.getRemoveCount()));
                statMap.put("clearCount", Long.toString(stats.getClearCount()));
                statMap.put("capacity", Integer.toString(ht.getCapacity()));
                statMap.put("createdDate", df.format(stats.getCreatedDate()));
                statMap.put("lastPrunedDate", getTimeDiff(stats
                        .getLastPrunedDate(), now));
                statMap.put("lastFlushedDate", getTimeDiff(stats
                        .getLastFlushedDate(), now));
                statMap.put("timeOut", getLengthString(ht.getTimeout()));
                statMap.put("cacheExpiresWhenEmpty", Boolean.toString(ht
                        .getCacheExpiresWhenEmpty()));
                statMap.put("cacheItemsExpireWhenIdle", Boolean.toString(ht
                        .getCacheItemsExpireWhenIdle()));
                statMap.put("iNotifyObjects", Boolean.toString(stats
                        .hasINotifyObjects()));
                rtMap.put(key, statMap);
            }
        }
        mav.addObject("runtimeMap", rtMap);

        return mav;
    }

    String getTimeDiff(final Date first, final Date last) {
        if ((first == null) || (last == null)) {
            return "unknown";
        }
        return getTimeDiff(first.getTime(), last);
    }

    String getTimeDiff(final long first, final Date last) {
        if (last == null) {
            return "unknown";
        }
        final long diff = last.getTime() - first;
        if (diff < 60000) {
            final int sec = (int) (diff / 1000);
            return Integer.toString(sec) + " sec";
        } else {
            final int sec = (int) (diff / (1000 * 60));
            return Integer.toString(sec) + " min";
        }
    }

    String getLengthString(final long time) {
        if (time < 0) {
            return "eternal";
        }
        final int sec = (int) (time / 1000);
        return Integer.toString(sec) + " sec";
    }
}
