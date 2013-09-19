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

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

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

		public String getFeedbackUrl() {
			return feedback;
		}

		public String getMetadataUrl() {
			return metadata;
		}

		public String getTitle() {
			return title;
		}
	}

	private String metadataUrl = "http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/PQMs/DigitalClimaticAtlas_mt_an_v10.xml";
	private String feedbackUrl = "";

	private List<Example> examplesList = new ArrayList<LandingPageBean.Example>();

	public LandingPageBean() {
		examplesList
				.add(new Example(
						"Climate Atlas",
						"http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/PQMs/DigitalClimaticAtlas_mt_an_v10.xml"));

		examplesList
				.add(new Example(
						"Feedback Use Case",
						"http://schemas.geoviqua.org/GVQ/3.1.0/example_documents/FeedbackUseCase_7_7_metadata.xml"));
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}

	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	public void setMetadataUrl(String metadataUrl) {
		this.metadataUrl = metadataUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public List<Example> getExamplesList() {
		return examplesList;
	}
}
