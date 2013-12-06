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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

/**
 * Provides multiple classes to wrap external data resources.
 */
public interface DocumentReference {

	/**
	 * Provides functionalities to wrap different kinds of data sources into a
	 * common layout. Resources should be provided via URL, and have to be
	 * accessible as {@link InputStream}
	 */
	public abstract class Base {

		@SuppressWarnings("unused")
        public InputStream getContent() throws IOException {
			return null;
		}

		public boolean hasUrl() {
			return false;
		}

		public URL getUrl() {
			return null;
		}

	}

	/**
	 * Provides resource via URL
	 */
	public class URLDocument extends Base {
		protected final URL documentUrl;

		public URLDocument(URL documentUrl) {
			this.documentUrl = documentUrl;
		}

		public URLDocument(String documentUrl) throws MalformedURLException {
			this.documentUrl = new URL(documentUrl);
		}

		@Override
		public boolean hasUrl() {
            return this.documentUrl != null;
		}

		@Override
		public URL getUrl() {
            return this.documentUrl;
		}

		@Override
		public InputStream getContent() throws IOException {
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity responseEntity = httpClient.execute(new HttpGet(this.documentUrl.toString())).getEntity();
			return responseEntity.getContent();
		}
	}

	/**
	 * Provides resource from {@link InputStream}
	 */
	public class InputStreamDocument extends Base {
		protected final InputStream documentStream;

		public InputStreamDocument(InputStream documentStream) {
			this.documentStream = documentStream;
		}

		@Override
		public InputStream getContent() {
            if (this.documentStream != null)
                return this.documentStream;
			return null;
		}

	}

	/**
	 * Provides resource from XML {@link Document}
	 */
	public class XMLDocument extends Base {
		protected final Document documentXml;

		public XMLDocument(Document documentXml) {
			this.documentXml = documentXml;
		}

		@Override
		public InputStream getContent() throws IOException {
            if (this.documentXml != null) {
				ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
                Source xmlSource = new DOMSource(this.documentXml);
				Result outputTarget = new StreamResult(resultStream);
				try {
					TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
				} catch (Exception e) {
					// TransformerExceptions etc.
					throw new IOException(e);
				}
				return new ByteArrayInputStream(resultStream.toByteArray());
			}
			return null;
		}

	}

}