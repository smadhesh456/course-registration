FROM tomcat:10.1.24-jdk17

COPY deploy/CourseRegistration.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
ENV PORT=8080

CMD ["catalina.sh", "run"]