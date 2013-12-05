GEO-label-java Client
=====================

A client implementation for the GEO label API.

## Include Dependency in Maven

To use the GEO label client in your own Java application you can add it as a maven dependency. You must include the 52°North repo as well.

```
<dependencies>
    <dependency>
        <groupId>org.n52.geolabel</groupId>
        <artifactId>client</artifactId>
        <version>0.0.2</version>
    </dependency>

[...]

<repositories>
	<repository>
        <id>n52-releases</id>
        <name>52n Releases</name>
        <url>http://52north.org/maven/repo/releases</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>n52-snapshots</id>
        <name>52n Snapshots</name>
        <url>http://52north.org/maven/repo/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

```

## Using the client

```
GeoLabelClientV1.createGeoLabelRequest().setMetadataDocument(metadataUrl).setUseCache(true).getSVG();

InputStream svg = GeoLabelClientV1.createGeoLabelRequest().setMetadataDocument(metadataUrl)
				.setFeedbackDocument(feedbackStream).getSVG();
// org.apache.commons.io.IOUtils
String svgString = IOUtils.toString(svg);
		
```