FROM maven:latest as build
RUN mkdir -p /var/www/app
ADD . /var/www/app
WORKDIR /var/www/app
RUN mvn clean install

FROM tomcat:8-jdk8-openjdk
COPY --from=build /var/www/app/server/target/glbservice.war /usr/local/tomcat/webapps
COPY --from=build /var/www/app/misc/tomcat-users.xml /usr/local/tomcat/conf
EXPOSE 8080
CMD ["catalina.sh","run"]

