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
package org.n52.geolabel.server.mapping.description;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.n52.geolabel.commons.Label;
import org.w3c.dom.Document;

@XmlRootElement(name = "transformationDescription")
public class LabelTransformationDescription {

	static class NamespaceMapping {
		@XmlAttribute
		public String prefix;
		@XmlAttribute
		public String namespace;

		NamespaceMapping() {
		}

		public NamespaceMapping(String prefix, String namespace) {
			this.prefix = prefix;
			this.namespace = namespace;
		}
	}

	@XmlElementWrapper
	@XmlElement(name = "mapping")
	NamespaceMapping[] namespaceMappings;

	@XmlElementWrapper
	@XmlElementRef
	FacetTransformationDescription<?>[] facetDescriptions;

	public void initXPaths() throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();

		XPath xPath = factory.newXPath();

		final Map<String, String> namespaceMap = new HashMap<String, String>();
        for (NamespaceMapping mapping : this.namespaceMappings)
            namespaceMap.put(mapping.prefix, mapping.namespace);

		xPath.setNamespaceContext(new NamespaceContext() {

			@SuppressWarnings("rawtypes")
			@Override
			public Iterator getPrefixes(String namespaceURI) {
				return null;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				return null;
			}

			@Override
			public String getNamespaceURI(String prefix) {
				return namespaceMap.get(prefix);
			}
		});

        for (FacetTransformationDescription< ? > facetDescription : this.facetDescriptions)
            facetDescription.initXPaths(xPath);
	}

	public void updateGeoLabel(Label label, Document metadataXml) {
        for (FacetTransformationDescription< ? > facetDescription : this.facetDescriptions)
            facetDescription.updateLabel(label, metadataXml);
	}

}
