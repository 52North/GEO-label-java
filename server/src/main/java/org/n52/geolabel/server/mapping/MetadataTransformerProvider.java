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

import javax.inject.Inject;

import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import com.google.inject.name.Named;

public class MetadataTransformerProvider implements Provider<MetadataTransformer> {

    private static Logger log = LoggerFactory.getLogger(MetadataTransformerProvider.class);

    private TransformationDescriptionLoader resources;

    private long cacheMaxLabels;

    private long cacheMaxHours;

    @Inject
    public MetadataTransformerProvider(TransformationDescriptionLoader resources,
                                       @Named(GeoLabelConfig.CACHE_MAX_LABELS)
                                       long cacheMaxLabels,
                                       @Named(GeoLabelConfig.CACHE_MAX_HOURS)
                                       long cacheMaxHours) {
        this.resources = resources;
        this.cacheMaxLabels = cacheMaxLabels;
        this.cacheMaxHours = cacheMaxHours;

        log.info("NEW {}", this);
    }

    @Override
	public MetadataTransformer get() {
        return new MetadataTransformer(this.resources, this.cacheMaxLabels, this.cacheMaxHours);
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MetadataTransformerProvider [");
        if (this.resources != null) {
            builder.append("resources=");
            builder.append(this.resources);
            builder.append(", ");
        }
        builder.append("cacheMaxLabels=");
        builder.append(this.cacheMaxLabels);
        builder.append(", cacheMaxHours=");
        builder.append(this.cacheMaxHours);
        builder.append("]");
        return builder.toString();
    }

}
