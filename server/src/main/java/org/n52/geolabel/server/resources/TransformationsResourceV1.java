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

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.config.TransformationDescriptionResources.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/v1/transformations")
@Api(value = "/v1/transformations", description = "Access to the transformation rules used to generate a label")
@Singleton
public class TransformationsResourceV1 {

    protected static final Logger log = LoggerFactory.getLogger(TransformationsResourceV1.class);

    @XmlRootElement(name = "transformation")
    public static class Transformation {

        public String url;

        public String fallback;

        public String used;

        Transformation() {
            //
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Transformation [");
            if (this.url != null) {
                builder.append("url=");
                builder.append(this.url);
                builder.append(", ");
            }
            if (this.fallback != null) {
                builder.append("fallback=");
                builder.append(this.fallback);
                builder.append(", ");
            }
            if (this.used != null) {
                builder.append("used=");
                builder.append(this.used);
            }
            builder.append("]");
            return builder.toString();
        }
    }

    @XmlRootElement(name = "transformations")
    public static class TransformationsHolder {

        @XmlElementRef
        private ArrayList<Transformation> transformations = new ArrayList<>();

        public TransformationsHolder() {
            //
        }

        public TransformationsHolder(Map<URL, String> transformationDescriptionResources, Map<URL, Source> usedSources) {
            for (Entry<URL, String> entry : transformationDescriptionResources.entrySet()) {
                Transformation t = new Transformation();
                t.url = entry.getKey().toString();
                t.fallback = entry.getValue();
                Source source = usedSources.get(entry.getKey());
                t.used = (source == null) ? Source.NA.toString() : source.toString();

                this.transformations.add(t);
                log.debug("Created transformation {}", t);
            }
        }

	}

    private TransformationDescriptionLoader loader;

    private TransformationDescriptionResources resources;

	@Inject
    public TransformationsResourceV1(TransformationDescriptionResources resources,
                                     TransformationDescriptionLoader loader) {
        this.resources = resources;
        this.loader = loader;
	}

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Returns a list of used transformations")
    public TransformationsHolder getTransformationsInfo() {
        TransformationsHolder holder = new TransformationsHolder(this.resources.getTransformationDescriptionResources(),
                                                                 this.loader.getUsedSources());

        return holder;
	}
}
