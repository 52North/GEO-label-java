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
import java.nio.charset.Charset;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
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
		metadataUrl, feedbackUrl, size, async, serviceUrl, metadataContent, feedbackContent, forceDownload, useCache;
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

	public String getServiceUrl() {
		return (String) getStateHelper().eval(PropertyKeys.serviceUrl);
	}

	public void setServiceUrl(String mServiceUrl) {
		getStateHelper().put(PropertyKeys.serviceUrl, mServiceUrl);
	}

	public String getFeedbackUrl() {
		return (String) getStateHelper().eval(PropertyKeys.feedbackUrl);
	}

	public void setFeedbackUrl(String mFeedbackUrl) {
		getStateHelper().put(PropertyKeys.feedbackUrl, mFeedbackUrl);
	}

	public String getFeedbackContent() {
		return (String) getStateHelper().eval(PropertyKeys.feedbackContent);
	}

	public void setFeedbackContent(String mFeedbackContent) {
		getStateHelper().put(PropertyKeys.feedbackContent, mFeedbackContent);
	}

	public String getMetadataContent() {
		return (String) getStateHelper().eval(PropertyKeys.metadataContent);
	}

	public void setMetadataContent(String mMetadataContent) {
		getStateHelper().put(PropertyKeys.metadataContent, mMetadataContent);
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

	public Boolean isUseCache() {
		return (Boolean) getStateHelper().eval(PropertyKeys.useCache);
	}

	public void setUseCache(boolean cache) {
		getStateHelper().put(PropertyKeys.useCache, cache);
	}

	public Boolean isForceDownload() {
		return (Boolean) getStateHelper().eval(PropertyKeys.forceDownload);
	}

	public void setForceDownload(boolean force) {
		getStateHelper().put(PropertyKeys.forceDownload, force);
	}

	@Override
	public String getFamily() {
		return null;
	}

	public void encodeBegin(FacesContext context) throws IOException {
		if (Boolean.TRUE.equals(isAsync())) {
			if (context.getPartialViewContext().isPartialRequest()) {
				String endpoint = getServiceUrl() != null && !getServiceUrl().isEmpty() ? getServiceUrl() : "default";
				if (!isServiceFailed(endpoint, context)) {
					writeLocalLabel(context);
				} else {
					FacesMessage message = new FacesMessage("Previous request failed");
					message.setSeverity(FacesMessage.SEVERITY_WARN);
					context.addMessage(getClientId(), message);
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
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(context), null);
		try {
			GeoLabelRequestBuilder requestBuilder;

			if (getServiceUrl() != null && !getServiceUrl().isEmpty()) {
				// Custom service url
				requestBuilder = GeoLabelClientV1.createGeoLabelRequest(getServiceUrl());
			} else {
				// Default service url
				requestBuilder = GeoLabelClientV1.createGeoLabelRequest();
			}

			// Set metadata
			if (getMetadataUrl() != null && !getMetadataUrl().isEmpty()) {
				requestBuilder.setMetadataDocument(getMetadataUrl());
			} else if (getMetadataContent() != null && !getMetadataContent().isEmpty()) {
				requestBuilder
						.setMetadataDocument(IOUtils.toInputStream(getMetadataContent(), Charset.forName("utf-8"))); // TODO
																														// charset

			}
			// Set feedback
			if (getFeedbackUrl() != null && !getFeedbackUrl().isEmpty()) {
				requestBuilder.setFeedbackDocument(getFeedbackUrl());
			} else if (getFeedbackContent() != null && !getFeedbackContent().isEmpty()) {
				requestBuilder
						.setFeedbackDocument(IOUtils.toInputStream(getFeedbackContent(), Charset.forName("utf-8"))); // TODO
																														// charset

			}

			// Further params
			if (getSize() != null)
				requestBuilder.setDesiredSize(getSize());

			if (isForceDownload() != null)
				requestBuilder.setForceDownload(isForceDownload());

			if (isUseCache() != null)
				requestBuilder.setUseCache(isUseCache());

			InputStream svg = requestBuilder.getSVG();
			String svgString = IOUtils.toString(svg);
			int svgStartIndex = svgString.indexOf("<svg");
			if (svgStartIndex == -1) {
				throw new IOException("No valid SVG");
			}

			if (context.getPartialViewContext().isPartialRequest()) {
				// Mask nested CDATA ends in partial response as geolabel svg
				// may contain CDATA blocks
				svgString = svgString.replace("]]>", "<![CDATA[]]]]><![CDATA[>");
			}
			writer.append(svgString.substring(svgStartIndex));

		} catch (IOException e) {
			if (getServiceUrl() != null && !getServiceUrl().isEmpty()) {
				setServiceFailed(getServiceUrl(), context);
			} else {
				setServiceFailed("default", context);
			}

			FacesMessage message = new FacesMessage("Service Failed: " + e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(getClientId(), message);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Unknown Error: " + e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_FATAL);
			context.addMessage(getClientId(), message);
		}

		writer.endElement("div");
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
		Resource createResource = context.getApplication().getResourceHandler().createResource("spinner.gif");

		int size = getSize() == null ? 16 : getSize();

		writer.writeAttribute("style", "width:" + size + "px;" + "height:" + size + "px;" + "background-image: url('"
				+ createResource.getRequestPath() + "');" + "background-repeat:no-repeat;"
				+ "background-position:center;", null);

		writer.endElement("div");

		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.append("jsf.ajax.request('" + getClientId(context) + "', null, {'render': '@this'});");

		writer.endElement("script");
	}

	/**
	 * Marks a specific GEO label service for the current request as broken.
	 * Important to not send any further GeoLabel requests in case of an error.
	 * 
	 * @param context
	 */
	private static void setServiceFailed(String endpoint, FacesContext context) {
		ValueExpression expression = context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression.getValue(context.getELContext());
		resourcesBean.setGeoLabelServiceFailed(endpoint);
	}

	/**
	 * Check whether a specific GEO label service is known to be broken for the
	 * current request
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isServiceFailed(String endpoint, FacesContext context) {
		ValueExpression expression = context.getApplication().getExpressionFactory()
				.createValueExpression(context.getELContext(), "#{geoLabelResourcesBean}", GeoLabelResourcesBean.class);
		GeoLabelResourcesBean resourcesBean = (GeoLabelResourcesBean) expression.getValue(context.getELContext());
		return resourcesBean.isGeoLabelServiceFailed(endpoint);
	}

}
