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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.GeoLabelConfig;

import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/v1/svg")
@Api(value = "/v1/svg", description = "Operations to retrieve GEO label SVG representations")
public class SVGResourceV1 {

	private Provider<LMLResourceV1> lmlResource;

	@Inject
	private SVGResourceV1(Provider<LMLResourceV1> lmlResource) {
		this.lmlResource = lmlResource;
	}

	@GET
	@Produces("image/svg+xml")
	@ApiOperation(value = "Returns a GEO label", notes = "Requires metadata/feedback documents as url")
	@ApiResponses({ @ApiResponse(code = 400, message = "Error in feedback/metadata document") })
	public Response getLabelSVGByURL(@ApiParam("Url to LML document") @QueryParam(Constants.PARAM_LML) URL lmlURL,
			@ApiParam("Url to metadata document") @QueryParam(Constants.PARAM_METADATA) URL metadataURL,
			@ApiParam("Url to feedback document") @QueryParam(Constants.PARAM_FEEDBACK) URL feedbackURL,
			@ApiParam("Desired size of returned SVG") @QueryParam(Constants.PARAM_SIZE) Integer size,
			@ApiParam("Desired id of returned SVG root element") @QueryParam(Constants.PARAM_ID) String id)
			throws IOException {

		Label label = null;
		if (lmlURL != null) {
			URLConnection con = lmlURL.openConnection();
			con.setConnectTimeout(GeoLabelConfig.CONNECT_TIMEOUT);
			con.setReadTimeout(GeoLabelConfig.READ_TIMEOUT);
			label = Label.fromXML(con.getInputStream());
		}
        else
            label = this.lmlResource.get().getLabelByURL(metadataURL, feedbackURL);
        return createLabelSVGResponse(size != null ? size.intValue() : 200, id, label);
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("image/svg+xml")
	@ApiOperation(value = "Returns a GEO label", notes = "Requires metadata/feedback documents as data stream")
	@ApiResponses({ @ApiResponse(code = 400, message = "Error in feedback/metadata document") })
	// TODO Find a way to use Document as Type for FormDataParams, instead of
	// Streams, seems to be unsupported
	public Response getLabelSVGByFile(
	/* @ApiParam("LML representation") */@FormDataParam(Constants.PARAM_LML) Label label,
	/* @ApiParam("Metadata document") */@FormDataParam(Constants.PARAM_METADATA) InputStream metadataInputStream,
	/* @ApiParam("Feedback document") */@FormDataParam(Constants.PARAM_FEEDBACK) InputStream feedbackInputStream,
	/* @ApiParam("Desired size of returned SVG") */@FormDataParam(Constants.PARAM_SIZE) Integer size,
	/* @ApiParam("Desired id of returned SVG root element") */@FormDataParam(Constants.PARAM_ID) String id)
			throws IOException {
        Label l = label;
        if (l == null)
            l = this.lmlResource.get().getLabelByFile(metadataInputStream, feedbackInputStream);
        return createLabelSVGResponse(size != null ? size.intValue() : 200, id, l);
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
