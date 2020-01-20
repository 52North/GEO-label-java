FROM maven:latest as build
RUN mkdir -p /var/www/app
COPY . /build
WORKDIR /build
RUN mvn clean install -DskipTests

FROM tomcat:8-jdk8-openjdk
COPY --from=build /build/server/target/glbservice.war /usr/local/tomcat/webapps
COPY --from=build /build/misc/tomcat-users.xml /usr/local/tomcat/conf
EXPOSE 8080
CMD ["catalina.sh","run"]

LABEL maintainer="Daniel Nüst <daniel.nuest@uni-muenster.de>, Anika Graupner" \
  org.label-schema.url="https://geolabel.net" \
  org.label-schema.name="GEO-label-java" \
  org.label-schema.description="Java implementation of the GeoViQua GEO Label API" \
  org.label-schema.version=$VERSION \
  org.label-schema.vcs-url=$VCS_URL \
  org.label-schema.vcs-ref=$VCS_REF \
  org.label-schema.build-date=$BUILD_DATE \
  org.label-schema.docker.schema-version="rc1"
