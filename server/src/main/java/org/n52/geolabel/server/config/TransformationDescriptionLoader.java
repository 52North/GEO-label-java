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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.xpath.XPathExpressionException;

import org.codehaus.jackson.map.ObjectMapper;
import org.n52.geolabel.server.config.TransformationDescriptionResources.Source;
import org.n52.geolabel.server.mapping.description.TransformationDescription;
import org.n52.geolabel.server.mapping.description.TransformationDescription.TransformationDescriptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.inject.name.Named;

@Singleton
public class TransformationDescriptionLoader {

    protected static final Logger log = LoggerFactory.getLogger(TransformationDescriptionLoader.class);

    private TransformationDescriptionResources resources;

    private ObjectMapper mapper;

    private boolean useLocalOnly;

    private static Set<TransformationDescription> descriptions;

    private static Map<URL, Source> transformationDescriptionUsedSource = new HashMap<>();

    @Inject
    public TransformationDescriptionLoader(TransformationDescriptionResources resources,
                                           ObjectMapper mapper,
                                           @Named("transformer.useLocalOnly")
                                           boolean useLocalOnly) {
        this.resources = resources;
        this.mapper = mapper;
        this.useLocalOnly = useLocalOnly;
    }

    private TransformationDescription readTransformationDescription(InputStream input) throws IOException {
        /**
         * using JSONJAXB
         */
        // try {
        // JSONJAXBContext context = new JSONJAXBContext(JSONConfiguration.mappedJettison().build(),
        // LabelTransformationDescription.class);
        // // JSONConfiguration jsonConfiguration = context.getJSONConfiguration();
        // JSONUnmarshaller unmarshaller = context.createJSONUnmarshaller();
        //
        // Object obj = unmarshaller.unmarshalFromJSON(input, LabelTransformationDescription.class);
        // LabelTransformationDescription transformationDescription = (LabelTransformationDescription) obj;
        //
        // add(transformationDescription);
        // }
        // catch (JAXBException e) {
        // throw new IOException("Error while parsing transformation description stream", e);
        // }

        /**
         * using Jackson
         */
        TransformationDescriptionWrapper wrapper = this.mapper.readValue(input, TransformationDescriptionWrapper.class);

        return wrapper.transformationDescription;
    }

    /**
     * Reads {@link LabelTransformationDescription} from passed XML {@link File} .
     *
     */
    private TransformationDescription readTransformationDescription(File descriptionFile) throws FileNotFoundException,
            IOException {
        return readTransformationDescription(new FileInputStream(descriptionFile));
    }

    public Set<TransformationDescription> load() {
        if (descriptions == null)
            if (this.useLocalOnly)
                descriptions = loadLocal(this.resources);
            else
                descriptions = loadWithFallback(this.resources);

        return descriptions;
    }

    private Set<TransformationDescription> loadLocal(final TransformationDescriptionResources res) {
        log.info("Loading resources _locally only_ from {}", res);

        final Set<TransformationDescription> ds = new HashSet<>();

        Set<Entry<URL, String>> entrySet = this.resources.getResources().entrySet();
        for (Entry<URL, String> entry : entrySet) {
            log.debug("Loading transformation description from fallback {}", entry.getValue());

            TransformationDescription td = null;
            log.debug("Using fallback {} for URL {}", entry.getValue(), entry.getKey());
            InputStream stream = getClass().getResourceAsStream(entry.getValue());

            try {
                td = readTransformationDescription(stream);
                log.debug("Loaded transformation description from {} ", entry.getValue());
                setUsedSource(entry.getKey(), Source.FALLBACK);
            }
            catch (IOException e) {
                log.error("There was a problem loading transformation description from {}: {} : {}",
                          entry.getValue(),
                          e.getClass(),
                          e.getMessage());
            }

            if (td != null) {
                ds.add(td);
                log.debug("Read transformation description: {}", td);
            }
            else
                log.error("Could load neither from URL nor fallback!");
        }

        // init desriptions
        for (TransformationDescription td : ds)
            try {
                td.initXPaths();
                log.debug("Added description: {}", td);
            }
            catch (XPathExpressionException e) {
                log.error("Error while compiling XPaths in tranformation description {}", td, e);
            }

        log.debug("Loaded {} transformation descriptions with the names {}", Integer.valueOf(ds.size()), getNames(ds));

        return ds;
    }

    private Set<TransformationDescription> loadWithFallback(final TransformationDescriptionResources res) {
        log.info("Loading resources from {}", res);

        final Set<TransformationDescription> ds = new HashSet<>();

        // load from server with fallback
        Set<Entry<URL, String>> entrySet = this.resources.getResources().entrySet();
        for (Entry<URL, String> entry : entrySet) {
            log.debug("Loading transformation description from URL {} with fallback {}",
                      entry.getKey(),
                      entry.getValue());

            TransformationDescription td = null;
            try {
                File temp = File.createTempFile("geolabel_transformationDescription_", ".json");
                ByteStreams.copy(Resources.newInputStreamSupplier(entry.getKey()), Files.newOutputStreamSupplier(temp));
                log.debug("Saved transformation description from {} to {}", entry.getKey(), temp);

                td = readTransformationDescription(temp);
                log.debug("Loaded transformation description from {} ", entry.getKey());
                setUsedSource(entry.getKey(), Source.URL);
            }
            catch (Exception e) {
                log.warn("There was a problem loading transformation description from {}: {} : {}",
                         entry.getValue(),
                         e.getClass(),
                         e.getMessage());
            }

            if (td == null) {
                log.debug("Using fallback {} for URL {}", entry.getValue(), entry.getKey());
                InputStream stream = getClass().getResourceAsStream(entry.getValue());

                try {
                    td = readTransformationDescription(stream);
                    log.debug("Loaded transformation description from {} ", entry.getValue());
                    setUsedSource(entry.getKey(), Source.FALLBACK);
                }
                catch (IOException e) {
                    log.error("There was a problem loading transformation description from {}: {} : {}",
                              entry.getValue(),
                              e.getClass(),
                              e.getMessage());
                }
            }

            if (td != null) {
                ds.add(td);
                log.debug("Read transformation description: {}", td);
            }
            else
                log.error("Could load neither from URL nor fallback!");
        }

        // init desriptions
        for (TransformationDescription td : ds)
            try {
                td.initXPaths();
                log.debug("Added description: {}", td);
            }
            catch (XPathExpressionException e) {
                log.error("Error while compiling XPaths in tranformation description {}", td, e);
            }

        log.debug("Loaded {} transformation descriptions with the names {}", Integer.valueOf(ds.size()), getNames(ds));

        return ds;
    }

    private String getNames(Set<TransformationDescription> ds) {
        StringBuilder sb = new StringBuilder();

        for (TransformationDescription td : ds) {
            sb.append(td.name);
            sb.append(" ");
        }
        return sb.toString();
    }

    public void setUsedSource(URL url, Source online) {
        transformationDescriptionUsedSource.put(url, online);
    }

    public Map<URL, Source> getUsedSources() {
        return transformationDescriptionUsedSource;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TransformationDescriptionLoader [");
        if (this.resources != null) {
            builder.append("resources=");
            builder.append(this.resources);
        }
        builder.append("]");
        return builder.toString();
    }

}
