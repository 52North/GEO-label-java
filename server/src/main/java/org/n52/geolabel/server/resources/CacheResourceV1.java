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

package org.n52.geolabel.server.resources;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.n52.geolabel.server.mapping.MetadataTransformer.LabelUrlKey;

import com.google.inject.name.Named;
import com.sun.jersey.spi.resource.Singleton;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/v1/cache")
@Api(value = "/v1/cache", description = "Operations to explore the service cache")
@Singleton
public class CacheResourceV1 {

    private Provider<MetadataTransformer> transformer;

    protected long cacheMaxHours;

    protected long cacheMaxLabels;

    @XmlRootElement(name = "CacheMappings")
    private static class CacheMappingsHolder {

        @XmlElementRef
        private Set<LabelUrlKey> cacheMappings;

        @XmlAttribute(name = "maxCacheHours")
        private long maxHours = 1;

        @XmlAttribute(name = "maxCacheLabels")
        private long maxLabels;

        @SuppressWarnings("unused")
        CacheMappingsHolder() {
            //
        }

        public CacheMappingsHolder(Set<LabelUrlKey> cacheMappings, long cacheMaxHours, long cacheMaxLabels) {
            this.cacheMappings = cacheMappings;
            this.maxHours = cacheMaxHours;
            this.maxLabels = cacheMaxLabels;
        }
    }

    @Inject
    public CacheResourceV1(Provider<MetadataTransformer> transformer, @Named(GeoLabelConfig.CACHE_MAX_LABELS)
    long cacheMaxLabels, @Named(GeoLabelConfig.CACHE_MAX_HOURS)
    long cacheMaxHours) {
        this.cacheMaxHours = cacheMaxHours;
        this.cacheMaxLabels = cacheMaxLabels;
        this.transformer = transformer;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Returns a list of currently cached metadata/feedback url mappings")
    public CacheMappingsHolder getCacheInfo() {
        return new CacheMappingsHolder(this.transformer.get().getCacheContent(),
                                       this.cacheMaxHours,
                                       this.cacheMaxLabels);
    }
}
