FROM tomcat:10.1.24-jdk17

COPY CourseRegistration.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
ENV PORT=8080

# Database configuration via environment variables
ENV DB_URL=jdbc:mysql://localhost:3306/course_registration
ENV DB_USERNAME=root
ENV DB_PASSWORD=
ENV DB_DRIVER=com.mysql.cj.jdbc.Driver

CMD ["catalina.sh", "run"]