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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.n52.geolabel.server.config.GeoLabelObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.mapping.description.CitationsFacetDescription;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription;
import org.n52.geolabel.server.mapping.description.TransformationDescription;
import org.n52.geolabel.server.mapping.description.TransformationDescription.NamespaceMapping;

/**
 * transformer file structure: <code>
 {
     "transformationDescription":{
     "name":"transformer",
     "namespaceMappings":[],
     "facetDescriptions":
        [
             {
             "producerProfile":{}
             },
             {
             "producerComments":{}
             },
             {
             "lineage":{}
             },
             {
             "standardsCompliance":{}
             },
             {
             "qualityInformation":{}
             },
             {
             "userFeedback":{}
             },
             {
             "expertReview":{}
             },
             {
             "citations":{}
             }
         ]
     }
 }
 </code>
 */
public class TransformationDescriptionTest {

    private TransformationDescriptionLoader loader;

    @Before
    public void instantiateLoader() {
        TransformationDescriptionResources res = new TransformationDescriptionResources();
        this.loader = new TransformationDescriptionLoader(res, new GeoLabelObjectMapper(res));
    }

    @SuppressWarnings("boxing")
    @Test
    public void loadGvqJsonTransformationDescription() throws MalformedURLException {

        Map<URL, String> resources = new HashMap<>();
        resources.put(new URL("http://do.not.even.look/for/it"), "transformations/transformerGVQ.json");


        Set<TransformationDescription> tds = this.loader.load();
        TransformationDescription description = tds.iterator().next();

        assertThat("mappings read", description.namespaceMappings.length, is(equalTo(3)));
        assertTrue(description.namespaceMappings[0].prefix.equals("gmd"));
        assertTrue(description.namespaceMappings[0].namespace.equals("http://www.isotc211.org/2005/gmd"));

        assertTrue(description.facetDescriptions.length == 8);

        // assertThat("class is correct umarshalled",
        // description.facetDescriptions[0].getClass().getName(),
        // is(equalTo(ProducerCommentsFacetDescription.class.getName())));
        // assertThat("availability path is correct",
        // description.facetDescriptions[0].getAvailabilityPath(),
        // is(equalTo("boolean(normalize-space(string(//*[local-name()='dataQualityInfo']//*[local-name()='GVQ_DiscoveredIssue']/*[local-name()='knownProblem'])))")));
        // assertThat("drilldown is correct",
        // description.facetDescriptions[0].getDrilldown().url,
        // is(equalTo("http://www.geolabel.net/api/v1/drilldown?metadata=%s&facet=producer_comments")));
    }

    @SuppressWarnings("boxing")
    @Test
    public void loadJsonTransformationDescription() throws MalformedURLException {

        Map<URL, String> resources = new HashMap<>();
        resources.put(new URL("http://do.not.even.look/for/it"), "/transformations/transformer.json");

        Set<TransformationDescription> tds = this.loader.load();
        TransformationDescription description = tds.iterator().next();

        assertThat("name", description.name, is(equalTo("transformer")));
        assertThat("mappings read", description.namespaceMappings.length, is(equalTo(3)));
        assertTrue(description.namespaceMappings[0].prefix.equals("gmd"));
        assertTrue(description.namespaceMappings[0].namespace.equals("http://www.isotc211.org/2005/gmd"));

        assertThat("all facets read", description.facetDescriptions.length, is(equalTo(8)));

        // assertThat("class is correct umarshalled",
        // description.facetDescriptions[1].getClass().getName(),
        // is(equalTo(ProducerCommentsFacetDescription.class.getName())));
        // assertThat("availability path is correct",
        // description.facetDescriptions[1].getAvailabilityPath(),
        // containsString("knownProblem"));
        // assertThat("availability path is correct",
        // description.facetDescriptions[1].getAvailabilityPath(),
        // containsString("GVQ_DiscoveredIssue"));
        // assertThat("drilldown is correct",
        // description.facetDescriptions[1].getDrilldown().url,
        // is(equalTo("%s?metadata=%s&facet=producer_comments")));
    }

    public void marshalDescription() throws JsonGenerationException, JsonMappingException, IOException {
        TransformationDescription d = new TransformationDescription();
        d.name = "testname";

        d.facetDescriptions = new FacetTransformationDescription< ? >[2];
        CitationsFacetDescription cfd = new CitationsFacetDescription(new TransformationDescriptionResources());
        cfd.setCitationsCountPath("/test/path");
        d.facetDescriptions[0] = cfd;

        d.namespaceMappings = new NamespaceMapping[2];
        d.namespaceMappings[0] = new NamespaceMapping("lala", "http://la.la");
        d.namespaceMappings[1] = new NamespaceMapping("fb", "http://foo.bar");

        // JSONJAXBContext context = new JSONJAXBContext(JSONConfiguration.mappedJettison().build(),
        // TransformationDescription.class);
        // // JSONConfiguration jsonConfiguration = context.getJSONConfiguration();
        // JSONMarshaller marshaller = context.createJSONMarshaller();
        // StringWriter writer = new StringWriter();
        // marshaller.marshallToJSON(d, writer);

        ObjectMapper m = new ObjectMapper();
        // JsonNode rootNode = m.readTree(input);
        // System.out.println(rootNode);

        // Map<String, Object> map = m.readValue(input, Map.class);
        // System.out.println(map);

        String s = m.writeValueAsString(d);
        System.out.println(s);
    }
}
