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
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@ManagedBean
@SessionScoped
public class EditorBean {

	public static class Endpoint {
		@NotNull
		@NotEmpty
		@URL
		String url;

		String name;

		public Endpoint() {
		};

		public Endpoint(String url, String name) {
			this.url = url;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public boolean equals(Object obj) {
			if (url != null && obj instanceof Endpoint) {
				return url.equals(((Endpoint) obj).url);
			}
			return super.equals(obj);
		}
	}

	private String metadataContent = "";
	private String feedbackContent = "";

	private List<Endpoint> comparisonServices = new ArrayList<Endpoint>();
	private Endpoint newCustomService = new Endpoint();

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getMetadataContent() {
		return metadataContent;
	}

	public void setMetadataContent(String metadataContent) {
		this.metadataContent = metadataContent;
	}

	public List<Endpoint> getComparisonServices() {
		return comparisonServices;
	}

	public void setComparisonServices(List<Endpoint> comparisonServices) {
		this.comparisonServices = comparisonServices;
	}

	public Endpoint getNewCustomService() {
		return newCustomService;
	}

	public void setNewCustomService(Endpoint newCustomService) {
		this.newCustomService = newCustomService;
	}

	public void addCustomServiceEndpoint() {
		if (newCustomService != null && newCustomService.url != null && !comparisonServices.contains(newCustomService)) {
			comparisonServices.add(newCustomService);
			newCustomService = new Endpoint();
		}
	}

	public void removeCustomServiceEndpoint(Endpoint endpoint) {
		comparisonServices.remove(endpoint);
	}

	public void clearServiceEndpoints() {
		comparisonServices.clear();
	}
}
