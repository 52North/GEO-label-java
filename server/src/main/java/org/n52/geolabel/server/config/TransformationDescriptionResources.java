package org.n52.geolabel.server.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformationDescriptionResources {

    protected static final Logger log = LoggerFactory.getLogger(TransformationDescriptionResources.class);

    private static final String TRANSFORMATIONS_RESOURCE = "transformations";

    /**
     * map between normative URL and fallback
     */
    private Map<URL, String> transformationDescriptionResources = new HashMap<>();

    public TransformationDescriptionResources() {
        try {
            this.transformationDescriptionResources.put(new URL("http://geoviqua.github.io/geolabel/mappings/transformerGVQ.json"),
                                                        "/" + TRANSFORMATIONS_RESOURCE + "/transformer.json");
        }
        catch (MalformedURLException e) {
            log.error("Could not create transformation description resources.", e);
        }
    }

    public TransformationDescriptionResources(Map<URL, String> resources) {
        this();
        this.transformationDescriptionResources.putAll(resources);
    }

    public Map<URL, String> getTransformationDescriptionResources() {
        return this.transformationDescriptionResources;
    }

    public void setTransformationDescriptionResources(Map<URL, String> transformationDescriptionResources) {
        this.transformationDescriptionResources = transformationDescriptionResources;
    }

}
