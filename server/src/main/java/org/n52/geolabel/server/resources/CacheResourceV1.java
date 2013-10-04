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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.n52.geolabel.server.mapping.MetadataTransformer.LabelUrlKey;

@Path("/api/v1/cache")
public class CacheResourceV1 {
	private Provider<MetadataTransformer> transformer;

	@XmlRootElement(name = "CacheMappings")
	private static class CacheMappingsHolder {

		@SuppressWarnings("unused")
		CacheMappingsHolder() {

		}

		public CacheMappingsHolder(Set<LabelUrlKey> cacheMappings) {
			this.cacheMappings = cacheMappings;
		}

		@XmlElementRef
		private Set<LabelUrlKey> cacheMappings;
	}

	@Inject
	public CacheResourceV1(Provider<MetadataTransformer> transformer) {
		this.transformer = transformer;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public CacheMappingsHolder getCacheInfo() {
		return new CacheMappingsHolder(transformer.get().getCacheContent());
	}
}
