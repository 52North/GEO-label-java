GEO-label-java
==============

A Java implementation of the GEO label server API.

[![Build Status](https://travis-ci.org/nuest/GEO-label-java.png?branch=master)](https://travis-ci.org/nuest/GEO-label-java)
   
Modules
--------------
This project consists of a service implementation generating GEO label 
SVG representations from supplied metadata, a client API to access such a service as well as a client JSF 
component to directly render GEO labels into JSF 1/2 and JSP based webpages.


###Client

Client API to access a GEO label service. Uses a builder pattern to allow various combinations of metadata 
and feedback document inputs supporting streams, URL references and XML Documents.

```java
InputStream geoLabel = GeoLabelClientV1.createGeoLabelRequest()
							.setMetadataDocument(metadataStream)
							.setFeedbackDocument(feedbackStream)
							.getSVG();
```

###JSF

Simple JSF component rendering GEO labels from actual feedback/metadata documents and/or URL references. Supports asynchronous 
loading using Partial Page Rendering.


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

For more details see `jsf/Readme.md`

###Server

An instance is currently deployed at http://geoviqua.dev.52north.org/glbservice

For more details see `server/Readme.md`

###Commons

Resources required by server and client modules.

For more details see `commons/Readme.md`