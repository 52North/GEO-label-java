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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.n52.geolabel.formats.PngEncoder;
import org.n52.geolabel.server.config.ExceptionMappers.ContainerExceptionMapper;
import org.n52.geolabel.server.config.ExceptionMappers.IOExceptionMapper;
import org.n52.geolabel.server.config.ExceptionMappers.ParamExceptionMapper;
import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.n52.geolabel.server.resources.ApiResource;
import org.n52.geolabel.server.resources.CacheResourceV1;
import org.n52.geolabel.server.resources.LMLResourceV1;
import org.n52.geolabel.server.resources.PNGResourceV1;
import org.n52.geolabel.server.resources.SVGResourceV1;
import org.n52.geolabel.server.resources.StaticLabelResourceV1;
import org.n52.geolabel.server.resources.TransformationsResourceV1;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class GeoLabelModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {
        bind(LMLResourceV1.class);
        bind(SVGResourceV1.class);
        bind(PNGResourceV1.class);
        bind(StaticLabelResourceV1.class);
        bind(TransformationsResourceV1.class);
        bind(CacheResourceV1.class);
        bind(ApiResource.class);

        bind(ParamExceptionMapper.class);
        bind(IOExceptionMapper.class);
        bind(ContainerExceptionMapper.class);

        bind(ObjectMapper.class).to(GeoLabelObjectMapper.class);
        bind(TransformationDescriptionResources.class);

        bind(MetadataTransformer.class);
        bind(PngEncoder.class);

        Map<String, String> jerseyInitPrams = new HashMap<>();
        jerseyInitPrams.put(ServletContainer.FEATURE_FILTER_FORWARD_ON_404, "true");

        // Simple CORS filter
        filter("/api*").through(CORSFilter.class);

        // api endpoint served by jersey
        serve("/api*", "/api-docs/*").with(GuiceContainer.class, jerseyInitPrams);

    }
}
