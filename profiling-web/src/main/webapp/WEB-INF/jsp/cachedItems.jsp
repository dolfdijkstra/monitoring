<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%//
// Support/CacheManager/RS/CachedItems
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.FTValList" %>
<%@ page import="COM.FutureTense.Interfaces.ICS" %>
<%@ page import="COM.FutureTense.Interfaces.IList" %>
<%@ page import="COM.FutureTense.Interfaces.Utilities" %>
<%@ page import="COM.FutureTense.Util.ftErrors" %>
<%@ page import="COM.FutureTense.Util.ftMessage"%>
<%@ page import="COM.FutureTense.Util.ftTimedHashtable"%>
<%@ page import="com.fatwire.cs.core.cache.RuntimeCacheStats" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%! 
    String getTimeDiff(Date first, Date last){
        if (first == null || last == null) return "unknown";
        return getTimeDiff(first.getTime(),last);
    }
    String getTimeDiff(long first, Date last){
        if (last == null) return "unknown";
        long diff = last.getTime() - first;
        int sec = (int)(diff /1000);
        return Integer.toString(sec) +" s";
    }
    String getLengthString(long time){
        if (time < 0) return "eternal";
        int sec = (int)(time);// /1000);
        return Integer.toString(sec) +" s";
    }
%>
<cs:ftcs>
<ics:callelement element="Support/stylecss"/>
<ics:callelement element="Support/CacheManager/Header"/>
<h2><center>Resultset Cache Profiler</center></h2>
    <table class="altClass">
    <%
        DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        Date now = new Date();
        String key = ics.GetVar("key");
        ftTimedHashtable ht = ftTimedHashtable.findHash(key);
        if (ht != null) {  %>
        <tr>
            <th>Nr</th>
            <th>Name</th>
            <th>Size</th>
            <th>Hits</th>
            <th>Misses</th>
            <th>RemovedNum</th>
            <th>ClearNum</th>
            <th>MaxCount</th>
            <th>CreatedDate</th>
            <th>LastChanged</th>
            <th>LastFlush</th>
            <th>Lifetime</th>
            <th>ExpireWhenEmpty</th>
            <th>ItemsExpireWhenEmpty</th>
            <th>INotifyObjects</th>
        </tr>
        <tr>
        <td>&nbsp;</td><%
            RuntimeCacheStats stats = ht.getRuntimeStats();

            %><td nowrap="true"><%= key %></td><%
            %><td><%= ht.size() %></td><%
            %><td style="text-align:right"><%= stats.getHits()%></td><%
            %><td style="text-align:right"><%= stats.getMisses()%></td><%
            %><td style="text-align:right"><%= stats.getRemoveCount()%></td><%
            %><td style="text-align:right"><%= stats.getClearCount()%></td><%
            %><td style="text-align:right"><%= ht.getCapacity() %></td><%
            %><td nowrap="true"><%= df.format(stats.getCreatedDate())%></td><%
            %><td style="text-align:right"><%= getTimeDiff(stats.getLastPrunedDate(), now)%></td><%
            %><td style="text-align:right"><%= getTimeDiff(stats.getLastFlushedDate(),now)%></td><%
            %><td nowrap="true" style="text-align:right"><%= getLengthString(ht.getTimeout())%></td><%
            %><td style="text-align:right"><%= ht.getCacheExpiresWhenEmpty()%></td><%
            %><td style="text-align:right"><%= ht.getCacheItemsExpireWhenIdle()%></td><%
            %><td style="text-align:right"><%= stats.hasINotifyObjects() %></td><%

            int i=1;
            for (Enumeration keys = ht.keys(); keys.hasMoreElements();){
                String itemkey = (String)keys.nextElement();
                %><tr><td><%= Integer.toString(i++) %></td><%
                %><td colspan="14"><%= itemkey %></td><%
                %></tr>
            <% }  
        } else {
            %><tr><td colspan="16"><%= key %> not found</td></tr><%
        }
         %>
    </table>
<ics:callelement element="Support/Footer"/>
</cs:ftcs>
