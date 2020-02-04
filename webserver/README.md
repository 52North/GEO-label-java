# GEO-label-java Webserver

## Build Profiles

This server project provides two build profiles

 * `webserver (default)`

   Includes basic JSF 2.0 implementation (mojarra) in output archive
 * `appserver`

   Profile will not include JSF implementation dependencies as these are part of JEE application servers

## Installation

Clone the project from GitHub, build it with Maven, then deploy the created .war file to a servlet container of your choice:

```
git clone https://github.com/52North/GEO-label-java.git
cd GEO-label-java
maven clean install
```

## Run

### Jetty Runner

```bash
mvn package
java -jar target/dependency/jetty-runner.jar server/target/*.war
```

Open http://localhost:8080/ for the UI or directly access the API endpoint at http://localhost:8080/api/.

A [SoapUI](https://www.soapui.org/downloads/soapui.html) (use the Open Source one!) test project is at `misc/GEO-label-soapui-project.xml` with some example requests.

### Run in container

**Tomcat**

```bash
docker run --rm -it -v $(pwd)/server/target/glbservice.war:/usr/local/tomcat/webapps/glbservice.war -v $(pwd)/misc/tomcat-users.xml:/usr/local/tomcat/conf/tomcat-users.xml -p 8080:8080 tomcat:8-jdk8-openjdk
```

Or for Windows CMD:

```bash
docker run --rm -it -v "%cd%"/server/target/glbservice.war:/usr/local/tomcat/webapps/glbservice.war -v "%cd%"/misc/tomcat-users.xml:/usr/local/tomcat/conf/tomcat-users.xml -p 8080:8080 tomcat:8-jdk8-openjdk
```

Open http://localhost:8080/glbservice/

**Jetty**

```bash
docker run --rm -it -v $(pwd)/server/target/glbservice:/var/lib/jetty/webapps/glbservice -p 8080:8080 jetty:jre8
```

Landing page does not work but API is at http://localhost:8080/glbservice/api/

## Metadata Transformation Mappings

Transformation Mappings specify xpath expressions to map from supplied metadata to GEO label facet information. 
They are placed in `src/main/resources/transformations` directory. Find more information in the provided example mapping files.

## Configuration

The server uses one main configuration file `src/main/resources/server.properties`. Properties are overwritten by properties with the same name in a file called `org.n52.geolabel.server.properties` in the home directory of the user executing the servlet container.

## XPathFactory and TransformerFactory

The implmentations for the `XPathFactory` to be used is done via service loader configuration files in `\GEO-label-java\server\src\main\resources\META-INF\services` and is pre-configured to work with an Oracle JRE using the included Apache Xalan implementation (`com.sun.org.apache.xalan`). If you want to use a differen XPath implementation, change these configuration files.
