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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.n52.geolabel.commons.CitationsFacet;
import org.n52.geolabel.commons.Label;
import org.w3c.dom.Document;

/**
 * Checks availability of citations information
 */
@XmlRootElement(name = "citations")
public class CitationsFacetDescription extends FacetTransformationDescription<CitationsFacet> {
	@XmlElement
	private String citationsCountPath;

	private XPathExpression citationsCountExpression;

	@Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.citationsCountPath != null)
            this.citationsCountExpression = xPath.compile(this.citationsCountPath);
		super.initXPaths(xPath);
	}

	@Override
	public CitationsFacet updateFacet(final CitationsFacet facet, Document metadataXml) throws XPathExpressionException {
        visitExpressionResultStrings(this.citationsCountExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addCitations(Integer.parseInt(value));
				return true;
			}
		});

		return super.updateFacet(facet, metadataXml);
	}

	@Override
	public CitationsFacet getAffectedFacet(Label label) {
		return label.getCitationsFacet();
	}

}