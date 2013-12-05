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
package org.n52.geolabel.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.xml.sax.SAXException;

public class XMLTestCaseLabelBase extends XMLTestCase {

	@Override
	protected void setUp() throws Exception {
		Map<String, String> namespaceMapping = new HashMap<String, String>();
		namespaceMapping.put("svg", "http://www.w3.org/2000/svg");
		namespaceMapping.put("xlink", "http://www.w3.org/1999/xlink");
		XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaceMapping));
	}

	protected void performCommonLabelChecks(String testSVG, int size) throws XpathException, SAXException, IOException {
		// Check svg version
		assertXpathEvaluatesTo("1.1", "/svg:svg/@version", testSVG);

		// Check size
		assertXpathEvaluatesTo(size + "", "/svg:svg/@width", testSVG);
		assertXpathEvaluatesTo(size + "", "/svg:svg/@height", testSVG);

		// Check GVQ link
		assertXpathEvaluatesTo("http://www.geoviqua.org", "//svg:g[@id='branding']/svg:a/@xlink:href", testSVG);
	}

	/**
	 * Checks if facets are specified with the correct background color based on
	 * its availability state (excluding higher availability)
	 * 
	 * @param testSVG
	 * @param availableFacets
	 * @throws XpathException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected void performFacetChecks(String testSVG, EnumSet<Facet> availableFacets) throws XpathException,
			SAXException, IOException {
		// Check general facet availability
		for (Facet facet : availableFacets) {
			try {
				assertXpathEvaluatesTo(facet.getColorAvailable(), "/svg:svg//svg:g[@id='" + facet.name().toLowerCase()
						+ "']/svg:a/svg:path[1]/@fill", testSVG);
			} catch (AssertionFailedError e) {
				throw new AssertionError("Facet " + facet + " not reported as available", e);
			}
		}

		for (Facet facet : EnumSet.complementOf(availableFacets)) {
			try {
				assertXpathEvaluatesTo(facet.getColorNotAvailable(), "/svg:svg//svg:g[@id='"
						+ facet.name().toLowerCase() + "']/svg:a/svg:path[1]/@fill", testSVG);
			} catch (AssertionFailedError e) {
				throw new AssertionError("Facet " + facet + " not reported as not available", e);
			}
		}
	}

	
	protected void performFacetComparison(String testSVG, String controlSVG) throws XpathException, SAXException,
			IOException {
		for (Facet facet : EnumSet.allOf(Facet.class)) {
			try {
				String xPath = "/svg:svg//svg:g[@id='" + facet.name().toLowerCase() + "']/svg:a/svg:path[1]/@fill";
				assertXpathsEqual(xPath, controlSVG, xPath, testSVG);
			} catch (AssertionFailedError e) {
				throw new AssertionError("Facet " + facet + " has errors", e);
			}
		}

		
		
	}

	protected String getTestServiceUrl() {
		String port = System.getProperty("jettyPort");
		if (port == null) {
			throw new RuntimeException("Jetty port system property not set");
		}
		return "http://localhost:" + port + "/api/v1/";
	}

}
