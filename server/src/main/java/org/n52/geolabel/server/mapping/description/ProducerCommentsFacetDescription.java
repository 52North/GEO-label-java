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

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.ProducerCommentsFacet;
import org.w3c.dom.Document;

/**
 * Checks availability of producer comments information
 * 
 */
@XmlRootElement(name = "producerComments")
public class ProducerCommentsFacetDescription extends FacetTransformationDescription<ProducerCommentsFacet> {
	@XmlElement
	private String producerCommentsPath;

	private XPathExpression producerCommentsExpression;

	@Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
		if (producerCommentsPath != null) {
			producerCommentsExpression = xPath.compile(producerCommentsPath);
		}
		super.initXPaths(xPath);
	}

	@Override
	public ProducerCommentsFacet updateFacet(final ProducerCommentsFacet facet, Document metadataXml)
			throws XPathExpressionException {
		visitExpressionResultStrings(producerCommentsExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addProducerComment(value);
				return true;
			}
		});

		return super.updateFacet(facet, metadataXml);
	}

	@Override
	public ProducerCommentsFacet getAffectedFacet(Label label) {
		return label.getProducerCommentsFacet();
	}

}