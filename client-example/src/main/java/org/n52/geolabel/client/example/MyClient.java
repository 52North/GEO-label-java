/**
 * Copyright 2013 52°North Initiative for Geospatial Open Source Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.n52.geolabel.client.example;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.n52.geolabel.client.GeoLabelClientV1;
import org.n52.geolabel.client.GeoLabelRequestBuilder;

public class MyClient {

    public static void main(String[] args) throws IOException {
        // create a new (empty) request builder
        GeoLabelRequestBuilder builder = GeoLabelClientV1.createGeoLabelRequest();

        // find out a metadata URL and generate a label for it...
        String metadataUrl = "http://www.geoportal.org/geoportal/vrd?sourceType=USGSGN&uuid=NASA_LIS_DATA";
        builder = builder.setDesiredSize(200).setMetadataDocument(metadataUrl);

        InputStream svg = builder.getSVG();
        String svgString = IOUtils.toString(svg);
        System.out.println(svgString);

        // set another rendering server
        svgString = IOUtils.toString(GeoLabelClientV1.createGeoLabelRequest("http://geoviqua.dev.52north.org/glbservice/api/v1/svg/").setMetadataDocument(metadataUrl).getSVG());
        System.out.println(svgString);

        // display the file in a browser
        File tempFile = File.createTempFile("geolabel-", ".svg");
        FileUtils.writeStringToFile(tempFile, svgString);
        // open the default web browser for the HTML page
        Desktop.getDesktop().browse(tempFile.toURI());
        // if a web browser is the default HTML handler, this might work too
        // Desktop.getDesktop().open(htmlFile);
    }

}