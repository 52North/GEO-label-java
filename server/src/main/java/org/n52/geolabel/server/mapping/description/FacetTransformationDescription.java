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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.mapping.description.FeedbackFacetDescription.ExpertFeedbackFacetDescription;
import org.n52.geolabel.server.mapping.description.FeedbackFacetDescription.UserFeedbackFacetDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({@JsonSubTypes.Type(value = ProducerProfileFacetDescription.class, name = "producerProfile"),
               @JsonSubTypes.Type(value = LineageFacetDescription.class, name = "lineage"),
               @JsonSubTypes.Type(value = ExpertFeedbackFacetDescription.class, name = "expertReview"),
               @JsonSubTypes.Type(value = UserFeedbackFacetDescription.class, name = "userFeedback"),
               @JsonSubTypes.Type(value = ProducerCommentsFacetDescription.class, name = "producerComments"),
               @JsonSubTypes.Type(value = StandardsComplianceFacetDescription.class, name = "standardsCompliance"),
               @JsonSubTypes.Type(value = QualityInformationFacetDescription.class, name = "qualityInformation"),
               @JsonSubTypes.Type(value = CitationsFacetDescription.class, name = "citations")})
public abstract class FacetTransformationDescription<T extends LabelFacet> {

    private static Logger log = LoggerFactory.getLogger(FacetTransformationDescription.class);

    @JsonRootName("hoverover")
    public static class HoveroverWrapper {
        public HoveroverInformation hoverover;
    }

    public static class HoveroverInformation {

        private String facetName;

        private String template;

        private Map<String, String> text;

        public HoveroverInformation() {
            //
        }

        public String getFacetName() {
            return this.facetName;
        }

        public void setFacetName(String factName) {
            this.facetName = factName;
        }

        public String getTemplate() {
            return this.template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public Map<String, String> getText() {
            return this.text;
        }

        public void setText(Map<String, String> text) {
            this.text = text;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("HoveroverInformation [");
            if (this.facetName != null) {
                builder.append("facetName=");
                builder.append(this.facetName);
                builder.append(", ");
            }
            if (this.template != null) {
                builder.append("template=");
                builder.append(this.template);
                builder.append(", ");
            }
            if (this.text != null) {
                builder.append("text=");
                builder.append(this.text);
                builder.append(", ");
            }
            builder.append("]");
            return builder.toString();
        }

    }

    @JsonRootName("drilldown")
    public static class DrilldownWrapper {
        public Drilldown url;
    }

    public static class Drilldown {
        public String url;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Drilldown [");
            if (this.url != null) {
                builder.append("url=");
                builder.append(this.url);
            }
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * functional interface to process the result of an xpath expression in the calling class.
     */
    protected interface ExpressionResultFunction {
        /**
         *
         * @param value
         * @return try if the value was successfully used to create a result
         */
        boolean eval(String value);
    }

    protected String drilldownEndpoint;

    public FacetTransformationDescription(TransformationDescriptionResources resources) {
        this.drilldownEndpoint = resources.getDrilldownEndpoint();
    }

    /**
     * if method throws no exception, evaluation was successful
     */
    protected static void visitExpressionResultStrings(XPathExpression expression,
                                                       Document xml,
                                                       ExpressionResultFunction resultFunction) throws XPathExpressionException {
        if (expression == null) {
            log.debug("Expression is null, not evaluating anything!");
            return;
        }

        Object evaluationResult = expression.evaluate(xml); // for use with saxon: , XPathConstants.NODESET);
        if (evaluationResult instanceof String) {
            String textContent = (String) evaluationResult;
            if ( !resultFunction.eval(textContent.trim()))
                return;
        }
        else if (evaluationResult instanceof NodeList) {
            NodeList nodeList = (NodeList) evaluationResult;
            if (nodeList.getLength() < 1)
                log.debug("Evaluation returned no results for expression '{}' and xml '{}'", expression, xml);
            else
                for (int i = 0, len = nodeList.getLength(); i < len; i++) {
                    Node n = nodeList.item(i);
                    String textContent = n.getTextContent();
                    if (log.isDebugEnabled()) {
                        String t = textContent.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2").replace("\t",
                                                                                                                " ");
                        log.debug("Found content in evaluation of path: {}", t);
                    }
                    if (textContent != null && !resultFunction.eval(textContent.trim()))
                        break;
                }
        }
        else if (evaluationResult instanceof Collection< ? >) {
            ArrayList< ? > resultList = (ArrayList< ? >) evaluationResult;
            if (resultList.size() < 1)
                log.debug("Evaluation returned no results for expression '{}' and xml '{}'", expression, xml);

            for (int i = 0, len = resultList.size(); i < len; i++) {
                String textContent = resultList.get(i).toString();
                if ( !resultFunction.eval(textContent.trim()))
                    break;
            }
        }
        else
            throw new IllegalStateException("XPath generated unexpected result type of "
                    + evaluationResult.getClass().getSimpleName());
    }

    private String availabilityPath;

    private XPathExpression availabilityExpression;

    protected HoveroverInformation hoverover;

    protected Drilldown drilldown;

    private URL originalMetadataUrl;

    public abstract T getAffectedFacet(Label label);

    public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.availabilityPath != null)
            this.availabilityExpression = xPath.compile(this.availabilityPath);
    }

    /**
     * replaces two strings in the URL, first the drilldown endpoint and second the metadata URL (metadata or
     * feedback)
     */
    public T updateDrilldownUrl(final T facet) {
        if (this.drilldown.url != null && this.drilldownEndpoint != null && this.originalMetadataUrl != null) {
            String drilldownURL = String.format(this.drilldown.url, this.drilldownEndpoint, this.originalMetadataUrl);
        facet.setDrilldownURL(drilldownURL);
        }
        else
            log.debug("Could not update drilldown URL with the information provided: {} {} {}",
                      this.drilldown,
                      this.drilldownEndpoint,
                      this.originalMetadataUrl);
        return facet;
    }

    /**
     * method does availability test, both the drilldown url and hoverover are set in subclasses since they
     * can vary.
     */
    public T updateFacet(final T facet, Document metadataXml) throws XPathExpressionException {
        if (this.availabilityExpression == null) {
            log.warn("Availability expression is null, returning faced unchanged: {} for document {}",
                     facet,
                     metadataXml);
            return facet;
        }

        final AtomicBoolean hasTextNodes = new AtomicBoolean(false);
        log.debug("Checking availability of facet {} using {} in document {}",
                  facet.getClass().getSimpleName(),
                  this.availabilityPath,
                  metadataXml);

        visitExpressionResultStrings(this.availabilityExpression, metadataXml, new ExpressionResultFunction() {
            @Override
            public boolean eval(String value) {
                if ( !value.isEmpty() && Boolean.parseBoolean(value)) {
                    hasTextNodes.set(true);
                    return false;
                }
                if ( !value.isEmpty() && !Boolean.parseBoolean(value)) {
                    hasTextNodes.set(false);
                    return true;
                }
                return true;
            }
        });

        boolean hasTextNodesB = hasTextNodes.get();
        Availability availability = hasTextNodesB ? Availability.AVAILABLE : Availability.NOT_AVAILABLE;
        facet.updateAvailability(availability);

        return facet;
    }

    public Label updateLabel(Label label, Document metadataXml) {
        this.originalMetadataUrl = label.getMetadataUrl();

        try {
            T affectedF = getAffectedFacet(label);
            T updatedFacet = updateFacet(affectedF, metadataXml);
            log.debug("Updated facet: {} \n\t\tfor label: {}", updatedFacet, label);
            return label;
        }
        catch (XPathExpressionException e) {
            log.error("Error while executing XPath expression for facet {} in label {}", getClass().getName(), label, e);
            throw new RuntimeException(String.format("Error while executing XPath expression for facet %s in label %s",
                                                     getClass().getName(),
                                                     label), e);
        }
    }

    public String getAvailabilityPath() {
        return this.availabilityPath;
    }

    public void setAvailabilityPath(String availabilityPath) {
        this.availabilityPath = availabilityPath;
    }

    public HoveroverInformation getHoverover() {
        return this.hoverover;
    }

    public void setHoverover(HoveroverInformation hoverover) {
        this.hoverover = hoverover;
    }

    public Drilldown getDrilldown() {
        return this.drilldown;
    }

    public void setDrilldown(Drilldown drilldown) {
        this.drilldown = drilldown;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FacetTransformationDescription [class=");
        builder.append(getClass().getSimpleName());
        builder.append(", ");
        if (this.availabilityPath != null) {
            builder.append("availabilityPath=");
            builder.append(this.availabilityPath);
            builder.append(", ");
        }
        if (this.availabilityExpression != null) {
            builder.append("availabilityExpression=");
            builder.append(this.availabilityExpression);
            builder.append(", ");
        }
        if (this.hoverover != null) {
            builder.append("hoverover=");
            builder.append(this.hoverover);
            builder.append(", ");
        }
        if (this.drilldown != null) {
            builder.append("drilldown=");
            builder.append(this.drilldown);
        }
        builder.append("]");
        return builder.toString();
    }

}
