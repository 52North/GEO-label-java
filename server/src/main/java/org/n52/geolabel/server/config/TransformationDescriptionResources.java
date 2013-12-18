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

    public static enum Source {
        ONLINE, FALLBACK, NA;
    }

    /**
     * map between normative URL and fallback
     */
    private Map<URL, String> resources = new HashMap<>();

    public TransformationDescriptionResources() {
        try {
            this.resources.put(new URL("http://geoviqua.github.io/geolabel/mappings/transformer.json"),
                                                        "/" + TRANSFORMATIONS_RESOURCE + "/transformer.json");
        }
        catch (MalformedURLException e) {
            log.error("Could not create transformation description resources.", e);
        }
    }

    public TransformationDescriptionResources(Map<URL, String> resources) {
        this.resources.putAll(resources);
    }

    public Map<URL, String> getResources() {
        return this.resources;
    }

    public void setResources(Map<URL, String> transformationDescriptionResources) {
        this.resources = transformationDescriptionResources;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TransformationDescriptionResources [");
        if (this.resources != null) {
            builder.append("resources=");
            builder.append(this.resources);
        }
        builder.append("]");
        return builder.toString();
    }

}
