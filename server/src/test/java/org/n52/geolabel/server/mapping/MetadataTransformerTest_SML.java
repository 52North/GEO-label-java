package org.n52.geolabel.server.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.n52.geolabel.commons.Label;
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

    @Test
    public void testWS2500() throws IOException {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("sml/ws2500.xml");
        Label l = new Label();
        l.setMetadataUrl(new URL("http://not.available.net"));
        l.setFeedbackUrl(new URL("http://not.available.net"));
        Label label = this.transformer.updateGeoLabel(metadataStream, l);

        System.out.println(label);
    }

    @Test
    public void testWS2500inDescribeSensorResponse() throws IOException {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("sml/ws2500_DescribeSensorResponse.xml");
        Label l = new Label();
        l.setMetadataUrl(new URL("http://not.available.net"));
        l.setFeedbackUrl(new URL("http://not.available.net"));
        Label label = this.transformer.updateGeoLabel(metadataStream, l);

        System.out.println(label);
    }

}
