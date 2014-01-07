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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet.Availability;
import org.n52.geolabel.server.config.GeoLabelObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;

public class MetadataTransformerTest_SML {

    private MetadataTransformer transformer;

    @Before
    public void newMetadataTransformer() {
        TransformationDescriptionResources res = new TransformationDescriptionResources("http://geoviqua.github.io/geolabel/mappings/transformer.json=/transformations/transformer.json,http://geoviqua.github.io/geolabel/mappings/transformerSML10.json=/transformations/transformerSML10.json");
        this.transformer = new MetadataTransformer(new TransformationDescriptionLoader(res,
                                                                                       new GeoLabelObjectMapper(res),
                                                                                       true));
    }

    private void testLabel(Label label) {
        assertThat("standards compliance is found",
                   label.getStandardsComplianceFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        assertThat("standards compliance title contains version",
                   label.getStandardsComplianceFacet().getTitle(),
                   containsString("1.0.1"));
        assertThat("standards compliance title contains standard name",
                   label.getStandardsComplianceFacet().getTitle(),
                   containsString("SensorML"));

        assertThat("producer profile is found",
                   label.getProducerProfileFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        assertThat("producer profile contains individual name",
                   label.getProducerProfileFacet().getTitle(),
                   containsString("Eike"));
        assertThat("producer profile contains org name",
                   label.getProducerProfileFacet().getTitle(),
                   containsString("52°North"));
        assertThat("producer profile contains all contacts name",
                   label.getProducerProfileFacet().getTitle(),
                   containsString("2 responsible parties"));
    }

    @Test
    public void testWS2500_withQuality() throws IOException {
        Label label = testSensorMLDocument("sml/ws2500_with-quality_with-doc.xml");

        testLabel(label);

        assertThat("two textual quality elements are found",
                   label.getQualityInformationFacet().getTitle(),
                   containsString("2 are textual"));
        assertThat("two three quantity elements are found",
                   label.getQualityInformationFacet().getTitle(),
                   containsString("3 are quantities"));
    }

    @Test
    public void testWS2500_withHistoryAndProcessChain() throws IOException {
        Label label = testSensorMLDocument("sml/ws2500-with-history-and-processchain.xml");

        testLabel(label);

        assertThat("two history entires found",
                   label.getLineageFacet().getTitle(),
                   containsString("history entries: 1."));
        assertThat("process chain steps found", label.getLineageFacet().getTitle(), containsString("process steps: 3."));
    }

    @Test
    public void testWS2500() throws IOException {
        Label label = testSensorMLDocument("sml/ws2500.xml");

        testLabel(label);

        File f = File.createTempFile("geolabel_", ".svg");
        FileWriter fw = new FileWriter(f);
        label.toSVG(fw, "test", 420);
        System.out.println("Wrote label to " + f.getAbsolutePath());
    }

    @Test
    public void testWS2500inDescribeSensorResponse() throws IOException {
        Label label = testSensorMLDocument("sml/ws2500_DescribeSensorResponse.xml");

        testLabel(label);
    }

    private Label testSensorMLDocument(String input) throws MalformedURLException, IOException {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream(input);
        Label l = new Label();
        l.setMetadataUrl(new URL("http://not.available.net"));
        l.setFeedbackUrl(new URL("http://not.available.net"));
        Label label = this.transformer.updateGeoLabel(metadataStream, l);
        return label;
    }

}
