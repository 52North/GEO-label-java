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

import java.io.IOException;
import java.io.InputStream;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.io.IOUtils;
import org.n52.geolabel.client.GeoLabelClientV1;
import org.n52.geolabel.client.GeoLabelRequestBuilder;

@FacesComponent("geolabelcomponent")
public class GeoLabelComponent extends UIComponentBase {

	enum PropertyKeys {
		metadataUrl, size, async;
	}

	public GeoLabelComponent() {
		super();
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		// TODO Auto-generated method stub
		super.restoreState(context, state);
	}

	public String getMetadataUrl() {
		return (String) getStateHelper().eval(PropertyKeys.metadataUrl);
	}

	public void setMetadataUrl(String mMetadataUrl) {
		getStateHelper().put(PropertyKeys.metadataUrl, mMetadataUrl);
	}

	public Integer getSize() {
		return (Integer) getStateHelper().eval(PropertyKeys.size);
	}

	public void setSize(Integer size) {
		getStateHelper().put(PropertyKeys.size, size);
	}

	public Boolean isAsync() {
		return (Boolean) getStateHelper().eval(PropertyKeys.async);
	}

	public void setAsync(boolean async) {
		getStateHelper().put(PropertyKeys.async, async);
	}

	@Override
	public String getFamily() {
		return null;
	}

	public void encodeBegin(FacesContext context) throws IOException {
		if (getMetadataUrl() == null) {
			return;
		}

		if (Boolean.TRUE.equals(isAsync())) {
			if (context.getPartialViewContext().isPartialRequest()) {
				if (!isServiceFailed(context)) {
					writeLocalLabel(context);

				}
			} else {
				writeClientLabel(context);

			}
		} else {
			writeLocalLabel(context);
		}

	}

	/**
	 * Requests a GeoLabel and writes it to the component
	 * 
	 * @param context
	 * @throws IOException
	 */
	private void writeLocalLabel(FacesContext context) throws IOException {
		try {
			GeoLabelRequestBuilder requestBuilder = GeoLabelClientV1.createGeoLabelRequest()
					.setMetadataDocument(getMetadataUrl()).setForceDownload(true).setUseCache(true);

			if (getSize() != null) {
				requestBuilder.setDesiredSize(getSize());
			}

			InputStream svg = requestBuilder.getSVG();
			String svgString = IOUtils.toString(svg);
			int svgStartIndex = svgString.indexOf("<svg");
			if (svgStartIndex == -1) {
				throw new IOException("No valid SVG");
			}

			ResponseWriter writer = context.getResponseWriter();

			writer.startElement("div", this);
			writer.writeAttribute("id", getClientId(context), null);
			// Mask nested CDATA ends as geolabel svg may contain CDATA blocks
			if (context.getPartialViewContext().isPartialRequest()) {
				svgString = svgString.replace("]]>", "<![CDATA[]]]]><![CDATA[>");
			}
			writer.append(svgString.substring(svgStartIndex));
			writer.endElement("div");
		} catch (IOException e) {
			setServiceFailed(context);
			throw e;
		}
	}

	/**
	 * Writes a script call for the client to request the GeoLabel in an
	 * upcoming call (PPR)
	 * 
	 * @param context
	 * @throws IOException
	 */
	private void writeClientLabel(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(), null);

		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.append("jsf.ajax.request('" + getClientId(context) + "', null, {'render': '@this'});");

		writer.endElement("script");

	}

	/**
	 * Marks the GeoLabel service for the current request as broken. Important
	 * to not send any further GeoLabel requests in case of an error.
	 * 
	 * @param context
	 */
	private static void setServiceFailed(FacesContext context) {
		ValueExpression expression = context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression.getValue(context.getELContext());
		resourcesBean.setGeoLabelServiceFailed(true);
	}

	/**
	 * Check whether the GeoLabel service is known to be broken for the current
	 * request
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isServiceFailed(FacesContext context) {
		ValueExpression expression = context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression.getValue(context.getELContext());
		return resourcesBean.isGeoLabelServiceFailed();
	}

}
