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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.n52.geolabel.commons.Label;
import org.n52.geolabel.server.config.GeoLabelObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.mapping.description.TransformationDescription;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TransformationSelection {

    @Test
    public void usabilityPathsAreUsed() throws ParserConfigurationException, SAXException, IOException {
        TransformationDescriptionResources bothRes = new TransformationDescriptionResources("http://geoviqua.github.io/geolabel/mappings/transformer.json=/transformations/transformer.json,http://geoviqua.github.io/geolabel/mappings/transformerSML10.json=/transformations/transformerSML10.json");
        TransformationDescriptionLoader transformationDescriptionLoader = new TransformationDescriptionLoader(bothRes,
                                                                                                              new GeoLabelObjectMapper(bothRes),
                                                                                                              true);

        Set<TransformationDescription> transformationDescriptions = transformationDescriptionLoader.load();

        String exampleString = "<sml:SensorML xmlns:sml='http://www.opengis.net/sensorML/1.0.1' version='1.0.1'><sml:member><sml:System><gml:description>testdoc</gml:description></sml:System></sml:member></sml:SensorML>";
        InputStream is = new ByteArrayInputStream(exampleString.getBytes());
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(is);

        assertThat("both tds are loaded",
                   Integer.valueOf(transformationDescriptions.size()),
                   is(equalTo(Integer.valueOf(2))));

        for (TransformationDescription td : transformationDescriptions) {
            boolean updatedLabel = td.updateGeoLabel(new Label(), doc);

            if (td.name.contains("SML"))
                assertThat("transformation was applied", Boolean.valueOf(updatedLabel), is(Boolean.TRUE));

            // the base transformer always applies
            if ( !td.name.contains("SML"))
                assertThat("transformation was applied", Boolean.valueOf(updatedLabel), is(Boolean.TRUE));
        }
    }

}
