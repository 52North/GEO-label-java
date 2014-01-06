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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.name.Names;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class GeoLabelModule extends JerseyServletModule {

    protected static final Logger log = LoggerFactory.getLogger(TransformationDescriptionLoader.class);

    private static final String HOME_CONFIG_FILE = "org.n52.geolabel.server.properties";

    @Override
    protected void configureServlets() {
        try {
            Properties props = loadProperties("server.properties");
            props = updateFromUserHome(props, HOME_CONFIG_FILE);
            Names.bindProperties(binder(), props);
        }
        catch (Exception e) {
            log.error("Could not load properties file.", e);
        }

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

    protected Properties updateFromUserHome(Properties props, String homeConfigFile) {
        log.debug("Updating properties {} from {}", props, homeConfigFile);

        String home = System.getProperty("user.home");
        log.debug("Used home directory: {}", home);

        if (home != null) {
            File homeDirectory = new File(home);

            try {
                if (homeDirectory.isDirectory()) {
                    File configFile = new File(homeDirectory, homeConfigFile);
                    if (configFile.exists())
                        try (Reader r = new FileReader(configFile);) {
                            props.load(r);
                            log.info("Loaded properties (overwriting defaults) from {}", configFile);
                        }
                    else
                        log.debug("No config file in user home ({}), let's see if the defaults work...", homeDirectory);
                }
            }
            catch (IOException e) {
                log.error("Could not load properties.", e);
            }
        }
        else
            log.warn("user.home is not specified. Will try to use fallback resources.");

        log.debug("Updated properties from user home file {}: {}", HOME_CONFIG_FILE, props);
        return props;
    }

    protected Properties loadProperties(String name) throws Exception {
        log.trace("Loading properties for {}", name);

        Properties properties = new Properties();
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource(name);
        properties.load(url.openStream());

        log.trace("Loaded properties: {}", properties);
        return properties;
    }
}
