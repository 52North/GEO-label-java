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
 * @author Daniel Nüst, Anika Graupner
 */
public class APIHandler implements RequestStreamHandler {

    private JSONParser parser = new JSONParser();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); // get inputstream
        
        JSONObject responseJson = new JSONObject(); // JSON object for the response
        Label label = new Label(); // new geolabel 
        File f = File.createTempFile("geolabel_", ".svg"); // file for the filereader to process label to svg
        String responseBody; // string for the response

        try {

            JSONObject event = (JSONObject) parser.parse(reader); // JSON object from the inputstream
            JSONObject pathParams = new JSONObject(); // path Parameters
            JSONObject headerJson = new JSONObject(); // header of response

            // if the requests conatains path parameters
            if (event.get("pathParameters") != null) {
                pathParams = (JSONObject) event.get("pathParameters");
                context.getLogger().log(String.format("Path parameters: %s", pathParams));

                // if the request contains query parameters
                if (event.get("queryStringParameters") != null) {
                    JSONObject queryParams = (JSONObject) event.get("queryStringParameters");
                    context.getLogger().log(String.format("Query string parameters: %s", queryParams));

                    // do only if a metadata or a feedback url is defined at the endpoint api/v1/svg
                    if (pathParams.values().contains("api/v1/svg") && (queryParams.get("metadata") != null || queryParams.get("feedback") != null)) {

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

                        } else { size = 200;} // if not size = 200   

                        // label to svg using the svgtemplate
                        label.toSVG(new FileWriter(f), "geolabel", size);
                        context.getLogger().log(String.format("Metadata Stream: %s", f));

                        // get content of .svg file
                        responseBody = FileUtils.readFileToString(f, StandardCharsets.UTF_8);

                        // set response headers and body
                        responseJson.put("statusCode", 200);
                        headerJson.put("x-handled-by", "SVG creator");
                        headerJson.put("Content-Type", "image/svg+xml");
                        responseJson.put("headers", headerJson);
                        responseJson.put("body", responseBody);
                    
                    } else { // if no metadata or feedback url is defined

                        responseBody = "No metadata or feedback URL specified.";
                        headerJson.put("Content-Type", "text/plain");
                        responseJson.put("statusCode", 200);
                        responseJson.put("headers", headerJson);
                        responseJson.put("body", responseBody);
                    }

                // if path od request is ...api  
                } else if (pathParams.values().contains("api")) { 

                    JSONObject obj = new JSONObject();
                    obj.put("currentVersion","https://6x843uryh9.execute-api.eu-central-1.amazonaws.com/glbservice/api/v1");
                    obj.put("v1","https://6x843uryh9.execute-api.eu-central-1.amazonaws.com/glbservice/api/v1");
                    responseBody = obj.toString();
                    responseBody = responseBody.replace("\\","");
                    headerJson.put("Content-Type", "application/json");
                    responseJson.put("statusCode", 200);
                    responseJson.put("headers", headerJson);
                    responseJson.put("body", responseBody);

                // if path of request is ...api/v1 
                } else if (pathParams.values().contains("api/v1")) {

                    JSONObject obj = new JSONObject();
                    obj.put("version","v1");
                    obj.put("svg-label-generation","https://6x843uryh9.execute-api.eu-central-1.amazonaws.com/glbservice/api/v1/svg");
                    responseBody = obj.toString();
                    responseBody = responseBody.replace("\\","");
                    headerJson.put("Content-Type", "application/json");
                    responseJson.put("statusCode", 200);
                    responseJson.put("headers", headerJson);
                    responseJson.put("body", responseBody);
                
                // if path of request is ...api/v1/svg 
                } else if(pathParams.values().contains("api/v1/svg")){

                    responseBody = "No metadata or feedback URL specified.";
                    headerJson.put("Content-Type", "text/plain");
                    responseJson.put("statusCode", 200);
                    responseJson.put("headers", headerJson);
                    responseJson.put("body", responseBody);

                // if path parameters are specified which are not supported from the api   
                } else {

                    responseBody = "Page not found.";
                    headerJson.put("Content-Type", "text/plain");
                    responseJson.put("statusCode", 404);
                    responseJson.put("headers", headerJson);
                    responseJson.put("body", responseBody);
                }
            
            } 

        // if something went wrong by handling the request    
        } catch (ParseException pex){

            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex.toString());
        }

        // OutputStream for Response
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();

    }
}