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
package org.n52.geolabel.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.n52.geolabel.commons.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoLabelClientV1 extends GeoLabelClient {

    protected static final String DEFAULT_GEO_LABEL_SERVER = "http://www.geolabel.net/api/v1/geolabel";

	/**
	 * Handles requests for LML
	 */
	private static GeoLabelRequestHandler geolabelRequestHandler = new GeoLabelRequestHandler() {

		@Override
		public InputStream getLabel(GeoLabelRequestBuilder labelRequestBuilder) throws IOException {

			boolean useUrlRequest = true;
			// use get lml from xml (post) if any supplied document is not
			// referenced
			// by url
			if (labelRequestBuilder.metadataDocument != null && !labelRequestBuilder.metadataDocument.hasUrl())
                useUrlRequest = false;
            else if (labelRequestBuilder.feedbackDocument != null && !labelRequestBuilder.feedbackDocument.hasUrl())
                useUrlRequest = false;

			String cacheIdentifier = null;
			if (useUrlRequest && labelRequestBuilder.useCache) {
				// Caching
				cacheIdentifier = "";
				if (labelRequestBuilder.metadataDocument != null)
                    cacheIdentifier += labelRequestBuilder.metadataDocument.getUrl().toString();
				if (labelRequestBuilder.feedbackDocument != null)
                    cacheIdentifier += labelRequestBuilder.feedbackDocument.getUrl().toString();

				if (GeoLabelCache.hasSVG(cacheIdentifier))
                    return IOUtils.toInputStream(GeoLabelCache.getSVG(cacheIdentifier));
			}

			if (labelRequestBuilder.forceDownload)
                // force downloading url references if requested
				useUrlRequest = false;

			HttpUriRequest request = null;
			if (useUrlRequest)
                // GET request
				try {
					URIBuilder builder = new URIBuilder(labelRequestBuilder.serviceUrl);

					if (labelRequestBuilder.feedbackDocument != null)
                        builder.setParameter(PARAM_FEEDBACK, labelRequestBuilder.feedbackDocument.getUrl().toString());
					if (labelRequestBuilder.metadataDocument != null)
                        builder.setParameter(PARAM_METADATA, labelRequestBuilder.metadataDocument.getUrl().toString());

					if (labelRequestBuilder.getDesiredSize() != null)
                        builder.setParameter(PARAM_DESIREDSIZE, labelRequestBuilder.getDesiredSize().toString());

                    URI requestUri = builder.build();

                    request = new HttpGet(requestUri);
				} catch (URISyntaxException e) {
					throw new IOException(e);
				}
            else {
				// POST request
				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				if (labelRequestBuilder.feedbackDocument != null)
                    multipartEntity.addPart(PARAM_FEEDBACK,
							new ByteArrayBody(IOUtils.toByteArray(labelRequestBuilder.feedbackDocument.getContent()),
									"text/xml", "file1.xml"));

				if (labelRequestBuilder.metadataDocument != null)
                    multipartEntity.addPart(PARAM_METADATA,
							new ByteArrayBody(IOUtils.toByteArray(labelRequestBuilder.metadataDocument.getContent()),
									"text/xml", "file2.xml"));

				if (labelRequestBuilder.getDesiredSize() != null)
                    multipartEntity.addPart(PARAM_DESIREDSIZE,
							new StringBody("" + labelRequestBuilder.getDesiredSize()));

				HttpPost httpPost = new HttpPost(labelRequestBuilder.serviceUrl);
				httpPost.setEntity(multipartEntity);
				request = httpPost;
			}

			// Issue request
            log.debug("Issuing request {}", request);

			HttpResponse response = HTTPCLIENT.execute(request);
			if (response.getStatusLine().getStatusCode() != 200) {
				InputStream content = response.getEntity().getContent();
				String contentString = IOUtils.toString(content);
				content.close();
				throw new IOException(response.getStatusLine().getReasonPhrase() + ":\n" + contentString);
			}
			HttpEntity entity = response.getEntity();

			if (cacheIdentifier != null) {
				// Caching
				String svgString = IOUtils.toString(entity.getContent());
				GeoLabelCache.putSVG(cacheIdentifier, svgString);
				return IOUtils.toInputStream(svgString);
			}

			return entity.getContent();
		}

	};

    protected final static DefaultHttpClient HTTPCLIENT;

    static Logger log = LoggerFactory.getLogger(GeoLabelClientV1.class);

    protected static final String PARAM_DESIREDSIZE = Constants.PARAM_SIZE;

    protected static final String PARAM_FEEDBACK = Constants.PARAM_FEEDBACK;

    protected static final String PARAM_FEEDBACK_DRILL = Constants.PARAM_FEEDBACK_DRILL;

    protected static final String PARAM_METADATA = Constants.PARAM_METADATA;

    protected static final String PARAM_METADATA_DRILL = Constants.PARAM_METADATA_DRILL;

    static {
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        HTTPCLIENT = new DefaultHttpClient(connectionManager);
    }

    public String createGeoLabelGetUrl(String metadataUrl) {
        return DEFAULT_GEO_LABEL_SERVER + "?" + PARAM_METADATA + "=" + metadataUrl;
    }

    private GeoLabelClientV1() {
        log.debug("NEW {}", this);
    }

    public static GeoLabelRequestBuilder createGeoLabelRequest() {
        return new GeoLabelRequestBuilder(geolabelRequestHandler, DEFAULT_GEO_LABEL_SERVER);
	}

    public static GeoLabelRequestBuilder createGeoLabelRequest(String serviceUrl) {
        return new GeoLabelRequestBuilder(geolabelRequestHandler, serviceUrl);
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GeoLabelClientV1 [");
        builder.append("default=");
        builder.append(DEFAULT_GEO_LABEL_SERVER);
        builder.append("]");
        return builder.toString();
    }

}
