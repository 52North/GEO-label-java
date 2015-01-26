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
package org.n52.geolabel.server.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.multipart.FormDataParam;

@Path("/v1/lml")
//@Api(value = "/v1/lml", description = "Operations to retrieve GEO label LML representations")
public class LMLResourceV1 {

    private static Logger log = LoggerFactory.getLogger(LMLResourceV1.class);

    private Provider<MetadataTransformer> transformer;

    @Inject
    private LMLResourceV1(Provider<MetadataTransformer> transformer) {
        this.transformer = transformer;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    @ApiOperation(value = "Returns a GEO label LML representation", notes = "Requires metadata/feedback documents as url")
//    @ApiResponses({@ApiResponse(code = 400, message = "Error in feedback/metadata document")})
    public Label getLabelByURL(/*@ApiParam("Url to metadata document")*/
            @QueryParam(Constants.PARAM_METADATA) URL metadataURL, /*@ApiParam("Url to feedback document")*/
            @QueryParam(Constants.PARAM_FEEDBACK) URL feedbackURL, /*@ApiParam("use cached labels")*/
            @QueryParam(Constants.PARAM_USECACHE)
            @DefaultValue(Constants.PARAM_USECACHE_DEFAULT) boolean useCache) throws IOException {
        if (metadataURL == null && feedbackURL == null) {
            throw new WebApplicationException(Response.ok().status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("No metadata or feedback URL specified").build());
        }

        MetadataTransformer metadataTransformer = this.transformer.get();

        return metadataTransformer.getLabel(metadataURL, feedbackURL, useCache);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    @ApiOperation(value = "Returns a GEO label LML representation", notes = "Requires metadata/feedback documents as data stream")
//    @ApiResponses({@ApiResponse(code = 400, message = "Error in feedback/metadata document")})
    public Label getLabelByStream(
            /* @ApiParam("Metadata document") */@FormDataParam(Constants.PARAM_METADATA) InputStream metadataInputStream,
            /* @ApiParam("Feedback document") */ @FormDataParam(Constants.PARAM_FEEDBACK) InputStream feedbackInputStream) throws IOException {
        MetadataTransformer metadataTransformer = this.transformer.get();

        Label label = new Label();
        label.setMetadataUrl(new URL("http://not.available.for.direct.upload"));
        label.setFeedbackUrl(new URL("http://not.available.for.direct.upload"));

        PushbackInputStream tempStream;
        boolean hasData = false;
        if (metadataInputStream != null) {
            tempStream = new PushbackInputStream(metadataInputStream);
            int t = tempStream.read();
            if (t != -1) {
                tempStream.unread(t);
                log.debug("Reading from metadata stream...");
                metadataTransformer.updateGeoLabel(tempStream, label);
                hasData = true;
            } else {
                tempStream.close();
            }
        }
        if (feedbackInputStream != null) {
            tempStream = new PushbackInputStream(feedbackInputStream);
            int t = tempStream.read();
            if (t != -1) {
                tempStream.unread(t);
                log.debug("Reading from feedback stream...");
                metadataTransformer.updateGeoLabel(tempStream, label);
                hasData = true;
            } else {
                tempStream.close();
            }
        }

        if (!hasData) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("No metadata or feedback file specified").build());
        }

        return label;
    }

}
