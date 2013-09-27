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
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.mapping.MetadataTransformer;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.multipart.FormDataParam;

@Path("/api")
public class GeoLabelResource {

	private Provider<MetadataTransformer> transformer;

	@Inject
	public GeoLabelResource(Provider<MetadataTransformer> transformer) {
		this.transformer = transformer;
	}

	@GET
	@Path("/lml")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Label getLabelByURL(
			@QueryParam(Constants.PARAM_METADATA) URL metadataURL,
			@QueryParam(Constants.PARAM_FEEDBACK) URL feedbackURL)
			throws IOException {
		if (metadataURL == null && feedbackURL == null) {
			throw new WebApplicationException(Response.serverError()
					.type(MediaType.TEXT_PLAIN)
					.entity("No metadata or feedback URL specified").build());
		}

		MetadataTransformer metadataTransformer = transformer.get();

		return metadataTransformer.getLabel(metadataURL, feedbackURL);
	}

	@POST
	@Path("/lml")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Label getLabelByFile(
			@FormDataParam(Constants.PARAM_METADATA) InputStream metadataInputStream,
			@FormDataParam(Constants.PARAM_FEEDBACK) InputStream feedbackInputStream)
			throws IOException {
		MetadataTransformer metadataTransformer = transformer.get();

		Label label = new Label();
		PushbackInputStream tempStream;
		boolean hasData = false;
		if (metadataInputStream != null) {
			tempStream = new PushbackInputStream(metadataInputStream);
			int t = tempStream.read();
			if (t != -1) {
				tempStream.unread(t);
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
				metadataTransformer.updateGeoLabel(tempStream, label);
				hasData = true;
			} else {
				tempStream.close();
			}
		}

		if (!hasData) {
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST)
					.type(MediaType.TEXT_PLAIN)
					.entity("No metadata or feedback file specified").build());
		}

		return label;
	}

	@GET
	@Path("/svg")
	@Produces("image/svg+xml")
	public Response getLabelSVGByURL(
			@QueryParam(Constants.PARAM_LML) URL lmlURL,
			@QueryParam(Constants.PARAM_METADATA) URL metadataURL,
			@QueryParam(Constants.PARAM_FEEDBACK) URL feedbackURL,
			@QueryParam(Constants.PARAM_SIZE) Integer size,
			@QueryParam(Constants.PARAM_ID) String id) throws IOException {

		Label label = null;
		if (lmlURL != null) {
			URLConnection con = lmlURL.openConnection();
			con.setConnectTimeout(GeoLabelConfig.CONNECT_TIMEOUT);
			con.setReadTimeout(GeoLabelConfig.READ_TIMEOUT);
			label = Label.fromXML(con.getInputStream());
		} else {
			label = getLabelByURL(metadataURL, feedbackURL);
		}
		return createLabelSVGResponse(size != null ? size : 200, id, label);
	}

	@POST
	@Path("/svg")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("image/svg+xml")
	public Response getLabelSVGByFile(
			@FormDataParam(Constants.PARAM_LML) Label label,
			@FormDataParam(Constants.PARAM_METADATA) InputStream metadataInputStream,
			@FormDataParam(Constants.PARAM_FEEDBACK) InputStream feedbackInputStream,
			@FormDataParam(Constants.PARAM_SIZE) Integer size,
			@FormDataParam(Constants.PARAM_ID) String id) throws IOException {
		if (label == null) {
			label = getLabelByFile(metadataInputStream, feedbackInputStream);
		}
		return createLabelSVGResponse(size != null ? size : 200, id, label);
	}

	@GET
	public Response showTestPage() throws IOException {
		return Response.ok(new Viewable("/geolabelTest.jspx")).build();
	}

	@GET
	@Path("/{pp:[0-2]}{pc:[0-2]}{li:[0-2]}{sc:[0-2]}{qi:[0-2]}{uf:[0-2]}{ef:[0-2]}{ci:[0-2]}.svg")
	public Response getStaticLabelSVG(
			//
			@PathParam("pp") Availability producerProfileAvailability,
			@PathParam("pc") Availability producerCommentsAvailability,
			@PathParam("li") Availability lineageAvailability,
			@PathParam("sc") Availability standardsComplianceAvailability,
			@PathParam("qi") Availability qualityInformationAvailability,
			@PathParam("uf") Availability userFeedbackAvailability,
			@PathParam("ef") Availability expertFeedbackAvailability,
			@PathParam("ci") Availability citationsAvailability,
			//
			@QueryParam(Constants.PARAM_SIZE) Integer size,
			@QueryParam(Constants.PARAM_ID) String id) {

		Label label = new Label();

		label.getProducerProfileFacet().updateAvailability(
				producerProfileAvailability);
		label.getProducerCommentsFacet().updateAvailability(
				producerCommentsAvailability);
		label.getLineageFacet().updateAvailability(lineageAvailability);
		label.getStandardsComplianceFacet().updateAvailability(
				standardsComplianceAvailability);
		label.getQualityInformationFacet().updateAvailability(
				qualityInformationAvailability);
		label.getUserFeedbackFacet().updateAvailability(
				userFeedbackAvailability);
		label.getExpertFeedbackFacet().updateAvailability(
				expertFeedbackAvailability);
		label.getCitationsFacet().updateAvailability(citationsAvailability);

		return createLabelSVGResponse(size != null ? size : 200, id, label);
	}

	private static Response createLabelSVGResponse(final int size,
			final String id, final Label label) {
		return Response.ok().entity(new StreamingOutput() {
			@Override
			public void write(OutputStream stream) throws IOException,
					WebApplicationException {
				label.toSVG(new OutputStreamWriter(stream), id, size);
			}
		}).type("image/svg+xml").build();
	}

}
