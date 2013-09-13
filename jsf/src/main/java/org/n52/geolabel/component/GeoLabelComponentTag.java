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

	private ValueExpression metadataUrl;
	private ValueExpression size;
	private ValueExpression async;

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
			component.setValueExpression(
					GeoLabelComponent.PropertyKeys.metadataUrl.name(),
					metadataUrl);
		}

		if (size != null) {
			component.setValueExpression(
					GeoLabelComponent.PropertyKeys.size.name(), size);
		}

		if (async != null) {
			component.setValueExpression(
					GeoLabelComponent.PropertyKeys.async.name(), async);
		}

	}

	public void setMetadataUrl(ValueExpression metadataUrl) {
		this.metadataUrl = metadataUrl;
	}

	public void setSize(ValueExpression size) {
		this.size = size;
	}

	public void setAsync(ValueExpression async) {
		this.async = async;
	}

	public ValueExpression getAsync() {
		return async;
	}

	public ValueExpression getMetadataUrl() {
		return metadataUrl;
	}

	public ValueExpression getSize() {
		return size;
	}
}
