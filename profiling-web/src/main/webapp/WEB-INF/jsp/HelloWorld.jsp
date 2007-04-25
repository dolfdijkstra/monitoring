<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>HelloWorld <c:out value="${userName}"/>.</h2>
<a href="a?action=a">HelloWorld style 1</a><br/>
<a href="a?action=a&userName=Joe">HelloWorld style 1 for Joe</a><br/>
<a href="b?action=helloWorld">HelloWorld style 2</a><br/>
<a href="b?action=helloWorld&userName=Joe">HelloWorld style 2 for Joe</a><br/>
<a href="b?action=NotFound">NotFound</a><br/>
</body>
</html>
