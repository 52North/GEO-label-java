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
            //
        }

		public Endpoint(String url, String name) {
			this.url = url;
			this.name = name;
		}

		public String getName() {
            return this.name;
		}

		public String getUrl() {
            return this.url;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public boolean equals(Object obj) {
            if (this.url != null && obj instanceof Endpoint)
                return this.url.equals(((Endpoint) obj).url);

			return super.equals(obj);
		}

		@Override
		public int hashCode() {
            if (this.url == null)
                return super.hashCode();
            return this.url.hashCode();
		}
	}

	private String metadataContent = "";
	private String feedbackContent = "";

	private List<Endpoint> comparisonServices = new ArrayList<Endpoint>();
	private Endpoint newCustomService = new Endpoint();

	public String getFeedbackContent() {
        return this.feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getMetadataContent() {
        return this.metadataContent;
	}

	public void setMetadataContent(String metadataContent) {
		this.metadataContent = metadataContent;
	}

	public List<Endpoint> getComparisonServices() {
        return this.comparisonServices;
	}

	public void setComparisonServices(List<Endpoint> comparisonServices) {
		this.comparisonServices = comparisonServices;
	}

	public Endpoint getNewCustomService() {
        return this.newCustomService;
	}

	public void setNewCustomService(Endpoint newCustomService) {
		this.newCustomService = newCustomService;
	}

	public void addCustomServiceEndpoint() {
        if (this.newCustomService != null && this.newCustomService.url != null
                && !this.comparisonServices.contains(this.newCustomService)) {
            this.comparisonServices.add(this.newCustomService);
            this.newCustomService = new Endpoint();
		}
	}

	/**
	 * Allows to add a specific service endpoint to the list of custom geo label
	 * apis to use
	 *
	 * @param endpointUrl
	 * @param endpointName
	 */
	public void addCustomServiceEndpoint(String endpointUrl, String endpointName) {
		if (endpointUrl != null) {
			Endpoint newEndpoint = new Endpoint(endpointUrl, endpointName);
            if ( !this.comparisonServices.contains(newEndpoint))
                this.comparisonServices.add(new Endpoint(endpointUrl, endpointName));
		}
	}

	public void removeCustomServiceEndpoint(Endpoint endpoint) {
        this.comparisonServices.remove(endpoint);
	}

	public void clearServiceEndpoints() {
        this.comparisonServices.clear();
	}
}
