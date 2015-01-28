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

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.SessionScoped;
import com.sun.jersey.multipart.FormDataParam;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.GeoLabelConfig;

/**
 * Test URL: http://localhost:8080/server/api/v1/geolabel?metadata=http://schemas.geoviqua.org/GVQ/4.0/example_documents/PQMs/DigitalClimaticAtlas_mt_an_GEOlabel.xml
 * @author Daniel
 */
@Path("/v1/geolabel")
@SessionScoped
public class StandardizedGeolabelResourceV1 {

    private static Logger log = LoggerFactory.getLogger(StandardizedGeolabelResourceV1.class);

    private URI redirectPath;

    @Inject
    public StandardizedGeolabelResourceV1(@Context UriInfo uri) {
        this.redirectPath = UriBuilder.fromUri(uri.getBaseUri()).path("v1").path("svg").build();

        log.info("NEW {} @ {}", this.toString(), this.redirectPath);
    }

    @GET
    @Produces("image/svg+xml")
    public Response getLabelSVGByURL(
            @Context UriInfo uriInfo) throws IOException {
        UriBuilder builder = uriInfo.getRequestUriBuilder().clone();
        builder.replacePath(this.redirectPath.getPath());

        URI uri = builder.build();
        log.trace("Redirecting to {}", uri);
        return Response.temporaryRedirect(uri).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/svg+xml")
    public Response getLabelSVGByFile(
            //            @FormDataParam(Constants.PARAM_LML) Label label,
            //            @FormDataParam(Constants.PARAM_METADATA) InputStream metadataInputStream,
            //            @FormDataParam(Constants.PARAM_FEEDBACK) InputStream feedbackInputStream,
            //            @FormDataParam(Constants.PARAM_SIZE) Integer size,
            //            @FormDataParam(Constants.PARAM_ID) String id
            MultivaluedMap<String, String> form, @Context UriInfo uriInfo) throws IOException {
        UriBuilder builder = uriInfo.getRequestUriBuilder().clone();
        builder.replacePath(this.redirectPath.getPath());

        URI uri = builder.build();
        log.trace("Redirecting to {}", uri);
        return Response.temporaryRedirect(uri).build();
    }

    static Response createLabelSVGResponse(final int size, final String id, final Label label) {
        return Response.ok().entity(new StreamingOutput() {
            @Override
            public void write(OutputStream stream) throws IOException, WebApplicationException {
                label.toSVG(new OutputStreamWriter(stream), id, size);
            }
        }).type("image/svg+xml").build();
    }
}
