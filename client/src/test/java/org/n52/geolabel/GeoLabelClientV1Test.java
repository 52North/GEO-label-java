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
package org.n52.geolabel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.n52.geolabel.client.GeoLabelCache;
import org.n52.geolabel.client.GeoLabelClientV1;

public class GeoLabelClientV1Test {

	@Test
	public void testCreateGeoLabelMetadataStream() throws IOException {
		InputStream metadataStream = getClass().getResourceAsStream(
				"FAO_GEO_Network_iso19139.xml");
		InputStream svg = GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataStream).getSVG();
		String svgString = IOUtils.toString(svg);

		assertTrue(svgString
				.contains("<title>Standards Compliance. Standard name: ISO 19115:2003/19139, version 1.0.</title>"));
		assertTrue("ensure has no feedback info",
				svgString.contains("<title>User Feedback</title>"));
	}

	@Test
	public void testCreateGeoLabelMetadataAndFeedbackStream()
			throws IOException {
		InputStream metadataStream = getClass().getResourceAsStream(
				"FAO_GEO_Network_iso19139.xml");
		InputStream feedbackStream = getClass().getResourceAsStream(
				"GVQ_Feedback_All_Available.xml");

		InputStream svg = GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataStream)
				.setFeedbackDocument(feedbackStream).getSVG();
		String svgString = IOUtils.toString(svg);

		assertTrue(svgString
				.contains("<title>User Feedback. Number of feedbacks 3. Average rating: 3 (2 ratings).</title>"));
	}

	@Test
	public void testCreateGeoLabelMetadataUrl() throws IOException {
		String metadataUrl = "http://twiki.geoviqua.org/twiki/pub/GeoViQuaIntranet/QualityModelGCIExamples/483980.xml";

		InputStream svg = GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataUrl).getSVG();
		String svgString = IOUtils.toString(svg);

		assertTrue(svgString
				.contains("<title>Standards Compliance. Standard name: ISO 19115, version 1.0.</title>"));
		assertTrue("ensure has no feedback info",
				svgString.contains("<title>User Feedback</title>"));
	}

	@Test
	public void testCreateGeoLabelMetadataUrlAndFeedbackStream()
			throws IOException {
		String metadataUrl = "http://twiki.geoviqua.org/twiki/pub/GeoViQuaIntranet/QualityModelGCIExamples/483980.xml";
		InputStream feedbackStream = getClass().getResourceAsStream(
				"GVQ_Feedback_All_Available.xml");

		InputStream svg = GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataUrl)
				.setFeedbackDocument(feedbackStream).getSVG();
		String svgString = IOUtils.toString(svg);

		assertTrue(svgString
				.contains("<title>User Feedback. Number of feedbacks 3. Average rating: 3 (2 ratings).</title>"));
	}

	@Test
	public void testCreateGeoLabelMetadataUrlCache() throws IOException {
		String metadataUrl = "http://twiki.geoviqua.org/twiki/pub/GeoViQuaIntranet/QualityModelGCIExamples/483980.xml";

		GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataUrl).setUseCache(true).getSVG();
		
		assertTrue(GeoLabelCache.hasSVG(metadataUrl));
		
		InputStream svg = GeoLabelClientV1.createGeoLabelRequest()
				.setMetadataDocument(metadataUrl).setUseCache(true).getSVG();
		String svgString = IOUtils.toString(svg);

		assertTrue(svgString
				.contains("<title>Standards Compliance. Standard name: ISO 19115, version 1.0.</title>"));
		assertTrue("ensure has no feedback info",
				svgString.contains("<title>User Feedback</title>"));
	}

	@Test
	public void testCreateGeoLabelNoContent() throws IOException {
		try {
			GeoLabelClientV1.createGeoLabelRequest().getSVG();
			fail("Server should send error code");
		} catch (IOException e) {
			assertTrue(e.getMessage().contains("Bad Request"));
		}

	}
}
