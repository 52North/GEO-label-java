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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.SessionScoped;

@Path("/")
//@Api(value = "/", description = "Operations to explore the service cache")
@SessionScoped
public class ApiResource {

    private static Logger log = LoggerFactory.getLogger(ApiResource.class);

    private URI baseUri;

    @Inject
    public ApiResource(@Context UriInfo uri) {
        this.baseUri = uri.getBaseUri();

        log.info("NEW {} @ {}", this, this.baseUri);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
//    @ApiOperation(value = "Returns a list of currently available api versions")
    public Response getApiListing() {
        StringBuilder sb = new StringBuilder();

        sb.append(" { ");

        sb.append("\"currentVersion\" : \"");
        sb.append(this.baseUri);
        sb.append("v1\"");
        sb.append(" , ");

        sb.append("\"v1\" : \"");
        sb.append(this.baseUri);
        sb.append("v1");
        sb.append("\"");

        sb.append(" } ");

        return Response.ok().entity(sb.toString()).build();
    }

    @GET
    @Path("/v1")
    @Produces({MediaType.APPLICATION_JSON})
//    @ApiOperation(value = "Returns a list of available api endpoints")
    public Response getV1Listing() {
        StringBuilder sb = new StringBuilder();

        sb.append(" { ");

        sb.append("\"version\" : \"v1\"");
        sb.append(" , ");

        sb.append("\"cache\" : \"");
        sb.append(this.baseUri);
        sb.append("v1/cache");
        sb.append("\"");
        sb.append(",");

        sb.append("\"transformations\" : \"");
        sb.append(this.baseUri);
        sb.append("v1/transformations");
        sb.append("\"");
        sb.append(",");

        sb.append("\"lml-label-generation\" : \"");
        sb.append(this.baseUri);
        sb.append("v1/lml");
        sb.append("\"");
        sb.append(",");

        sb.append("\"svg-label-generation\" : \"");
        sb.append(this.baseUri);
        sb.append("v1/svg");
        sb.append("\"");

        sb.append(" } ");

        return Response.ok().entity(sb.toString()).build();
    }

    @Path("/ping")
    public class PingResource {

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.WILDCARD)
        public Response createPet() {
            // return a basic map. This will be marshalled automatically into a 
            // json object with a single property
            Map<String, String> pong = new HashMap<>();
            pong.put("pong", "Hello, World!");
            return Response.status(200).entity(pong).build();
        }
    }
}
