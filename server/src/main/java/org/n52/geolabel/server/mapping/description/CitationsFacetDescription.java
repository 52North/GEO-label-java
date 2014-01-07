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
import org.n52.geolabel.commons.CitationsFacet;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Checks availability of citations information
 */
public class CitationsFacetDescription extends FacetTransformationDescription<CitationsFacet> {

    private static Logger log = LoggerFactory.getLogger(CitationsFacetDescription.class);

    private String citationsCountPath;

	private XPathExpression citationsCountExpression;

    @JsonCreator
    public CitationsFacetDescription(@JacksonInject
    TransformationDescriptionResources resources) {
        super(resources);
        log.debug("NEW {}", this);
    }

    @Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
        if (this.citationsCountPath != null)
            this.citationsCountExpression = xPath.compile(this.citationsCountPath);
		super.initXPaths(xPath);
	}

	@Override
	public CitationsFacet updateFacet(final CitationsFacet facet, Document metadataXml) throws XPathExpressionException {
        visitExpressionResultStrings(this.citationsCountExpression, metadataXml, new ExpressionResultFunction() {
			@Override
			public boolean eval(String value) {
				facet.addCitations(Integer.parseInt(value));
				return true;
			}
		});

        // is the only facet that needs both urls, so create drilldown link here:
        if (this.drilldown.url != null && this.drilldownEndpoint != null && this.originalFeedbackUrl != null
                && this.originalMetadataUrl != null) {
            String drilldownURL = String.format(this.drilldown.url,
                                                this.drilldownEndpoint,
                                                this.originalMetadataUrl,
                                                this.originalFeedbackUrl);
            facet.setHref(drilldownURL);
        }

		return super.updateFacet(facet, metadataXml);
	}

	@Override
	public CitationsFacet getAffectedFacet(Label label) {
		return label.getCitationsFacet();
	}

    public String getCitationsCountPath() {
        return this.citationsCountPath;
    }

    public void setCitationsCountPath(String citationsCountPath) {
        this.citationsCountPath = citationsCountPath;
    }

}