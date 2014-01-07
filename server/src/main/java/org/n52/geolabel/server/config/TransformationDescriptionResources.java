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

package org.n52.geolabel.server.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Singleton
public class TransformationDescriptionResources {

    protected static final Logger log = LoggerFactory.getLogger(TransformationDescriptionResources.class);

    public static enum Source {
        URL, FALLBACK, NA;
    }

    private String drilldownEndpoint;

    /**
     * map between normative URL and fallback
     */
    private Map<URL, String> resources = new HashMap<>();

    @Inject
    public TransformationDescriptionResources(@Named("transformer.resources")
    String resourcesString, @Named(GeoLabelConfig.DRILLDOWN_EXTERNAL_ENDPOINT)
    String drilldownEndpoint) {
        this.drilldownEndpoint = drilldownEndpoint;

        Map<String, String> splitted = Splitter.on(",").withKeyValueSeparator("=").split(resourcesString);

        for (Entry<String, String> entry : splitted.entrySet())
            try {
                this.resources.put(new URL(entry.getKey()), entry.getValue());
                log.debug("Added transformation resource {} with fallback {}", entry.getKey(), entry.getValue());
            }
            catch (MalformedURLException e) {
                log.error("Could not create transformation description resources.", e);
            }
    }

    public TransformationDescriptionResources(Map<URL, String> resources) {
        this.resources.putAll(resources);
    }

    public TransformationDescriptionResources(String resourcesString) {
        this(resourcesString, null);
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

    public String getDrilldownEndpoint() {
        return this.drilldownEndpoint;
    }

}
