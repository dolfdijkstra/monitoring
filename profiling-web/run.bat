@echo off
set MAVEN_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
rem mvn -Dslf4j=false -Dlog4j.configuration=file:./src/main/webapp/WEB-INF/log4j.xml jetty:run
mvn jetty:run