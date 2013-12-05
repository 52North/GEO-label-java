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
package org.n52.geolabel.server.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.commons.test.Facet;
import org.xml.sax.SAXException;

public class MetadataTransformerTest {

	public static MetadataTransformer newMetadataTransformer() {
		return new MetadataTransformer();
	}

	@Test
	public void testParseMetadata() throws IOException {

		MetadataTransformer metadataTransformer = newMetadataTransformer();
		metadataTransformer.readTransformationDescription(getClass().getResourceAsStream("transformer.xml"));

		InputStream metadataStream = getClass().getResourceAsStream("metadata.xml");

		Label geoLabel = metadataTransformer.createGeoLabel(metadataStream);

		assertTrue(geoLabel.getProducerProfileFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getProducerProfileFacet().getOrganizationNames().contains("JRC"));

		assertTrue(geoLabel.getLineageFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getLineageFacet().getProcessStepCount() == 3);

		assertTrue(geoLabel.getProducerCommentsFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getProducerCommentsFacet().getProducerComments().size() == 2);

		assertTrue(geoLabel.getStandardsComplianceFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getStandardsComplianceFacet().getStandards().size() == 1);

		assertTrue(geoLabel.getQualityInformationFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getQualityInformationFacet().getScopeLevels().size() == 1);

		assertTrue(geoLabel.getUserFeedbackFacet().getAvailability() == Availability.NOT_AVAILABLE);
		assertTrue(geoLabel.getUserFeedbackFacet().getAverageRating() == null);
		assertTrue(geoLabel.getUserFeedbackFacet().getTotalFeedbacks() == 0);
		assertTrue(geoLabel.getUserFeedbackFacet().getTotalRatings() == 0);

		assertTrue(geoLabel.getExpertFeedbackFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getExpertFeedbackFacet().getAverageRating().doubleValue() == 2.5);
		assertTrue(geoLabel.getExpertFeedbackFacet().getTotalFeedbacks() == 2);
		assertTrue(geoLabel.getExpertFeedbackFacet().getTotalRatings() == 2);

		assertTrue(geoLabel.getCitationsFacet().getAvailability() == Availability.AVAILABLE);
		assertTrue(geoLabel.getCitationsFacet().getTotalCitations() == 7);
	}

	private class LabelControlHolder {
		protected EnumSet<Facet> availableFacets;

		protected String[] standards;
		protected String[] scopeLevels;
		protected String[] organizationsNames;
		protected String[] producerCommentsStart;

		protected Integer processStepCount;

		protected Integer citationCount;

		protected Integer expertReviewCount;
		protected Double expertRating;
		protected Integer expertRatingCount;

		protected Integer userReviewCount;
		protected Double userRating;
		protected Integer userRatingCount;
	}

	@Test
	public void testFAO_GEONETWORK() throws IOException {
		testMetadataExample("FAO_GEO_Network_iso19139.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS, Facet.LINEAGE,
						Facet.QUALITY_INFORMATION, Facet.STANDARDS_COMPLIANCE);
				this.organizationsNames = new String[] { "FAO - NRL" };
				this.producerCommentsStart = new String[] { "The EPSMO Project was initiated" };
				this.processStepCount = 0;
				this.standards = new String[] { "ISO 19115:2003/19139, 1.0" };
				this.scopeLevels = new String[] { "dataset" };
			}
		});
	}

	@Test
	public void testFGDC_Producer() throws IOException {
		testMetadataExample("FGDC_Producer.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.PRODUCER_PROFILE, Facet.STANDARDS_COMPLIANCE);
				this.organizationsNames = new String[] { "Esri" };
				this.standards = new String[] { "FGDC-STD-001-1998" };
			}
		});
	}

	@Test
	public void testGVQ_Aggregated_All() throws IOException {
		testMetadataExample("GVQ_Aggregated_All_Available.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.complementOf(EnumSet.of(Facet.USER_FEEDBACK));
				this.organizationsNames = new String[] { "JRC" };
				this.producerCommentsStart = new String[] { "The GVM unit" };
				this.processStepCount = 3;
				this.standards = new String[] { "ISO 19115:2003/19139, 1.0" };
				this.scopeLevels = new String[] { "dataset" };
				this.citationCount = 7;
				this.expertReviewCount = 2;
				this.expertRating = 2.5;
				this.expertRatingCount = 2;
			}
		});
	}

	@Test
	public void testGVQ_Feedback_All() throws MalformedURLException, IOException, XpathException, SAXException {
		testMetadataExample("GVQ_Feedback_All_Available.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.CITATIONS_INFORMATION, Facet.EXPERT_REVIEW, Facet.USER_FEEDBACK);
				this.citationCount = 4;

				this.expertReviewCount = 1;
				this.expertRating = 3d;
				this.expertRatingCount = 1;

				this.userReviewCount = 3;
				this.userRating = 3d;
				this.userRatingCount = 2;
			}
		});
	}

	@Test
	public void testGVQ_Feedback_No_Expert() throws MalformedURLException, IOException, XpathException, SAXException {
		testMetadataExample("GVQ_Feedback_No_Expert_Review.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.CITATIONS_INFORMATION, Facet.USER_FEEDBACK);
				this.citationCount = 2;

				this.userReviewCount = 2;
				this.userRating = 3d;
				this.userRatingCount = 2;
			}
		});
	}

	@Test
	public void testGVQ_Producer_All() throws IOException {
		testMetadataExample("GVQ_Producer_All_Available.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.complementOf(EnumSet.of(Facet.USER_FEEDBACK, Facet.EXPERT_REVIEW));
				this.organizationsNames = new String[] { "JRC" };
				this.producerCommentsStart = new String[] { "The GVM unit" };
				this.processStepCount = 3;
				this.standards = new String[] { "ISO 19115:2003/19139, 1.0" };
				this.scopeLevels = new String[] { "dataset" };
				this.citationCount = 5;
			}
		});
	}

	@Test
	public void testIndia19139() throws IOException {
		testMetadataExample("india19139.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS, Facet.STANDARDS_COMPLIANCE);
				this.organizationsNames = new String[] { "FAO - UN AGL Documentation Center" };
				this.producerCommentsStart = new String[] { "ISIS Identifier" };
				this.standards = new String[] { "ISO 19115:2003/19139, 1.0" };
			}
		});
	}

	@Test
	public void testIndiaGVQ() throws IOException {
		testMetadataExample("indiaGVQ.xml", new LabelControlHolder() {
			{
				this.availableFacets = EnumSet.of(Facet.PRODUCER_PROFILE, Facet.PRODUCER_COMMENTS, Facet.STANDARDS_COMPLIANCE);
				this.organizationsNames = new String[] { "FAO - UN AGL Documentation Center" };
				this.producerCommentsStart = new String[] { "ISIS Identifier" };
				this.standards = new String[] { "ISO 19115:2003/19139, 1.0" };
			}
		});
	}

	private void testMetadataExample(String exampleFile, LabelControlHolder control) throws IOException {
		MetadataTransformer metadataTransformer = newMetadataTransformer();
		InputStream metadataStream = getClass().getClassLoader().getResourceAsStream(
				"testfiles/metadata/" + exampleFile);
		Label label = metadataTransformer.createGeoLabel(metadataStream);

		// Check facet availability
		if (control.availableFacets != null)
            for (Facet facet : EnumSet.allOf(Facet.class))
                assertEquals("Facet " + facet.name() + " availability",
						control.availableFacets.contains(facet) ? Availability.AVAILABLE : Availability.NOT_AVAILABLE,
						facet.getFacet(label).getAvailability());

		if (control.organizationsNames != null)
            label.getProducerProfileFacet().getOrganizationNames()
					.containsAll(Arrays.asList(control.organizationsNames));

		if (control.producerCommentsStart != null) {
			// Original service only includes first comment
			// assertEquals("Number comments",
			// control.producerCommentsStart.length,
			// label.getProducerCommentsFacet()
			// .getProducerComments().size());
			BitSet commentsToBeFound = new BitSet();
			commentsToBeFound.set(0, control.producerCommentsStart.length, true);
			String commentStartRef;
			for (int i = 0; i < control.producerCommentsStart.length; i++) {
				commentStartRef = control.producerCommentsStart[i];
				for (String commentTest : label.getProducerCommentsFacet().getProducerComments())
                    if (commentTest.startsWith(commentStartRef))
                        commentsToBeFound.set(i, false);
			}
			assertTrue("Comments", commentsToBeFound.isEmpty());
		}

		if (control.processStepCount != null)
            assertEquals("Lineage processing steps", control.processStepCount.intValue(), label.getLineageFacet()
					.getProcessStepCount());

		if (control.standards != null)
            assertTrue("Standards",
					label.getStandardsComplianceFacet().getStandards().containsAll(Arrays.asList(control.standards)));

		if (control.scopeLevels != null)
            assertTrue("Scope Levels",
					label.getQualityInformationFacet().getScopeLevels().containsAll(Arrays.asList(control.scopeLevels)));

		if (control.expertRating != null)
            assertEquals("Expert rating", control.expertRating, label.getExpertFeedbackFacet().getAverageRating(), 0.05);
		if (control.expertRatingCount != null)
            assertEquals("Expert rating count", control.expertRatingCount.intValue(), label.getExpertFeedbackFacet()
					.getTotalRatings());
		if (control.expertReviewCount != null)
            assertEquals("Expert review count", control.expertReviewCount.intValue(), label.getExpertFeedbackFacet()
					.getTotalFeedbacks());

		if (control.userRating != null)
            assertEquals("User rating", control.userRating, label.getUserFeedbackFacet().getAverageRating(), 0.05);
		if (control.userRatingCount != null)
            assertEquals("User rating count", control.userRatingCount.intValue(), label.getUserFeedbackFacet()
					.getTotalRatings());
		if (control.userReviewCount != null)
            assertEquals("User review count", control.userReviewCount.intValue(), label.getUserFeedbackFacet()
					.getTotalFeedbacks());

		if (control.citationCount != null)
            assertEquals("citations", control.citationCount.intValue(), label.getCitationsFacet().getTotalCitations());
	}

	@Test
	public void testLabelUrlKey() throws MalformedURLException {
		new MetadataTransformer() {
			{
				URL testURL1 = new URL("http://test1.resource1");
				URL testURL2 = new URL("http://test2.resource2");

				assertEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(testURL1, testURL2));
				assertEquals(new LabelUrlKey(testURL2, testURL1), new LabelUrlKey(testURL1, testURL2));

				assertEquals(new LabelUrlKey(null, testURL1), new LabelUrlKey(testURL1, null));
				assertEquals(new LabelUrlKey(testURL1, null), new LabelUrlKey(null, testURL1));

				assertNotEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(testURL1, null));
				assertNotEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(null, testURL2));

				assertTrue(new LabelUrlKey(testURL1, testURL2).hashCode() == new LabelUrlKey(testURL2, testURL1)
						.hashCode());
			}
		};

	}

}
