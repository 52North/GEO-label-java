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

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.n52.geolabel.commons.Constants;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.server.mapping.MetadataTransformer;

@Path("/api/v1/static")
public class StaticLabelResourceV1 {

	@Inject
	private StaticLabelResourceV1(Provider<MetadataTransformer> transformer) {
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
			@QueryParam(Constants.PARAM_SIZE) Integer size, @QueryParam(Constants.PARAM_ID) String id) {

		Label label = new Label();

		label.getProducerProfileFacet().updateAvailability(producerProfileAvailability);
		label.getProducerCommentsFacet().updateAvailability(producerCommentsAvailability);
		label.getLineageFacet().updateAvailability(lineageAvailability);
		label.getStandardsComplianceFacet().updateAvailability(standardsComplianceAvailability);
		label.getQualityInformationFacet().updateAvailability(qualityInformationAvailability);
		label.getUserFeedbackFacet().updateAvailability(userFeedbackAvailability);
		label.getExpertFeedbackFacet().updateAvailability(expertFeedbackAvailability);
		label.getCitationsFacet().updateAvailability(citationsAvailability);

		return LabelResourceV1.createLabelSVGResponse(size != null ? size : 200, id, label);
	}

}
