FROM tomcat:8.5-alpine
VOLUME /tmp
COPY target/airline-0.0.1.war /usr/local/tomcat/webapps/airline-0.0.1.war
RUN sh -c 'touch /usr/local/tomcat/webapps/airline-0.0.1.war'
ENTRYPOINT [ "sh", "-c", "java -jar /usr/local/tomcat/webapps/airline-0.0.1.war"]
