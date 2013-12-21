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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebServiceException;

import org.apache.commons.io.IOUtils;
import org.n52.geolabel.server.config.TransformationDescriptionLoader;
import org.n52.geolabel.server.config.TransformationDescriptionResources;
import org.n52.geolabel.server.config.TransformationDescriptionResources.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.SessionScoped;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/v1/transformations")
@Api(value = "/v1/transformations", description = "Access to the transformation rules used to generate a label")
@SessionScoped
public class TransformationsResourceV1 {

    protected static final Logger log = LoggerFactory.getLogger(TransformationsResourceV1.class);

    @XmlRootElement(name = "transformation")
    public static class Transformation {

        public String url;

        private String fallback;

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

        public String getFallback() {
            return this.fallback;
        }

        public void setFallback(String fallback) {
            this.fallback = fallback;
        }
    }

    @XmlRootElement(name = "transformations")
    public static class TransformationsHolder {

        @XmlElementRef
        private ArrayList<Transformation> transformations = new ArrayList<>();

        public TransformationsHolder() {
            //
        }

        public TransformationsHolder(UriInfo uri,
                                     Map<URL, String> transformationDescriptionResources,
                                     Map<URL, Source> usedSources) {
            for (Entry<URL, String> entry : transformationDescriptionResources.entrySet()) {
                Transformation t = new Transformation();
                t.url = entry.getKey().toString();

                URI fallbackUrl = getUriToFallbackFile(uri, entry.getValue());
                t.setFallback(fallbackUrl.toString());

                Source source = usedSources.get(entry.getKey());
                t.used = (source == null) ? Source.NA.toString() : source.toString();

                this.transformations.add(t);
                log.debug("Created transformation {}", t);
            }
        }

        private URI getUriToFallbackFile(UriInfo uri, String fallback) {
            String id = getFallbackFileId(fallback);
            URI fallbackUri = uri.getRequestUriBuilder().path(id).build();
            return fallbackUri;
        }

	}

    private TransformationDescriptionLoader loader;

    private TransformationDescriptionResources resources;

    private UriInfo uri;

	@Inject
    public TransformationsResourceV1(@Context
    UriInfo uri, TransformationDescriptionResources resources,
                                     TransformationDescriptionLoader loader) {
        this.uri = uri;
        this.resources = resources;
        this.loader = loader;
	}

    protected static String getFallbackFileId(final String fallback) {
        return fallback.substring(fallback.lastIndexOf("/") + 1, fallback.length());
    }

	@GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Returns a list of used transformations")
    public Response getTransformationsInfo(@PathParam("id")
    String id) {
        Collection<String> res = this.resources.getResources().values();
        String path = null;
        for (String r : res) {
            String name = getFallbackFileId(r);
            if (name.equals(id))
                path = r;
        }

        if (path != null) {
            InputStream input = getClass().getResourceAsStream(path);
            try {
                String s = IOUtils.toString(input);
                return Response.ok(s).build();
            }
            catch (IOException e) {
                throw new WebServiceException(e);
            }
        }

        return Response.ok().status(Status.NOT_FOUND).build();
    }

    private ArrayList<String> getLocalResourcesNames() {
        Collection<String> res = this.resources.getResources().values();
        ArrayList<String> names = new ArrayList<>();
        for (String r : res) {
            String name = getFallbackFileId(r);
            names.add(name);
        }
        return names;
    }

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Returns a list of used transformations")
    public Response getTransformationsInfo() {
        TransformationsHolder holder = new TransformationsHolder(this.uri,
                                                                 this.resources.getResources(),
                                                                 this.loader.getUsedSources());

        return Response.ok().entity(holder).build();

    }

}
