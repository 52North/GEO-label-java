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
import java.net.MalformedURLException;
import java.util.EnumSet;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.n52.geolabel.client.GeoLabelClientV1;
import org.n52.geolabel.client.GeoLabelRequestBuilder;
import org.xml.sax.SAXException;

/**
 * Tests against remote geolabel service.
 * 
 * TODO Currently commented because of problems with test service or client
 * library...
 * 
 */
public class ExampleMetadataComparisonIT extends XMLTestCaseLabelBase {

	@Test
	public void testFAO_GEONETWORK() throws MalformedURLException, IOException, XpathException, SAXException {
		// testMetadataExample("FAO_GEO_Network_iso19139.xml",
		// EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS,
		// Facet.LINEAGE, Facet.STANDARDS_COMPLIANCE,
		// Facet.QUALITY_INFORMATION));
	}

	/*
	 * public void testFGDC_Producer() throws MalformedURLException,
	 * IOException, XpathException, SAXException {
	 * testMetadataExample("FGDC_Producer.xml",
	 * EnumSet.of(Facet.PRODUCER_PROFILE, Facet.STANDARDS_COMPLIANCE)); }
	 * 
	 * public void testGVQ_Aggregated_All() throws MalformedURLException,
	 * IOException, XpathException, SAXException { // XXX User Feedback missing,
	 * correct behavior? testMetadataExample("GVQ_Aggregated_All_Available.xml",
	 * EnumSet.complementOf(EnumSet.of(Facet.USER_FEEDBACK))); }
	 * 
	 * public void testGVQ_Feedback_All() throws MalformedURLException,
	 * IOException, XpathException, SAXException {
	 * testMetadataExample("GVQ_Feedback_All_Available.xml",
	 * EnumSet.of(Facet.USER_FEEDBACK, Facet.EXPERT_REVIEW,
	 * Facet.CITATIONS_INFORMATION)); }
	 * 
	 * public void testGVQ_Feedback_No_Expert() throws MalformedURLException,
	 * IOException, XpathException, SAXException {
	 * testMetadataExample("GVQ_Feedback_No_Expert_Review.xml",
	 * EnumSet.of(Facet.USER_FEEDBACK, Facet.CITATIONS_INFORMATION)); }
	 * 
	 * public void testGVQ_Producer_All() throws MalformedURLException,
	 * IOException, XpathException, SAXException {
	 * testMetadataExample("GVQ_Producer_All_Available.xml",
	 * EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS,
	 * Facet.LINEAGE, Facet.STANDARDS_COMPLIANCE, Facet.QUALITY_INFORMATION,
	 * Facet.CITATIONS_INFORMATION)); }
	 * 
	 * public void testGVQ_Producer_No_ProducerProfile() throws
	 * MalformedURLException, IOException, XpathException, SAXException {
	 * testMetadataExample("GVQ_Producer_No_Producer.xml",
	 * EnumSet.of(Facet.PRODUCER_COMMENTS, Facet.LINEAGE,
	 * Facet.STANDARDS_COMPLIANCE, Facet.QUALITY_INFORMATION,
	 * Facet.CITATIONS_INFORMATION)); }
	 * 
	 * public void testGVQ_Producer_No_Standard() throws MalformedURLException,
	 * IOException, XpathException, SAXException {
	 * testMetadataExample("GVQ_Producer_No_Standard.xml",
	 * EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS,
	 * Facet.LINEAGE, Facet.QUALITY_INFORMATION, Facet.CITATIONS_INFORMATION));
	 * }
	 * 
	 * public void testIndia19139() throws MalformedURLException, IOException,
	 * XpathException, SAXException { testMetadataExample("india19139.xml",
	 * EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS,
	 * Facet.STANDARDS_COMPLIANCE)); }
	 * 
	 * public void testIndiaGVQ() throws MalformedURLException, IOException,
	 * XpathException, SAXException { testMetadataExample("indiaGVQ.xml",
	 * EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS,
	 * Facet.STANDARDS_COMPLIANCE)); }
	 */
	protected void testMetadataExample(String exampleFile, EnumSet<Facet> availableFacets) throws IOException,
			XpathException, SAXException {
		GeoLabelRequestBuilder labelRequest = GeoLabelClientV1.createGeoLabelRequest(getTestServiceUrl() + "svg/");

		labelRequest.setMetadataDocument(getClass().getClassLoader().getResourceAsStream(
				"testfiles/metadata/" + exampleFile));
		labelRequest.setDesiredSize(100);
		labelRequest.setForceDownload(true);

		GeoLabelRequestBuilder labelRequestControl = GeoLabelClientV1.createGeoLabelRequest();

		labelRequestControl.setMetadataDocument(getClass().getClassLoader().getResourceAsStream(
				"testfiles/metadata/" + exampleFile));
		labelRequestControl.setDesiredSize(100);
		labelRequestControl.setForceDownload(true);

		String testSVG = IOUtils.toString(labelRequest.getSVG());
		String controlSVG = IOUtils.toString(labelRequestControl.getSVG());

		performFacetComparison(testSVG, controlSVG);
	}

}
