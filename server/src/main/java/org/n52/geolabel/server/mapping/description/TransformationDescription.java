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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.n52.geolabel.commons.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class TransformationDescription {

    protected static final Logger log = LoggerFactory.getLogger(TransformationDescription.class);

    

    @JsonRootName("transformationDescription")
    public static class TransformationDescriptionWrapper {
        public TransformationDescription transformationDescription;
    }

    public static class NamespaceMapping {

        public String prefix;

        public String namespace;

        NamespaceMapping() {
            //
        }

        public NamespaceMapping(String prefix, String namespace) {
            this.prefix = prefix;
            this.namespace = namespace;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("NamespaceMapping [");
            if (this.prefix != null) {
                builder.append("prefix=");
                builder.append(this.prefix);
                builder.append(", ");
            }
            if (this.namespace != null) {
                builder.append("namespace=");
                builder.append(this.namespace);
            }
            builder.append("]");
            return builder.toString();
        }

    }

    public String name;

    public String applicabilityPath;

    public NamespaceMapping[] namespaceMappings;

    public FacetTransformationDescription< ? >[] facetDescriptions;

    private XPathExpression applicabilityExpression;

    public void initXPaths() throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();

        if (this.namespaceMappings != null) {
            XPath xPath = factory.newXPath();

            final Map<String, String> namespaceMap = new HashMap<>();
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

            if (this.facetDescriptions != null)
                for (FacetTransformationDescription< ? > facetDescription : this.facetDescriptions)
                    facetDescription.initXPaths(xPath);
            else
                log.error("No facet descriptions given, cannot initialize XPaths.");
        }
        else
            log.warn("No mappings defined!");

        if (this.applicabilityPath != null) {
            XPath xPath = factory.newXPath();
            this.applicabilityExpression = xPath.compile(this.applicabilityPath);
            log.debug("Created applicability expresssion {} based on {}", this.applicabilityExpression, this.applicabilityPath);
        }
        else
            log.warn("No applicability expression defined!");
    }

    public boolean updateGeoLabel(Label label, Document metadataXml) {
        if (this.applicabilityExpression != null)
            try {
                Object evaluationResult = this.applicabilityExpression.evaluate(metadataXml);

                if (evaluationResult instanceof String) {
                    String textContent = (String) evaluationResult;
                    boolean value = Boolean.parseBoolean(textContent);

                    if ( !value) {
                        log.debug("TransformationDescription {} is NOT usable for this document.", this.name);
                        return false;
                    }
                }
            }
            catch (XPathExpressionException e) {
                log.debug("Could not evaluate usability expression", e);
            }

        log.debug("TransformationDescription {} is usable for this document, tested with path {}",
                  this.name,
                  this.applicabilityPath);
        for (FacetTransformationDescription< ? > facetDescription : this.facetDescriptions)
            facetDescription.updateLabel(label, metadataXml);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LabelTransformationDescription [");
        if (this.name != null) {
            builder.append("name=");
            builder.append(this.name);
            builder.append(", ");
        }
        if (this.applicabilityPath != null) {
            builder.append("usabilityPath=");
            builder.append(this.applicabilityPath);
            builder.append(", ");
        }
        if (this.namespaceMappings != null) {
            builder.append("namespaceMappings=");
            builder.append(Arrays.toString(this.namespaceMappings));
            builder.append(", ");
        }
        if (this.facetDescriptions != null) {
            builder.append("facetDescriptions=");
            builder.append(Arrays.toString(this.facetDescriptions));
        }
        builder.append("]");
        return builder.toString();
    }

}
