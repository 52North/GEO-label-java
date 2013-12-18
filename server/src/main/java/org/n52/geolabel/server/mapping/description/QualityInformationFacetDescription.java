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
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.QualityInformationFacet;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Checks availability of quality information
 *
 */
public class QualityInformationFacetDescription extends
		FacetTransformationDescription<QualityInformationFacet> {

    private static Logger log = LoggerFactory.getLogger(ProducerProfileFacetDescription.class);

    private String scopeLevelPath;

	private XPathExpression scopeLevelExpression;

    @JsonCreator
    public QualityInformationFacetDescription(@JacksonInject
    TransformationDescriptionResources resources) {
        super(resources);
        log.debug("NEW {}", this);
    }

    @Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.scopeLevelPath != null)
            this.scopeLevelExpression = xPath.compile(this.scopeLevelPath);
		super.initXPaths(xPath);
	}

	@Override
	public QualityInformationFacet updateFacet(
			final QualityInformationFacet facet, Document metadataXml)
			throws XPathExpressionException {
        visitExpressionResultStrings(this.scopeLevelExpression, metadataXml,
				new ExpressionResultFunction() {
					@Override
					public boolean eval(String value) {
						facet.addScopeLevel(value);
						return true;
					}
				});

        QualityInformationFacet f = super.updateDrilldownUrl(facet);
        return super.updateFacet(f, metadataXml);
	}

	@Override
	public QualityInformationFacet getAffectedFacet(Label label) {
		return label.getQualityInformationFacet();
	}

}