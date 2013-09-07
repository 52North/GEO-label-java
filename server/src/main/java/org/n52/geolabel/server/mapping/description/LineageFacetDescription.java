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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LineageFacet;
import org.w3c.dom.Document;

/**
 * Checks availability of lineage information
 * 
 */
@XmlRootElement(name = "lineage")
public class LineageFacetDescription extends FacetTransformationDescription<LineageFacet> {
	@XmlElement
	private String processStepCountPath;

	private XPathExpression processStepCountExpression;

	@Override
	public void initXPaths(XPath xPath) throws XPathExpressionException {
		if (processStepCountPath != null)
			processStepCountExpression = xPath.compile(processStepCountPath);
		super.initXPaths(xPath);
	}

	@Override
	public LineageFacet updateFacet(LineageFacet facet, Document metadataXml) throws XPathExpressionException {
		if (processStepCountExpression != null) {
			Double result = (Double) processStepCountExpression.evaluate(metadataXml, XPathConstants.NUMBER);
			if (result != null) {
				facet.addProcessSteps(result.intValue());
			}
		}

		return super.updateFacet(facet, metadataXml);
	}

	@Override
	public LineageFacet getAffectedFacet(Label label) {
		return label.getLineageFacet();
	}

}