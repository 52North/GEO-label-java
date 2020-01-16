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

    @Test //tests if the given SSNODocuments contains information about the producer
    public void producerProfile() throws IOException {
        //EasyRDFConverter RDF/XML encoding, prov:wasAssociatedWith, prov:Person
        Label label = testSSNODocument("ssno/ERC_producer_profile_prov_foaf_Person_icecore.rdf");

        assertThat("producer profile is found",
                   label.getProducerProfileFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        
        assertThat("standards compliance is found",
                   label.getStandardsComplianceFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        
        assertThat("Hover-Over is not supported",
                   label.getProducerProfileFacet().getTitle(),
                   containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
        
        assertThat("Hover-Over is not supported",
                   label.getStandardsComplianceFacet().getTitle(),
                   containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
        
        //MyBluemixConverter RDF/XML encoding, prov:wasAttributedTo, prov:Organization
        label = testSSNODocument("ssno/MBC_producer_profile_prov_foaf_Organization_iphonebarometer.rdf");

        assertThat("producer profile is found",
                    label.getProducerProfileFacet().getAvailability(),
                    equalTo(Availability.AVAILABLE));
                   
        assertThat("standards compliance is found",
                    label.getStandardsComplianceFacet().getAvailability(),
                    equalTo(Availability.AVAILABLE));

        assertThat("Hover-Over is not supported",
                    label.getProducerProfileFacet().getTitle(),
                    containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
        
        assertThat("Hover-Over is not supported",
                    label.getStandardsComplianceFacet().getTitle(),
                    containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
    }

    @Test //tests if the given SSNODocuments contains comments of the producer
    public void producerComments() throws IOException {
        //EasyRDFConverter RDF/XML encoding
        Label label = testSSNODocument("ssno/ERC_producer_comments_rdf_coiloilplant.rdf");

        assertThat("producer comment is found",
                   label.getProducerCommentsFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));
        
        assertThat("Hover-Over is not supported",
                   label.getProducerCommentsFacet().getTitle(),
                   containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
        
        //MyBluemixConverter RDF/XML encoding
        label = testSSNODocument("ssno/MBC_producer_comments_rdf_coiloilplant.rdf");

        assertThat("producer comment is found",
                   label.getProducerCommentsFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));

        assertThat("Hover-Over is not supported",
                   label.getProducerCommentsFacet().getTitle(),
                   containsString("Hoverover and drilldown for RDF / XML are not supported yet."));
                
    }

    @Test //tests if the given SSNODocuments contains only information about standard compliance
    public void onlyStandardCompliance() throws IOException {
        //EasyRDFConverter RDF/XML encoding
        Label label = testSSNODocument("ssno/ERC_compliance_with_standards_only_spinningcups.rdf");

        assertThat("standards compliance is found",
                    label.getStandardsComplianceFacet().getAvailability(),
                    equalTo(Availability.AVAILABLE));

        assertThat("producer profile is not found",
                   label.getProducerProfileFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("producer comment is not found",
                   label.getProducerCommentsFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Quality information is not found",
                   label.getQualityInformationFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("User feedback is not found",
                   label.getUserFeedbackFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Lineage information is not found",
                   label.getLineageFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));

        assertThat("Expert feedback is not found",
                   label.getExpertFeedbackFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Citations information is not found",
                   label.getCitationsFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));

        //MyBluemixConverter RDF/XML encoding
        label = testSSNODocument("ssno/MBC_compliance_with_standards_only_spinningcups.rdf");

        assertThat("producer comment is not found",
                   label.getProducerCommentsFacet().getAvailability(),
                   equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("standards compliance is found",
                   label.getStandardsComplianceFacet().getAvailability(),
                   equalTo(Availability.AVAILABLE));

        assertThat("producer profile is not found",
                    label.getProducerProfileFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));
       
        assertThat("producer comment is not found",
                    label.getProducerCommentsFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Quality information is not found",
                    label.getQualityInformationFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("User feedback is not found",
                    label.getUserFeedbackFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Lineage information is not found",
                    label.getLineageFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));

        assertThat("Expert feedback is not found",
                    label.getExpertFeedbackFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));
        
        assertThat("Citations information is not found",
                    label.getCitationsFacet().getAvailability(),
                    equalTo(Availability.NOT_AVAILABLE));

    }


    private Label testSSNODocument(String input) throws MalformedURLException, IOException {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream(input);
        Label l = new Label();
        l.setMetadataUrl(new URL("http://localhost:8080/glbservice"));
        Label label = this.transformer.updateGeoLabel(metadataStream, l);
        return label;
    }


}

