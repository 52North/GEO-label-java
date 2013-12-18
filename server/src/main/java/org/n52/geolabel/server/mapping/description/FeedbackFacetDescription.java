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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JacksonInject;
import org.n52.geolabel.commons.FeedbackFacet;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Base class for feedback availability
 */
public abstract class FeedbackFacetDescription extends FacetTransformationDescription<FeedbackFacet> {

    protected static Logger log = LoggerFactory.getLogger(FeedbackFacetDescription.class);

    private String feedbacksCountPath;

    private String ratingsPath;

    private XPathExpression ratingsExpression;
    private XPathExpression feedbacksCountExpression;

    public FeedbackFacetDescription(TransformationDescriptionResources resources) {
        super(resources);
    }

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

        FeedbackFacet f = super.updateDrilldownUrlWithFeedback(facet);
        return super.updateFacet(f, metadataXml);
    }

    /**
     * Checks availability of user feedback
     */
    public static class UserFeedbackFacetDescription extends FeedbackFacetDescription {

        @JsonCreator
        public UserFeedbackFacetDescription(@JacksonInject
        TransformationDescriptionResources resources) {
            super(resources);
            log.debug("NEW {}", this);
        }

        @Override
        public FeedbackFacet getAffectedFacet(Label label) {
            return label.getUserFeedbackFacet();
        }
    }

    /**
     * Checks availability of expert reviews information
     */
    public static class ExpertReviewFacetDescription extends FeedbackFacetDescription {

        @JsonCreator
        public ExpertReviewFacetDescription(@JacksonInject
        TransformationDescriptionResources resources) {
            super(resources);
            log.debug("NEW {}", this);
        }

        @Override
        public FeedbackFacet getAffectedFacet(Label label) {
            return label.getExpertFeedbackFacet();
        }
    }
}