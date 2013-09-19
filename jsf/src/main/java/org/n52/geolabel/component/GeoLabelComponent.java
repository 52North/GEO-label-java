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
import javax.faces.application.Resource;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.io.IOUtils;
import org.n52.geolabel.client.GeoLabelClientV1;
import org.n52.geolabel.client.GeoLabelRequestBuilder;

@FacesComponent("geolabelcomponent")
@ResourceDependency(library = "javax.faces", name = "jsf.js")
public class GeoLabelComponent extends UIComponentBase {

	enum PropertyKeys {
		metadataUrl, feedbackUrl, size, async;
	}

	public GeoLabelComponent() {
		super();
	}

	public String getMetadataUrl() {
		return (String) getStateHelper().eval(PropertyKeys.metadataUrl);
	}

	public void setMetadataUrl(String mMetadataUrl) {
		getStateHelper().put(PropertyKeys.metadataUrl, mMetadataUrl);
	}

	public String getFeedbackUrl() {
		return (String) getStateHelper().eval(PropertyKeys.feedbackUrl);
	}

	public void setFeedbackUrl(String mFeedbackUrl) {
		getStateHelper().put(PropertyKeys.feedbackUrl, mFeedbackUrl);
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
		ResponseWriter writer = context.getResponseWriter();

		try {
			GeoLabelRequestBuilder requestBuilder = GeoLabelClientV1
					.createGeoLabelRequest().setForceDownload(true)
					.setUseCache(true);
			if (getMetadataUrl() != null && !getMetadataUrl().isEmpty())
				requestBuilder.setMetadataDocument(getMetadataUrl());

			if (getFeedbackUrl() != null && !getFeedbackUrl().isEmpty())
				requestBuilder.setFeedbackDocument(getFeedbackUrl());

			if (getSize() != null)
				requestBuilder.setDesiredSize(getSize());

			InputStream svg = requestBuilder.getSVG();
			String svgString = IOUtils.toString(svg);
			int svgStartIndex = svgString.indexOf("<svg");
			if (svgStartIndex == -1) {
				throw new IOException("No valid SVG");
			}

			writer.startElement("div", this);
			writer.writeAttribute("id", getClientId(context), null);
			if (context.getPartialViewContext().isPartialRequest()) {
				// Mask nested CDATA ends in partial response as geolabel svg
				// may contain CDATA blocks
				svgString = svgString
						.replace("]]>", "<![CDATA[]]]]><![CDATA[>");
			}
			writer.append(svgString.substring(svgStartIndex));
			writer.endElement("div");
		} catch (IOException e) {
			setServiceFailed(context);

			writer.startElement("div", this);
			writer.writeAttribute("id", getClientId(context), null);

			writer.append("Service failed: " + e.getMessage());
			writer.endElement("div");

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

		writer.startElement("div", this);
		Resource createResource = context.getApplication().getResourceHandler()
				.createResource("spinner.gif");

		int size = getSize() == null ? 16 : getSize();

		writer.writeAttribute(
				"style",
				"width:" + size + "px;" + "height:" + size + "px;"
						+ "background-image: url('"
						+ createResource.getRequestPath() + "');"
						+ "background-repeat:no-repeat;"
						+ "background-position:center;",

				null);

		writer.endElement("div");

		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.append("jsf.ajax.request('" + getClientId(context)
				+ "', null, {'render': '@this'});");

		writer.endElement("script");

	}

	/**
	 * Marks the GeoLabel service for the current request as broken. Important
	 * to not send any further GeoLabel requests in case of an error.
	 * 
	 * @param context
	 */
	private static void setServiceFailed(FacesContext context) {
		ValueExpression expression = context
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(context.getELContext(),
						"#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression
				.getValue(context.getELContext());
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
		ValueExpression expression = context
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(context.getELContext(),
						"#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression
				.getValue(context.getELContext());
		return resourcesBean.isGeoLabelServiceFailed();
	}

}
