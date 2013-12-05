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
package org.n52.geolabel.component;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class GeoLabelComponentTag extends UIComponentELTag {

	private ValueExpression metadataContent;
	private ValueExpression feedbackContent;

	private ValueExpression metadataUrl;
	private ValueExpression feedbackUrl;

	private ValueExpression size;
	private ValueExpression async;
	private ValueExpression forceDownload;
	private ValueExpression useCache;

	private ValueExpression serviceUrl;

	@Override
	public String getComponentType() {
		return "geolabelcomponent";
	}

	@Override
	public String getRendererType() {
		return null;
	}

	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		if (metadataUrl != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.metadataUrl.name(), metadataUrl);
		}

		if (feedbackUrl != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.feedbackUrl.name(), feedbackUrl);
		}

		if (metadataContent != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.metadataContent.name(), metadataContent);
		}

		if (feedbackContent != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.feedbackContent.name(), feedbackContent);
		}

		if (size != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.size.name(), size);
		}

		if (async != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.async.name(), async);
		}

		if (forceDownload != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.forceDownload.name(), forceDownload);
		}

		if (useCache != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.useCache.name(), useCache);
		}

		if (serviceUrl != null) {
			component.setValueExpression(GeoLabelComponent.PropertyKeys.serviceUrl.name(), serviceUrl);
		}

	}

	public void setMetadataUrl(ValueExpression metadataUrl) {
		this.metadataUrl = metadataUrl;
	}

	public void setFeedbackUrl(ValueExpression feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public void setMetadataContent(ValueExpression metadataContent) {
		this.metadataContent = metadataContent;
	}

	public void setFeedbackContent(ValueExpression feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public void setSize(ValueExpression size) {
		this.size = size;
	}

	public void setAsync(ValueExpression async) {
		this.async = async;
	}

	public void setForceDownload(ValueExpression forceDownload) {
		this.forceDownload = forceDownload;
	}

	public void setUseCache(ValueExpression useCache) {
		this.useCache = useCache;
	}

	public void setServiceUrl(ValueExpression serviceUrl) {
		this.serviceUrl = serviceUrl;
	}



	@Override
	public void release() {
		this.async = null;
		this.feedbackContent = null;
		this.feedbackUrl = null;
		this.forceDownload = null;
		this.metadataContent = null;
		this.metadataUrl = null;
		this.serviceUrl = null;
		this.size = null;
		this.useCache = null;

		super.release();
	}

}
