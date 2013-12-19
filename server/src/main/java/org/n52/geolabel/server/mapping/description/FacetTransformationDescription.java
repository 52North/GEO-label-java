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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder.Type;
import org.n52.geolabel.server.mapping.description.FeedbackFacetDescription.ExpertReviewFacetDescription;
import org.n52.geolabel.server.mapping.description.FeedbackFacetDescription.UserFeedbackFacetDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({@JsonSubTypes.Type(value = ProducerProfileFacetDescription.class, name = "producerProfile"),
               @JsonSubTypes.Type(value = LineageFacetDescription.class, name = "lineage"),
               @JsonSubTypes.Type(value = ExpertReviewFacetDescription.class, name = "expertReview"),
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

    public static class TypedPlaceholder {

        public enum Type {
            STRING, DECIMAL, UNKNOWN
        }

        public String placeholder;
        public Type type;

        public TypedPlaceholder(String placeholder, Type type) {
            super();
            this.placeholder = placeholder;
            this.type = type;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TypedPlaceholder [placeholder=");
            builder.append(this.placeholder);
            builder.append(", ");
            builder.append("type=");
            builder.append(this.type);
            builder.append("]");
            return builder.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( (this.placeholder == null) ? 0 : this.placeholder.hashCode());
            result = prime * result + ( (this.type == null) ? 0 : this.type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TypedPlaceholder other = (TypedPlaceholder) obj;
            if (this.placeholder == null) {
                if (other.placeholder != null)
                    return false;
            }
            else if ( !this.placeholder.equals(other.placeholder))
                return false;
            if (this.type != other.type)
                return false;
            return true;
        }

        public boolean isNumber() {
            return this.placeholder.equals("%d") || this.placeholder.equals("%f");
        }

    }

    public static class HoveroverInformation {

        private String facetName;

        private String template;

        private Map<String, String> text;

        private ArrayList<TypedPlaceholder> typedPlaceholders;

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

            this.typedPlaceholders = getTypedPlacehoders(this);
        }

        public Map<String, String> getText() {
            return this.text;
        }

        public void setText(Map<String, String> text) {
            this.text = text;
        }

        public ArrayList<TypedPlaceholder> getTypedPlaceholders() {
            if (this.typedPlaceholders == null)
                this.typedPlaceholders = getTypedPlacehoders(this);
            return this.typedPlaceholders;
        }

        private ArrayList<TypedPlaceholder> getTypedPlacehoders(HoveroverInformation hoveroverInformation) {
            ArrayList<TypedPlaceholder> list = new ArrayList<>();
            String templ = hoveroverInformation.getTemplate();

            ArrayList<String> placeholders = new ArrayList<>();
            for (int i = -1; (i = templ.indexOf("%", i + 1)) != -1;)
                placeholders.add(templ.substring(i, i + 2).trim());
            for (String s : placeholders) {
                Type t = null;
                switch (s) {
                case "%s":
                    t = Type.STRING;
                    break;
                case "%d":
                    t = Type.DECIMAL;
                    break;
                default:
                    t = Type.UNKNOWN;
                    break;
                }
                list.add(new TypedPlaceholder(s, t));
            }

            return list;
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
         * @return true if the value was successfully used to create a result or a default value has been set
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

        Object evaluationResult = expression.evaluate(xml); // for use with saxon respectively XPath 2.0: ,
                                                            // XPathConstants.NODESET);
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

    protected Map<String, XPathExpression> hoveroverExpressions = new HashMap<>();

    protected HoveroverInformation hoverover;

    protected Drilldown drilldown;

    protected URL originalMetadataUrl;

    protected URL originalFeedbackUrl;

    public abstract T getAffectedFacet(Label label);

    public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.availabilityPath != null)
            this.availabilityExpression = xPath.compile(this.availabilityPath);

        for (Entry<String, String> e : this.hoverover.getText().entrySet()) {
            XPathExpression expression = xPath.compile(e.getValue());
            log.debug("Storing expression {} for {}", expression, e.getKey());
            this.hoveroverExpressions.put(e.getKey(), expression);
        }
    }

    private T updateDrilldownUrl(T facet, URL metadataOrFeedbackUrl) {
        if (this.drilldown.url != null && this.drilldownEndpoint != null && metadataOrFeedbackUrl != null) {
            String drilldownURL = String.format(this.drilldown.url, this.drilldownEndpoint, metadataOrFeedbackUrl);
            facet.setHref(drilldownURL);
        }
        else
            log.debug("Could not update drilldown URL with the information provided: {} {} {}",
                      this.drilldown,
                      this.drilldownEndpoint,
                      this.originalMetadataUrl);
        return facet;
    }

    /**
     * replaces two strings in the URL, first the drilldown endpoint and second the metadata URL
     */
    public T updateDrilldownUrlWithMetadata(final T facet) {
        return updateDrilldownUrl(facet, this.originalMetadataUrl);
    }

    /**
     * replaces two strings in the URL, first the drilldown endpoint and second the feedback URL
     */
    public T updateDrilldownUrlWithFeedback(final T facet) {
        return updateDrilldownUrl(facet, this.originalFeedbackUrl);
    }

    /**
     * set the hoverover text based on hover templates and expressions
     *
     * @throws XPathExpressionException
     */
    public T updateHoverover(final T facet, Document metadataXml) throws XPathExpressionException {
        if (this.hoverover.getTemplate() != null) {
            Map<String, String> texts = this.hoverover.getText();
            String template = this.hoverover.getTemplate();
            // just format numbers as strings
            // template = template.replace("%d", "%s");

            ArrayList<TypedPlaceholder> typedPlaceholders = this.hoverover.getTypedPlaceholders();

            if (typedPlaceholders.size() != texts.size())
                log.warn("template placeholder count differs from replacement string count.");

            // get the evaluated values
            final ArrayList<Object> values = new ArrayList<>();

            for (int i = 0; i < texts.size(); i++) {
                String key = texts.keySet().iterator().next();
                TypedPlaceholder tp = typedPlaceholders.get(i);
                XPathExpression expression = this.hoveroverExpressions.get(key);

                if (tp.isNumber())
                    visitExpressionResultStrings(expression, metadataXml, new ExpressionResultFunction() {
                        @Override
                        public boolean eval(String value) {
                            if ( !value.isEmpty()) {
                                try {
                                    Number number = NumberFormat.getInstance().parse(value);
                                    values.add(number);
                                }
                                catch (ParseException e) {
                                    // log.error("Could not parse number returned by expression.");
                                    values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
                                }
                                return false;
                            }
                            values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
                            return true;
                        }
                    });
                else
                    visitExpressionResultStrings(expression, metadataXml, new ExpressionResultFunction() {
                        @Override
                        public boolean eval(String value) {
                            if ( !value.isEmpty()) {
                                values.add(value);
                                return false;
                            }
                            values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_TEXT);
                            return true;
                        }
                    });
            }

            final StringBuilder sb = new StringBuilder();
            sb.append(this.hoverover.getFacetName());

            if (typedPlaceholders.size() != values.size())
                log.warn("template placeholder count differs from result strings of xpath evaluations.");
            else {
                sb.append("\n");
                sb.append(String.format(template, values.toArray()));
            }

            facet.setTitle(sb.toString());
        }
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

        updateHoverover(facet, metadataXml);

        return facet;
    }

    public Label updateLabel(Label label, Document metadataXml) {
        // must be set for the subclasses to use:
        this.originalMetadataUrl = label.getMetadataUrl();
        this.originalFeedbackUrl = label.getFeedbackUrl();

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
