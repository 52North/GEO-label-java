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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.n52.geolabel.commons.FeedbackFacet;
import org.n52.geolabel.commons.Label;
import org.w3c.dom.Document;

/**
 * Base class for feedback availability
 */
@XmlSeeAlso({ FeedbackFacetDescription.UserFeedbackFacetDescription.class,
		FeedbackFacetDescription.ExpertFeedbackFacetDescription.class })
public abstract class FeedbackFacetDescription extends FacetTransformationDescription<FeedbackFacet> {
	@XmlElement
	private String feedbacksCountPath;

	@XmlElement
	private String ratingsPath;

	private XPathExpression ratingsExpression;
	private XPathExpression feedbacksCountExpression;

	@Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.ratingsPath != null)
            this.ratingsExpression = xPath.compile(this.ratingsPath);
        if (this.feedbacksCountPath != null)
            this.feedbacksCountExpression = xPath.compile(this.feedbacksCountPath);
		super.initXPaths(xPath);
	}

	@Override
	public FeedbackFacet updateFacet(final FeedbackFacet facet, Document metadataXml) throws XPathExpressionException {
        visitExpressionResultStrings(this.ratingsExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addRating(Double.parseDouble(value));
				return true;
			}
		});

        visitExpressionResultStrings(this.feedbacksCountExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addFeedbacks(Integer.parseInt(value));
				return true;
			}
		});

		return super.updateFacet(facet, metadataXml);
	}

	/**
	 * * Checks availability of user feedback
	 *
	 */
	@XmlRootElement(name = "userFeedback")
	public static class UserFeedbackFacetDescription extends FeedbackFacetDescription {
		@Override
		public FeedbackFacet getAffectedFacet(Label label) {
			return label.getUserFeedbackFacet();
		}
	}

	/**
	 * @ Checks availability of expert reviews information
	 *
	 */
	@XmlRootElement(name = "expertFeedback")
	public static class ExpertFeedbackFacetDescription extends FeedbackFacetDescription {
		@Override
		public FeedbackFacet getAffectedFacet(Label label) {
			return label.getExpertFeedbackFacet();
		}
	}
}