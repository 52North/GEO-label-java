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

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JacksonInject;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.ProducerCommentsFacet;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Checks availability of producer comments information
 *
 */
public class ProducerCommentsFacetDescription extends FacetTransformationDescription<ProducerCommentsFacet> {

    private static Logger log = LoggerFactory.getLogger(ProducerCommentsFacetDescription.class);

    private String producerCommentsPath;

    private XPathExpression producerCommentsExpression;

    @JsonCreator
    public ProducerCommentsFacetDescription(@JacksonInject
    TransformationDescriptionResources resources) {
        super(resources);
        log.debug("NEW {}", this);
    }

    // @Override
    // public void initXPaths(XPath xPath) throws XPathExpressionException {
    // // if (this.producerCommentsPath != null)
    // // this.producerCommentsExpression = xPath.compile(this.producerCommentsPath);
    // super.initXPaths(xPath);
    // }

    @Override
    public ProducerCommentsFacet updateFacet(final ProducerCommentsFacet facet, Document metadataXml) throws XPathExpressionException {
        ProducerCommentsFacet f = super.updateDrilldownUrlWithMetadata(facet);
        return super.updateFacet(f, metadataXml);
    }

    @Override
    public ProducerCommentsFacet getAffectedFacet(Label label) {
        return label.getProducerCommentsFacet();
    }

}