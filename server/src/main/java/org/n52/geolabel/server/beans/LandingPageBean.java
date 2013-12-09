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
package org.n52.geolabel.server.beans;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped
public class LandingPageBean {

	public static class Example {
		String metadata;
		String feedback;
		String title;

		public Example(String title, String metadata) {
			this.title = title;
			this.metadata = metadata;
		}

		public Example(String title, String metadata, String feedback) {
			this.title = title;
			this.metadata = metadata;
			this.feedback = feedback;
		}

		public String getFeedbackUrl() {
            return this.feedback;
		}

		public String getMetadataUrl() {
            return this.metadata;
		}

		public String getTitle() {
            return this.title;
		}
	}

	@org.hibernate.validator.constraints.URL
	private String metadataUrl = "http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/PQMs/DigitalClimaticAtlas_mt_an_v10.xml";
	@org.hibernate.validator.constraints.URL
	private String feedbackUrl = "";

	private List<Example> examplesList = new ArrayList<LandingPageBean.Example>();

	public LandingPageBean() {
        this.examplesList.add(new Example("Climate Atlas",
				"http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/PQMs/DigitalClimaticAtlas_mt_an_v10.xml"));

        this.examplesList.add(new Example("Feedback Use Case",
                                          null,
				"http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/FeedbackUseCase_7_7_metadata.xml"));
	}

	public String getMetadataUrl() {
        return this.metadataUrl;
	}

	public String getFeedbackUrl() {
        return this.feedbackUrl;
	}

	public void setMetadataUrl(String metadataUrl) {
		this.metadataUrl = metadataUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public List<Example> getExamplesList() {
        return this.examplesList;
	}

	/**
	 * Returns the absolute GEO label service API endpoint of this webapp
	 * instance. For example required by JSF components as these work
	 * Independent of this service.
	 *
	 * @return
	 * @throws MalformedURLException
	 */
	public String getServiceEndpoint() throws MalformedURLException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		URL requesturl = new URL(request.getRequestURL().toString());
		URL serviceUrl = new URL(requesturl.getProtocol(), requesturl.getHost(), requesturl.getPort(),
				request.getContextPath() + "/api/v1/svg/");
		return serviceUrl.toString();
	}
}
