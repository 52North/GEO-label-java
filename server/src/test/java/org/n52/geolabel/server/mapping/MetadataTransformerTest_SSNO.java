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

public class MetadataTransformerTest_SSNO {

    private MetadataTransformer transformer;

    @Before
    public void newMetadataTransformer() {
        TransformationDescriptionResources res = new TransformationDescriptionResources("http://geoviqua.github.io/geolabel/mappings/transformerSSNO.json=/transformations/transformerSSNO.json");
        this.transformer = new MetadataTransformer(new TransformationDescriptionLoader(res,
                                                                                       new GeoLabelObjectMapper(res),
                                                                                       true));
    }

    private void testLabel(Label label) {

        assertThat("producer profile is found",
                   label.getProducerProfileFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        
    }

    @Test
    public void testIce_Core_Producer_Profile() throws IOException {
        Label label = testSSNODocument("ssno/Ice_Core_Producer_Profile.rdf");

        testLabel(label);

    }

    private Label testSSNODocument(String input) throws MalformedURLException, IOException {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream(input);
        Label l = new Label();
        l.setMetadataUrl(new URL("http://localhost:8080/glbservice"));
        Label label = this.transformer.updateGeoLabel(metadataStream, l);
        return label;
    }


}

