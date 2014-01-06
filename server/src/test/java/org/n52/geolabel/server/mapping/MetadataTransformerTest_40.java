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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.commons.test.Facet;
import org.n52.geolabel.server.config.GeoLabelObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;

public class MetadataTransformerTest_40 {

    public static MetadataTransformer newMetadataTransformer() {
        TransformationDescriptionResources res = new TransformationDescriptionResources();
        return new MetadataTransformer(new TransformationDescriptionLoader(res, new GeoLabelObjectMapper(res), false));
    }

    private class LabelControlHolder {

        protected EnumSet<Facet> availableFacets;

        protected String[] standards;
        protected String[] scopeLevels;
        protected String[] organizationsNames;
        protected String producerCommentStart;

        protected Integer processStepCount;

        protected Integer citationCount;

        protected Integer expertReviewCount;
        protected Double expertRating;
        protected Integer expertRatingCount;

        protected Integer userReviewCount;
        protected Double userRating;
        protected Integer userRatingCount;

        public LabelControlHolder() {
            //
        }
    }

    @Test
    public void testMtri2an1ib() throws IOException {
        testMetadataExample("DigitalClimaticAtlas_mt_an_GEOlabel.xml", new LabelControlHolder() {
            {
                this.availableFacets = EnumSet.complementOf(EnumSet.of(Facet.USER_FEEDBACK, Facet.EXPERT_REVIEW));
                this.organizationsNames = new String[] {"JRC"};
                this.producerCommentStart = "Some methodological";
                this.processStepCount = Integer.valueOf(15);
                // this.standards = new String[] {"ISO 19115:2003/19139, 1.0"};
                this.scopeLevels = new String[] {"dataset level"};
                this.citationCount = Integer.valueOf(0);
            }
        });
    }

    private void testMetadataExample(String exampleFile, LabelControlHolder control) throws IOException {
        MetadataTransformer metadataTransformer = newMetadataTransformer();
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("4.0/" + exampleFile);
        Label l = new Label();
        l.setMetadataUrl(new URL("http://not.available.net"));
        l.setFeedbackUrl(new URL("http://not.available.net"));
        Label label = metadataTransformer.updateGeoLabel(metadataStream, l);

        // Check facet availability
        if (control.availableFacets != null)
            for (Facet facet : EnumSet.allOf(Facet.class)) {
                boolean contained = control.availableFacets.contains(facet);
                Availability expected = contained ? Availability.AVAILABLE : Availability.NOT_AVAILABLE;
                Availability actual = facet.getFacet(label).getAvailability();
                assertEquals("Facet " + facet.name() + " availability", expected, actual);
            }
        if (control.organizationsNames != null)
            label.getProducerProfileFacet().getOrganizationNames().containsAll(Arrays.asList(control.organizationsNames));

        if (control.producerCommentStart != null)
            assertTrue("Producer comment",
                       label.getProducerCommentsFacet().getKnownProblems().startsWith(control.producerCommentStart));

        if (control.processStepCount != null)
            assertEquals("Lineage processing steps",
                         control.processStepCount.intValue(),
                         label.getLineageFacet().getTotalProcessSteps());

        if (control.standards != null)
            assertTrue("Standards",
                       label.getStandardsComplianceFacet().getStandards().containsAll(Arrays.asList(control.standards)));

        if (control.scopeLevels != null)
            assertTrue("Scope Levels",
                       label.getQualityInformationFacet().getScopeLevels().containsAll(Arrays.asList(control.scopeLevels)));

        if (control.expertRating != null)
            assertEquals("Expert rating",
                         control.expertRating.doubleValue(),
                         label.getExpertFeedbackFacet().getAverageRating(),
                         0.05);
        if (control.expertRatingCount != null)
            assertEquals("Expert rating count",
                         control.expertRatingCount.intValue(),
                         label.getExpertFeedbackFacet().getRatingCount());
        if (control.expertReviewCount != null)
            assertEquals("Expert review count",
                         control.expertReviewCount.intValue(),
                         label.getExpertFeedbackFacet().getTotalItems());

        if (control.userRating != null)
            assertEquals("User rating",
                         control.userRating.doubleValue(),
                         label.getUserFeedbackFacet().getAverageRating(),
                         0.05);
        if (control.userRatingCount != null)
            assertEquals("User rating count",
                         control.userRatingCount.intValue(),
                         label.getUserFeedbackFacet().getRatingCount());
        if (control.userReviewCount != null)
            assertEquals("User review count",
                         control.userReviewCount.intValue(),
                         label.getUserFeedbackFacet().getTotalItems());

        if (control.citationCount != null)
            assertEquals("citations", control.citationCount.intValue(), label.getCitationsFacet().getTotalCitations());

        // check drilldown urls
        checkDrilldownUrl(label.getCitationsFacet(), false);
        checkDrilldownUrl(label.getExpertFeedbackFacet(), false);
        checkDrilldownUrl(label.getLineageFacet(), false);
        checkDrilldownUrl(label.getProducerCommentsFacet(), false);
        checkDrilldownUrl(label.getProducerProfileFacet(), false);
        checkDrilldownUrl(label.getQualityInformationFacet(), false);
        checkDrilldownUrl(label.getStandardsComplianceFacet(), false);
        checkDrilldownUrl(label.getUserFeedbackFacet(), false);
    }

    private void checkDrilldownUrl(LabelFacet facet, boolean asReference) {
        assertThat("drilldown does not contain string placeholders in " + facet.getClass().getSimpleName(),
                   facet.getHref(),
                   not(containsString("%s")));
        if (asReference)
            // sending documents directly, cannot check for geolabel.net
            assertThat("drilldown URL is not set in " + facet.getClass().getSimpleName(),
                       facet.getHref(),
                       is(nullValue()));
        else
            assertThat("drilldown contains URLs to geolabel.net in " + facet.getClass().getSimpleName(),
                       facet.getHref(),
                       containsString("http://not.available.net"));
    }

    @SuppressWarnings("unused")
    @Test
    public void testLabelUrlKey() throws IOException {
        TransformationDescriptionResources res = new TransformationDescriptionResources();
        new MetadataTransformer(new TransformationDescriptionLoader(res, new GeoLabelObjectMapper(res), false)) {
            {
                URL testURL1 = new URL("http://test1.resource1");
                URL testURL2 = new URL("http://test2.resource2");

                assertEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(testURL1, testURL2));
                assertEquals(new LabelUrlKey(testURL2, testURL1), new LabelUrlKey(testURL1, testURL2));

                assertEquals(new LabelUrlKey(null, testURL1), new LabelUrlKey(testURL1, null));
                assertEquals(new LabelUrlKey(testURL1, null), new LabelUrlKey(null, testURL1));

                assertNotEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(testURL1, null));
                assertNotEquals(new LabelUrlKey(testURL1, testURL2), new LabelUrlKey(null, testURL2));

                assertTrue(new LabelUrlKey(testURL1, testURL2).hashCode() == new LabelUrlKey(testURL2, testURL1).hashCode());
            }
        };
    }

    // TODO make this an integration test:
    // @Test
    // public void testResourceNotFoundErrorMessage() throws IOException {
    // MetadataTransformer metadataTransformer = newMetadataTransformer();
    // Label label = metadataTransformer.createGeoLabel(new URL("http://does.not/exist.xml"));
    //
    // ErrorFacet ef = label.getErrorFacet();
    // assertEquals("error facet availability", Availability.AVAILABLE, ef.getAvailability());
    // assertNotNull("error message null", ef.getErrorMessage());
    //
    // StringWriter sw = new StringWriter();
    // label.toSVG(sw, "testid", 100);
    // String svgString = sw.toString();
    //
    // assertTrue("error string is given", svgString.contains("does.not"));
    // assertTrue("error string is given", svgString.contains("Error:"));
    // assertTrue("error string is given", svgString.contains("Message:"));
    // }

}
