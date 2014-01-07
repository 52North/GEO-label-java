GEO-label-java server
======================

## Build Profiles

This server project provides two build profiles

 * appserver

   Profile will not include JSF implementation dependencies as these are part of JEE application servers
 * webserver (default)

   Includes basic JSF 2.0 implementation (mojarra) in output archive
 * swagger
 
   Build API documentation with swagger. Run it with `mvn clean install -Pswagger`.
   
   
## Metadata Transformation Mappings

Transformation Mappings specify xpath expressions to map from supplied metadata to GEO label facet information. 
They are placed in `src/main/resources/transformations` directory. Find more information in the provided example mapping files.

## Configuration

The server uses one main configuration file `src/main/resources/server.properties`. Properties are overwritten by properties with the same name in a file called `org.n52.geolabel.server.properties` in the home directory of the user executing the servlet container.