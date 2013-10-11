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
package org.n52.geolabel.server.mapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.mapping.description.LabelTransformationDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Transforms metadata XML into {@link Label} representations
 * 
 */
@Singleton
public class MetadataTransformer {

	private static final String TRANSFORMATIONS_RESOURCE = "transformations";
	final static Logger log = LoggerFactory.getLogger(MetadataTransformer.class);

	public static int CACHE_MAX_LABELS = 100; // TODO make available as property
	public static int CACHE_MAX_HOURS = 48;// TODO make available as property

	/**
	 * Acts as key for caching {@link Label}s based on its metadata and/or
	 * feedback source. Since metadata/feedback sources are handled equally, all
	 * combinations of equal metadata/feedback source urls are identical.
	 * 
	 */
	@XmlRootElement(name = "CacheMapping")
	public static class LabelUrlKey {
		protected URL metadataUrl;
		protected URL feedbackUrl;
		private Date cacheWriteTime;

		LabelUrlKey() {
		}

		public LabelUrlKey(URL metadataUrl, URL feedbackUrl) {
			this.metadataUrl = metadataUrl;
			this.feedbackUrl = feedbackUrl;
		}

		@XmlAttribute
		public URL getFeedbackUrl() {
			return feedbackUrl;
		}

		@XmlAttribute
		public URL getMetadataUrl() {
			return metadataUrl;
		}

		@XmlAttribute(name = "cachedAt")
		public Date getCacheWriteTime() {
			return cacheWriteTime;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof LabelUrlKey) {
				LabelUrlKey other = (LabelUrlKey) obj;

				if ((this.metadataUrl != null && this.metadataUrl.equals(other.metadataUrl) || this.metadataUrl == other.metadataUrl)
						&& (this.feedbackUrl != null && this.feedbackUrl.equals(other.feedbackUrl) || this.feedbackUrl == other.feedbackUrl))
					return true;

				if ((this.metadataUrl != null && this.metadataUrl.equals(other.feedbackUrl) || this.metadataUrl == other.feedbackUrl)
						&& (this.feedbackUrl != null && this.feedbackUrl.equals(other.metadataUrl) || this.feedbackUrl == other.metadataUrl))
					return true;
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			int hash = 0;
			if (this.metadataUrl != null)
				hash += this.metadataUrl.hashCode();
			if (this.feedbackUrl != null)
				hash += this.feedbackUrl.hashCode();

			return hash;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("metadata url: ");
			if (this.metadataUrl != null)
				builder.append(this.metadataUrl);
			builder.append("feedback url: ");
			if (this.feedbackUrl != null)
				builder.append(this.feedbackUrl);
			return builder.toString();
		}
	}

	private LoadingCache<LabelUrlKey, Label> labelUrlCache = CacheBuilder.newBuilder().maximumSize(CACHE_MAX_LABELS)
			.expireAfterWrite(CACHE_MAX_HOURS, TimeUnit.HOURS).build(new CacheLoader<LabelUrlKey, Label>() {
				@Override
				public Label load(LabelUrlKey key) throws Exception {
					key.cacheWriteTime = new Date();
					log.info("Generating new GEO label for cache from urls {}", key);

					Label label = new Label();

					if (key.feedbackUrl != null) {
						updateGeoLabel(key.feedbackUrl, label);
					}
					if (key.metadataUrl != null) {
						updateGeoLabel(key.metadataUrl, label);
					}

					return label;
				}
			});

	@Inject
	MetadataTransformer() {

	}

	private List<LabelTransformationDescription> transformationDescriptions;

	public List<LabelTransformationDescription> getTransformationDescriptions() {
		if (this.transformationDescriptions == null) {
			this.transformationDescriptions = new ArrayList<LabelTransformationDescription>();
		}
		return this.transformationDescriptions;
	}

	/**
	 * Reads {@link LabelTransformationDescription} from passed XML stream.
	 * 
	 * @param input
	 * @throws IOException
	 */
	public void readTransformationDescription(InputStream input) throws IOException {
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(LabelTransformationDescription.class)
					.createUnmarshaller();
			LabelTransformationDescription transformationDescription = (LabelTransformationDescription) unmarshaller
					.unmarshal(input);

			transformationDescription.initXPaths();
			getTransformationDescriptions().add(transformationDescription);
		} catch (JAXBException e) {
			throw new IOException("Error while parsing transformation description stream", e);
		} catch (XPathExpressionException e) {
			throw new IOException("Error while compiling XPaths in tranformation description", e);
		} finally {
			input.close();
		}

	}

	/**
	 * Reads {@link LabelTransformationDescription} from passed XML {@link File}
	 * .
	 * 
	 * @param descriptionFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readTransformationDescription(File descriptionFile) throws FileNotFoundException, IOException {
		readTransformationDescription(new FileInputStream(descriptionFile));
	}

	/**
	 * Reads {@link LabelTransformationDescription}s from resources.
	 * 
	 * @throws IOException
	 */
	private void readTransformationDescriptions() throws IOException {
		Enumeration<URL> descriptionResources = getClass().getClassLoader().getResources(TRANSFORMATIONS_RESOURCE);
		while (descriptionResources.hasMoreElements()) {
			try {
				URI descriptionResourceURI = descriptionResources.nextElement().toURI();
				File descriptionResourceFile = new File(descriptionResourceURI);
				for (File descriptionFile : descriptionResourceFile.listFiles()) {
					try {
						readTransformationDescription(descriptionFile);
					} catch (IOException e) {
						log.error("Could not read transformation description " + descriptionFile.getName() + ".", e);
					}
				}

			} catch (URISyntaxException e) {
				log.error("Could not read transformation description.", e);
			}
		}
	}

	/**
	 * Reads the supplied metadata XML stream and applies all loaded
	 * transformation descriptions to update a {@link Label} instance
	 * 
	 * @param xml
	 * @param label
	 * @return
	 * @throws IOException
	 *             If reading XML stream or initial loading of transformation
	 *             descriptions fails
	 */
	public Label updateGeoLabel(InputStream xml, Label label) throws IOException {
		if (this.transformationDescriptions == null) {
			readTransformationDescriptions();
		}

		Document doc;
		try {
			// Read supplied metadata xml
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			doc = builder.parse(xml);
		} catch (Exception e) {
			throw new IOException("Could not parse supplied metadata xml", e);
		}

		for (LabelTransformationDescription transformer : getTransformationDescriptions()) {
			transformer.updateGeoLabel(label, doc);
		}

		return label;
	}

	/**
	 * See {@link MetadataTransformer#updateGeoLabel(InputStream, Label)}.Loads
	 * metadata stream from {@link URL}.
	 * 
	 * @param metadataUrl
	 * @param label
	 * @return
	 * @throws IOException
	 */
	public Label updateGeoLabel(URL metadataUrl, Label label) throws IOException {
		URLConnection con = metadataUrl.openConnection();
		con.setConnectTimeout(GeoLabelConfig.CONNECT_TIMEOUT);
		con.setReadTimeout(GeoLabelConfig.READ_TIMEOUT);
		return updateGeoLabel(con.getInputStream(), label);
	}

	/**
	 * Returns new {@link Label} instance from supplied metadata XML stream. See
	 * {@link MetadataTransformer#updateGeoLabel(InputStream, Label)}
	 * 
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	public Label createGeoLabel(InputStream xml) throws IOException {
		return updateGeoLabel(xml, new Label());
	}

	/**
	 * Returns new {@link Label} instance from supplied metadata XML {@link URL}
	 * reference. See
	 * {@link MetadataTransformer#updateGeoLabel(InputStream, Label)}
	 * 
	 * @param metadataUrl
	 * @return
	 * @throws IOException
	 */
	public Label createGeoLabel(URL metadataUrl) throws IOException {
		return updateGeoLabel(metadataUrl, new Label());
	}

	/**
	 * Returns a {@link Label} from metadata and/or feedback {@link URL}.
	 * Results are cached.
	 * 
	 * @param metadataURL
	 * @param feedbackURL
	 * @return
	 * @throws IOException
	 */
	public Label getLabel(URL metadataURL, URL feedbackURL) throws IOException {
		try {
			return this.labelUrlCache.get(new LabelUrlKey(metadataURL, feedbackURL));
		} catch (ExecutionException e) {
			throw new IOException(e.getCause());
		}
	}

	public Set<LabelUrlKey> getCacheContent() {
		return labelUrlCache.asMap().keySet();
	}
}
