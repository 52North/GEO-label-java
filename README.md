# GEO-label-java

A Java implementation of the GEO label server API.

[![Build Status](https://travis-ci.org/nuest/GEO-label-java.png?branch=master)](https://travis-ci.org/52North/GEO-label-java) [![](https://images.microbadger.com/badges/version/nuest/geo-label-java.svg)](https://microbadger.com/images/nuest/geo-label-java "Get your own version badge on microbadger.com") [![](https://images.microbadger.com/badges/image/nuest/geo-label-java.svg)](https://microbadger.com/images/nuest/geo-label-java "Get your own image badge on microbadger.com")

## tl;dr

The  GEO label summarizes geospatial metadata in various formats in a nice visual image showing the availability of information on a number of facets, such as producer metadata, stand compliance, or quality information.
Take a look at the **demo server [https://geolabel.net](geolabel.net)** or run this implementation with [Docker](https://www.docker.com/):

```bash
docker build --tag geolabel-server .
docker run --rm -it -p 8080:8080 geolabel-server
```

Open landing page at http://localhost:8080/glbservice/ and query the API at http://localhost:8080/glbservice/api/v1/.

## The GEO label

The GEO label is an interactive visual summary of geospatial metadata to facilitate search and discovery use cases. Ii is a graphical representation which comprises 8 informational facets:
producer profile, lineage information, producer comments, compliance with standards, quality information, user feedback, expert reviews, and citations information. Each informational facet can have an availability states: available, not available, available only at a higher level. 
This information is encoded with symbols and colours for easy comprehensibility and repeated recognition.

Currently, the GEO label supports several metadata standards such as ISO19115 and FGDC (producer metadata documents), SensorML, as well as GeoViQua Quality Information Model (producer and user quality information).

More information: http://geolabel.info/ and http://geolabel.net/

## The GEO label API

The GEO label server is basically an API to generate a SVG document based on standardised metadata documents (e.g. XML) that can (a) be POSTed to the server, or (b) a GET Url can be build to let the server request the document and provide a permanent link to the label.

The Java implementation currently only supports the endpoint ``geolabel`` to generate labels.

The API further defines endpoints to retrieve seperate facets as well as "drilldown" informations where the service provides a visual and textual perspective (HTML) into specific aspects of a given metadata document.

Full API documentation: http://www.geolabel.net/api.html

### Transformation Rules/Mappings

The GEO label basically uses a set of XPath expressions to determine if the required information is available so that a facet can be marked as available. For interoperability reasons (using the same configuration file as the PHP implementation, and PHP only supports XPath 1.0) this Java implementation uses ``XPath 1.0``.

The transformation rules are be downloaded on startup from http://geoviqua.github.io/geolabel/ and the service has a local fallback if the online file cannot be found.

For future work, using XPath 2.0 using Saxon would be an advantage because wildcards can be used for namespaces. A commit that still contains a Saxon-based implementation is https://github.com/52North/GEO-label-java/commit/def538a4966201970a397963328664c70b2de788

## Project Modules

This project consists of a service implementation generating GEO label 
SVG representations from supplied metadata, a client API to access such a service as well as a client JSF 
component to directly render GEO labels into JSF 1/2 and JSP based webpages.

### Client

Client API to access a GEO label service. Uses a builder pattern to allow various combinations of metadata 
and feedback document inputs supporting streams, URL references and XML Documents.

```java
InputStream geoLabel = GeoLabelClientV1.createGeoLabelRequest()
							.setMetadataDocument(metadataStream)
							.setFeedbackDocument(feedbackStream)
							.getSVG();
```

### JSF

Simple JSF component rendering GEO labels from actual feedback/metadata documents and/or URL references. Supports asynchronous loading using Partial Page Rendering.


```html
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:glb="http://www.geolabel.net">
	<h:head/>
	<h:body>
		<glb:geolabel metadataUrl="#{metadataUrl}" feedbackUrl="#{feedbackUrl}"	async="true" size="100" />
	</h:body>
</html>		
```

For more details see [`jsf/Readme.md`](jsf/Readme.md).

### Server

Web application running in Servlet Containers - for more details see [`server/Readme.md`](server/Readme.md).

### Commons

Resources required by server and client modules.

For more details see [`commons/Readme.md`](commons/Readme.md).

## Development

This project is developed in [Java](https://en.wikipedia.org/wiki/Java_(programming_language)) and managed with [Maven](https://maven.apache.org/).

### Build

```bash
mvn clean install
```

### Run server locally

See [`server/README.md`](server/README.md).

### Tests

Run unit tests with

```bash
mvn test
```

### License headers and NOTICE

The service uses plugins to manage the license headers and the NOTICE file during every build.

Check the NOTICE file and license headers with `mvn notice:check` and `mvn license:check` respectively.
Update the NOTICE file with `mvn notice:generate`.
Update the license headers (based on the template in `/misc`) with `mvn license:format`.

You can also see the plugins' documentation/help pages with Maven:

```bash
mvn notice:help -Ddetail=true
mvn license:help -Ddetail=true
```

## Deployment

### Deployment with Google Cloud Run

"Cloud Run is a managed compute platform that automatically scales your stateless containers." (https://cloud.google.com/run/)

Create a new Project in Google Cloud, e.g. "geolabel-java-api" and select it (make sure that billing is enabled for the project).

Enable the Cloud Run API.

Open the Cloud Shell. 

Enter the following commands:

```bash
//clone the project from GitHub
git clone https://github.com/anikagraupner/GEO-label-java.git
cd GEO-label-java

//build the container image with the Dockerfile, geolabel-java-api is the project-id, geolabel the name of the image 
gcloud builds submit --tag  eu.gcr.io/geolabel-java-api/geolabel

//deploying to cloud run
gcloud run deploy --image  eu.gcr.io/geolabel-java-api/geolabel --platform managed
//enter a service name, e.g. geolabel
//choose a region, e.g. europe-west1-b
//respond y to allow unauthenticated invocations
//after a few seconds, the command line displays the service URL
```

More Information at https://cloud.google.com/run/docs/quickstarts/build-and-deploy .

### [WIP] Deployment with AWS Lambda

#### With Jersey

```bash
mvn notice:generate -P lambda-jar
mvn clean package -P lambda-jar
```

(re)use Jersey application! See https://aws.amazon.com/blogs/opensource/java-apis-aws-lambda/
https://github.com/awslabs/aws-serverless-java-container/tree/master/samples/jersey/pet-store

The file `lambda/target/glblambda.jar` is a "fat jar" [created with `maven-shade-plugin`](https://docs.aws.amazon.com/lambda/latest/dg/java-create-jar-pkg-maven-no-ide.html) that can be deployed as an AWS Lambda function.

#### With handler function

Use own simple handler function, see module `/lambda`

The file `lambda/target/glblambda.jar` is a "fat jar" [created with `maven-shade-plugin`](https://docs.aws.amazon.com/lambda/latest/dg/java-create-jar-pkg-maven-no-ide.html) that can be deployed as an AWS Lambda function.

The _Handler_ is the function `org.n52.geolabel.lambda.Hello::myHandler`.

- TODO: HTTP API Gateway not working yet
  - create a route `/hello`
  - attach integration `geolabel-lambda` somehow with https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-integrations.html (probably "custom" to mirror the GEO Label API) so that https://dlttwho6d0.execute-api.eu-central-1.amazonaws.com/default/hello?name=Tester works

## Contact

Daniel Nüst (d.nuest@52north.org)

Support: Metadatada management community mailing list, see http://metadata.forum.52north.org/

## License

The GEO label Java project is licensed under The Apache Software License, Version 2.0.

For licenses of used libraries see NOTICE file.
