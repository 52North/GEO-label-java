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

import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.n52.geolabel.server.mapping.MetadataTransformer.TransformationDescription;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/v1/mappings")
@Api(value = "/v1/mappings", description = "Access to the mapping files used to generate a label")
public class MappingsResourceV1 {

	private Provider<MetadataTransformer> transformer;

	@XmlRootElement(name = "Mappings")
	private static class MappingsHolder {

        @SuppressWarnings("unused")
		MappingsHolder() {
            //
		}

        public MappingsHolder(Set<TransformationDescription> descriptions) {
            this.descriptions = descriptions;
		}

		@XmlAttribute(name = "maxCacheHours")
		private int maxHours = MetadataTransformer.CACHE_MAX_HOURS;
        @XmlElementRef
        private Set<TransformationDescription> descriptions;
	}

	@Inject
	public MappingsResourceV1(Provider<MetadataTransformer> transformer) {
		this.transformer = transformer;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Returns a list of currently cached metadata/feedback url mappings")
	public MappingsHolder getCacheInfo() {
        return new MappingsHolder(this.transformer.get().getMappings());
	}
}
