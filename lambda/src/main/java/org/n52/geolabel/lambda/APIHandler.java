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
package org.n52.geolabel.lambda;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.*;
import java.net.URL;

import org.apache.commons.io.*;
import java.net.URLConnection;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.GeoLabelObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.mapping.MetadataTransformer;


/**
 * @author Daniel Nüst
 */
public class APIHandler implements RequestStreamHandler {

    private JSONParser parser = new JSONParser();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        // new Label 
        Label label = new Label();

        // file for the filereader to process label to svg
        File f = File.createTempFile("geolabel_", ".svg");

        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            // JSONObject responseBody = new JSONObject();
            String responseBody = "version : v1 , svg-label-generation : ...api/v1/svg";

            JSONObject pathParams = new JSONObject();
            JSONObject headerJson = new JSONObject();

            if (event.get("pathParameters") != null) {
                pathParams = (JSONObject) event.get("pathParameters");
                context.getLogger().log(String.format("Path parameters: %s", pathParams));
                responseBody = responseBody + "\n" + pathParams.toString();
            }

            if (event.get("queryStringParameters") != null) {
                JSONObject queryParams = (JSONObject) event.get("queryStringParameters");
                context.getLogger().log(String.format("Query string parameters: %s", queryParams));
                responseBody = responseBody + "\n" + queryParams.toString();
            }

            // query parameters 
            JSONObject queryParams = (JSONObject) event.get("queryStringParameters");

            // do only if a metadata or a feedback url is defined at the endpoint api/v1/svg
            if (pathParams.values().contains("api/v1/svg") && (queryParams.get("metadata") != null || queryParams.get("feedback") != null)) {
                headerJson.put("x-handled-by", "SVG creator");
                headerJson.put("Content-Type", "image/svg+xml");

                // transformation descriptions
                MetadataTransformer transformer;
                TransformationDescriptionResources res = new TransformationDescriptionResources("http://geoviqua.github.io/geolabel/mappings/transformer.json=/transformations/transformer.json,http://geoviqua.github.io/geolabel/mappings/transformerSML101.json=/transformations/transformerSML101.json,http://geoviqua.github.io/geolabel/mappings/transformerSOS20.json=/transformations/transformerSOS20.json,http://geoviqua.github.io/geolabel/mappings/transformerSML20.json=/transformations/transformerSML20.json,http://geoviqua.github.io/geolabel/mappings/transformerSSNO.json=/transformations/transformerSSNO.json");
                transformer = new MetadataTransformer(new TransformationDescriptionLoader(res,
                                                                                       new GeoLabelObjectMapper(res),
                                                                                       true));

                // temp label
                Label l = new Label();

                //drilldown urls
                l.setMetadataUrl(new URL("http://not.available.net"));
                l.setFeedbackUrl(new URL("http://not.available.net"));

                // if a metadata url is specified
                if(queryParams.get("metadata") != null){

                    //resource from url                                                                      
                    URL metadataURL = new URL(queryParams.get("metadata").toString());
                    URLConnection metadata = metadataURL.openConnection();
                    InputStream metadataStream = metadata.getInputStream();                                                                      

                    // update label
                    context.getLogger().log(String.format("Metadata Stream: %s", metadataStream));
                    label = transformer.updateGeoLabel(metadataStream, l);
                    context.getLogger().log(String.format("Label: %s", label));

                }
                
                // if a feedback url is specified
                if(queryParams.get("feedback") != null){

                    //resource from url                                                                      
                    URL feedbackURL = new URL(queryParams.get("feedback").toString());
                    URLConnection feedback = feedbackURL.openConnection();
                    InputStream feedbackStream = feedback.getInputStream();
                    
                    // update label
                    context.getLogger().log(String.format("Metadata Stream: %s", feedbackStream));
                    label = transformer.updateGeoLabel(feedbackStream, l);
                    context.getLogger().log(String.format("Label: %s", label));

                }

                int size;

                // if the size parameter is specified
                if(queryParams.get("size") != null){

                    size = Integer.parseInt(queryParams.get("size").toString());

                } else { size = 100;} // if not size = 100   

                // label to svg using the svgtemplate
                label.toSVG(new FileWriter(f), "geolabel", size);
                context.getLogger().log(String.format("Metadata Stream: %s", f));

                // get content of .svg file
                responseBody = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
            
            // if no metadata or feedback parameter is specified
            } else {
                headerJson.put("Content-Type", "plain/text");
                responseBody = "No metadata or feedback URL specified";
            }

            // create response
            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody);

        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex.toString());
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}