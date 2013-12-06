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

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@XmlSeeAlso({ ProducerProfileFacetDescription.class,
		LineageFacetDescription.class, ProducerCommentsFacetDescription.class,
		StandardsComplianceFacetDescription.class,
		QualityInformationFacetDescription.class,
		FeedbackFacetDescription.class, CitationsFacetDescription.class })
public abstract class FacetTransformationDescription<T extends LabelFacet> {

	protected interface ExpressionResultFunction {
		boolean eval(String value);
	}

	protected static void visitExpressionResultStrings(
			XPathExpression expression, Document xml,
			ExpressionResultFunction resultFunction)
			throws XPathExpressionException {
		if (expression == null)
            return;

		Object evaluationResult = expression.evaluate(xml,
				XPathConstants.NODESET);
		if (evaluationResult instanceof NodeList) {
			NodeList nodeList = (NodeList) evaluationResult;
			for (int i = 0, len = nodeList.getLength(); i < len; i++) {
                String textContent = nodeList.item(i).getFirstChild().getNodeValue(); // getTextContent();
				if (textContent != null
						&& !resultFunction.eval(textContent.trim()))
                    break;
			}
		} else if (evaluationResult instanceof Collection<?>) {
			ArrayList<?> resultList = (ArrayList<?>) evaluationResult;
			for (int i = 0, len = resultList.size(); i < len; i++) {
				String textContent = resultList.get(i).toString();
				if (!resultFunction.eval(textContent.trim()))
                    break;
			}
		}
        else
            throw new IllegalStateException(
					"XPath generated unexpected result type of "
							+ evaluationResult.getClass().getSimpleName());
	}

	@XmlElement
	private String availabilityPath;

	private XPathExpression availabilityExpression;

	public abstract T getAffectedFacet(Label label);

	public void initXPaths(XPath xPath) throws XPathExpressionException {
		if (this.availabilityPath != null)
			this.availabilityExpression = xPath.compile(this.availabilityPath);
	}

	public T updateFacet(final T facet, Document metadataXml)
			throws XPathExpressionException {
		if (this.availabilityExpression == null)
			return facet;

		final AtomicBoolean hasTextNodes = new AtomicBoolean(false);
		visitExpressionResultStrings(this.availabilityExpression, metadataXml,
				new ExpressionResultFunction() {
					@Override
					public boolean eval(String value) {
						if (!value.isEmpty()) {
							hasTextNodes.set(true);
							return false;
						}
						return true;
					}
				});

		facet.updateAvailability(hasTextNodes.get() ? Availability.AVAILABLE
				: Availability.NOT_AVAILABLE);

		return facet;
	}

	public Label updateLabel(Label label, Document metadataXml) {
		try {
			updateFacet(getAffectedFacet(label), metadataXml);
			return label;
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Error while executing XPath expression", e);
		}

	}
}
