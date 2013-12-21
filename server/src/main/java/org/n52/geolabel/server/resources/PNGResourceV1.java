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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.formats.PngEncoder;
import org.n52.geolabel.server.config.GeoLabelConfig;

import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/v1/png")
@Api(value = "/v1/png", description = "Operations to retrieve GEO label PNG representations")
public class PNGResourceV1 {

    private static final String MEDIA_TYPE = "image/png";

    private static final int DEFAULT_PNG_SIZE = 256;

    private Provider<LMLResourceV1> lmlResource;

    protected PngEncoder encoder;

    @Inject
    private PNGResourceV1(Provider<LMLResourceV1> lmlResource, PngEncoder encoder) {
        this.lmlResource = lmlResource;
        this.encoder = encoder;
    }

    @GET
    @Produces(MEDIA_TYPE)
    @ApiOperation(value = "Returns a GEO label", notes = "Requires metadata/feedback documents as url")
    @ApiResponses({@ApiResponse(code = 400, message = "Error in feedback/metadata document")})
    public Response getLabelSVGByURL(@ApiParam("Url to LML document")
    @QueryParam(Constants.PARAM_LML)
    URL lmlURL, @ApiParam("Url to metadata document")
    @QueryParam(Constants.PARAM_METADATA)
    URL metadataURL, @ApiParam("Url to feedback document")
    @QueryParam(Constants.PARAM_FEEDBACK)
    URL feedbackURL, @ApiParam("Desired size of returned PNG")
    @QueryParam(Constants.PARAM_SIZE)
    Integer size, @ApiParam("use cached labels")
    @QueryParam(Constants.PARAM_USECACHE)
    @DefaultValue(Constants.PARAM_USECACHE_DEFAULT)
    boolean useCache) throws IOException {

        Label label = null;
        if (lmlURL != null) {
            URLConnection con = lmlURL.openConnection();
            con.setConnectTimeout(GeoLabelConfig.CONNECT_TIMEOUT);
            con.setReadTimeout(GeoLabelConfig.READ_TIMEOUT);
            label = Label.fromXML(con.getInputStream());
        }
        else {
            LMLResourceV1 lmlR = this.lmlResource.get();
            label = lmlR.getLabelByURL(metadataURL, feedbackURL, useCache);
        }
        return createLabelPNGResponse(size != null ? size.intValue() : DEFAULT_PNG_SIZE, label);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MEDIA_TYPE)
    @ApiOperation(value = "Returns a GEO label", notes = "Requires metadata/feedback documents as data stream")
    @ApiResponses({@ApiResponse(code = 400, message = "Error in feedback/metadata document")})
    // TODO Find a way to use Document as Type for FormDataParams, instead of
    // Streams, seems to be unsupported
    public Response getLabelSVGByFile(
    /* @ApiParam("LML representation") */@FormDataParam(Constants.PARAM_LML)
    Label label,
    /* @ApiParam("Metadata document") */@FormDataParam(Constants.PARAM_METADATA)
    InputStream metadataInputStream,
    /* @ApiParam("Feedback document") */@FormDataParam(Constants.PARAM_FEEDBACK)
    InputStream feedbackInputStream,
    /* @ApiParam("Desired size of returned PNG") */@FormDataParam(Constants.PARAM_SIZE)
    Integer size) throws IOException {
        Label l = label;
        if (l == null)
            l = this.lmlResource.get().getLabelByStream(metadataInputStream, feedbackInputStream);
        return createLabelPNGResponse(size != null ? size.intValue() : DEFAULT_PNG_SIZE, l);
    }

    public Response createLabelPNGResponse(final int size, final Label label) {
        return Response.ok().entity(new StreamingOutput() {
            @Override
            public void write(OutputStream stream) throws WebApplicationException {

                try (InputStream in = PNGResourceV1.this.encoder.encode(label, size);) {
                    IOUtils.copy(in, stream);
                }
                catch (IOException e) {
                    throw new WebApplicationException(e);
                }
            }
        }).type(MEDIA_TYPE).build();
    }

}
