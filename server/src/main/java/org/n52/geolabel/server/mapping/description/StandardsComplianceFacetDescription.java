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
import org.n52.geolabel.commons.StandardsComplianceFacet;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Checks availability of standards compliance information
 */
public class StandardsComplianceFacetDescription extends FacetTransformationDescription<StandardsComplianceFacet> {

    private static Logger log = LoggerFactory.getLogger(StandardsComplianceFacetDescription.class);

    private String standardsPath;

	private XPathExpression standardsExpression;

    @JsonCreator
    public StandardsComplianceFacetDescription(@JacksonInject
    TransformationDescriptionResources resources) {
        super(resources);
        log.debug("NEW {}", this);
    }

    @Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.standardsPath != null)
            this.standardsExpression = xPath.compile(this.standardsPath);
		super.initXPaths(xPath);
	}

	@Override
	public StandardsComplianceFacet updateFacet(final StandardsComplianceFacet facet, Document metadataXml)
			throws XPathExpressionException {
        visitExpressionResultStrings(this.standardsExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addStandard(value);
				return true;
			}
		});

        StandardsComplianceFacet f = super.updateDrilldownUrlWithMetadata(facet);
        return super.updateFacet(f, metadataXml);
	}

	@Override
	public StandardsComplianceFacet getAffectedFacet(Label label) {
		return label.getStandardsComplianceFacet();
	}

}