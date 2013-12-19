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

package org.n52.geolabel.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class GeoLabelClientV1IT {

    private String gvqExample = "http://schemas.geoviqua.org/GVQ/4.0/example_documents/PQM_UQM_combined/DigitalClimaticAtlas_mt_an_v10.xml";

    private GeoLabelRequestBuilder requestBuilder;

    // private static String serviceUrl = "http://www.geolabel.net/api/v1/geolabel";

    private static String serviceUrl = "http://localhost:8080/glbservice/api/v1/svg";

    // private String serviceUrl = "http://www.geolabel.net/api/v1/geolabel";

    @Before
    public void createRequest() {
        this.requestBuilder = GeoLabelClientV1.createGeoLabelRequest(serviceUrl);
    }

    @Test
    public void testCreateGeoLabelMetadataStream() throws IOException {
        InputStream metadataStream = getClass().getResourceAsStream("/3.1/FAO_GEO_Network_iso19139.xml");
        InputStream svg = this.requestBuilder.setMetadataDocument(metadataStream).getSVG();
        String svgString = IOUtils.toString(svg);

        assertTrue(svgString.contains("<title>Standards Compliance"));
        assertTrue("ensure has no feedback info", svgString.contains("Number of feedbacks: 0."));
    }

    @Test
    public void testCreateGeoLabelMetadataAndFeedbackStream() throws IOException {
        InputStream metadataStream = getClass().getResourceAsStream("/3.1/FAO_GEO_Network_iso19139.xml");
        InputStream feedbackStream = getClass().getResourceAsStream("/3.1/GVQ_Feedback_All_Available.xml");

        InputStream svg = this.requestBuilder.setMetadataDocument(metadataStream).setFeedbackDocument(feedbackStream).getSVG();
        String svgString = IOUtils.toString(svg);

        // using 3.1 model, nothing there anymore
        assertTrue(svgString.contains("Number of feedbacks: 0. Average rating: 0 (0 ratings).</title>"));
    }

    @Test
    public void testCreateGeoLabelMetadataUrl() throws IOException {
        InputStream svg = this.requestBuilder.setMetadataDocument(this.gvqExample).getSVG();
        String svgString = IOUtils.toString(svg);

        assertTrue("organization", svgString.contains("Animal and Plant Biology and Ecology Department"));
        assertTrue(svgString.contains("<title>Standards Compliance"));
        assertTrue("ensure has no feedback info", svgString.contains("Number of feedbacks: 0."));
    }

    @Test
    public void testCreateGeoLabelMetadataUrlAndFeedbackStream() throws IOException {
        InputStream feedbackStream = getClass().getResourceAsStream("/3.1/GVQ_Feedback_All_Available.xml");

        InputStream svg = this.requestBuilder.setMetadataDocument(this.gvqExample).setFeedbackDocument(feedbackStream).getSVG();
        String svgString = IOUtils.toString(svg);

        // using 3.1 model, nothing there anymore
        assertTrue(svgString.contains("<title>User Feedback"));
        assertTrue(svgString.contains("Number of feedbacks: 0. Average rating: 0 (0 ratings)."));
    }

    @Test
    public void testCreateGeoLabelMetadataUrlCache() throws IOException {
        this.requestBuilder.setMetadataDocument(this.gvqExample).setUseCache(true).getSVG();

        assertTrue(GeoLabelCache.hasSVG(this.gvqExample));

        InputStream svg = GeoLabelClientV1.createGeoLabelRequest(serviceUrl).setMetadataDocument(this.gvqExample).setUseCache(true).getSVG();
        String svgString = IOUtils.toString(svg);

        assertTrue("organization", svgString.contains("Animal and Plant Biology and Ecology Department"));
        assertTrue(svgString.contains("<title>Standards Compliance"));
        // 52N server only:
        // assertTrue("no error", !svgString.contains("id=\"error_group\" "));
        assertTrue("ensure has no feedback info", svgString.contains("Number of feedbacks: 0."));
    }

    @Test
    public void testCreateGeoLabelNoContent() {
        try {
            this.requestBuilder.getSVG();
            fail("Server should send error code");
        }
        catch (IOException e) {
            assertTrue(e.getMessage().contains("Bad Request"));
        }

    }
}
