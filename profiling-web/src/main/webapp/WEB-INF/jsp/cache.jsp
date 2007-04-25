<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Resultset Cache Profiler</title>

<style type="text/css">
/*<![CDATA[*/
 li.c2 {list-style: none}
 div.title {text-align: center}
 th {font-size: 10pt}
 td {white-space: nowrap; font-size: 10pt}
 td.number {text-align: right}
  
/*]]>*/
</style>
</head>
<body>
<div class="title">
<h2>Resultset Cache Profiler</h2>
</div>
<div id="bdwrap">
<div id="bdcontent">
    <table class="altClass">
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
<c:forEach var="stat" items="${runtimeMap}" varStatus="status">
        
        
        <tr><td><c:out value='${status.index}'/></td><%
        %><td><a href="<c:url value='/content/sitemap.jsp?${stat.key}'/>"><c:out value="${stat.key}"/></a></td>
        
            %><td><c:out value="${stat.value.size}"/></td><%
            %><td class="number"><c:out value="${stat.value.hits}"/></td><%
            %><td class="number"><c:out value="${stat.value.misses}"/></td><%
            %><td class="number"><c:out value="${stat.value.removeNum}"/></td><%
            %><td class="number"><c:out value="${stat.value.clearCount}"/></td><%
            %><td class="number"><c:out value="${stat.value.createdDate}"/></td><%
            %><td nowrap="true"><c:out value="${stat.value.createdDate}"/></td><%
            %><td class="number"><c:out value="${stat.value.lastPrunedDate}"/></td><%
            %><td class="number"><c:out value="${stat.value.lastFlushedDate}"/></td><%
            %><td nowrap="true" class="number"><c:out value="${stat.value.timeOut}"/></td><%
            %><td class="number"><c:out value="${stat.value.cacheExpiresWhenEmpty}"/></td><%
            %><td class="number"><c:out value="${stat.value.cacheItemsExpireWhenIdle}"/></td><%
            %><td class="number"><c:out value="${stat.value.iNotifyObjects}"/></td>
</tr>
</c:forEach>

    </table>
</div>
</div>
<hr/>
<ul id="navbar">
    <li><b>Name: </b>name of the key of the table</li>
    <li><b>Size: </b>number of entries in the cache for this table</li>
    <li><b>Hits: </b>number of times a cached object is requested from cache and was in cache</li>
    <li><b>Misses: </b>number of times a cached object is requested from cache and was NOT in cache</li>
    <li><b>RemovedNum: </b>number of items removed from cache, either by time based expiration, LRU or because an item was removed due to IList.flush() for instance.</li>
    <li><b>ClearNum: </b>number of times the who table is cleared. The happens on any update on the table or by catalog.flush().</li>
    <li><b>MaxCount: </b>maximum number of items in the cache. This seems not a hard limit because I have seen BlobServer go above it's max. (cc.$tablename$CSz)</li>
    <li><b>LinkedTables: </b>number of linked tables to the table. Any flush of this table will trigger a flush on other tables too.</li>
    <li><b>IdleCount: </b>number of times this table is checked for idleness and was found idle. After two times this check an idle table will be removed from 'cache'.</li>
    <li><b>CreatedDate: </b>time ftTimedhashtable was created. </li>
    <li><b>LastChanged: </b>last time this table was changed. I doubt that this value is correct or meaning full</li>
    <li><b>LastFlush: </b>last time this table was flush. I doubt that this value is correct or meaningfull.</li>
    <li><b>Lifetime: </b>lifetime of the items in cache (cc.$tablename$Timeout)</li>
    <li><b>ExpireWhenEmpty: </b>should this ftTimedHashtable be removed from 'cache' if it is idle (for two times as explained above).</li>
    <li><b>Idle: </b>is it idle</li>
    <li><b>INotifyObjects: </b>does it have INotifyObjects. INotifyObjects are object that need to notify other objects when they expire.</li>
    <br/>
    
    <li>To use this tool the most interesting numbers are<br/>
    1) A high number of ClearNum. This means a lot of flushes of the table<br/>
    2) Hits compared to misses and hit count in total.<br/>
    3) Size compared to MaxSize. If size is at maxsize and remove num is growing you might want to enlarge the cache size in futuretense.ini.</li>
</ul>
</body>
</html>
