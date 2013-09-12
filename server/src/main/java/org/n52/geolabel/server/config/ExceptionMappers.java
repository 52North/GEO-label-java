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

import java.io.IOException;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.container.ContainerException;

public interface ExceptionMappers {

	final static Logger log = LoggerFactory.getLogger(ExceptionMappers.class);

	@Provider
	@Singleton
	public class ParamExceptionMapper implements ExceptionMapper<ParamException> {
		@Override
		public Response toResponse(ParamException exception) {
			return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
					.entity(exception.getParameterName() + " has an invalid format").build();
		}
	}

	@Provider
	@Singleton
	public class IOExceptionMapper implements ExceptionMapper<IOException> {
		@Override
		public Response toResponse(IOException exception) {
			log.error("Error processing request", exception);
			return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Error processing request").build();
		}
	}
	
	@Provider
	@Singleton
	public class ContainerExceptionMapper implements ExceptionMapper<ContainerException> {
		@Override
		public Response toResponse(ContainerException exception) {
			log.error("Unexpected error while handling request", exception);
			return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Error processing request").build();
		}
	}
}
