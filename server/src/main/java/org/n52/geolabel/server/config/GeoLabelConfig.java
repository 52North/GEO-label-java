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

import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

@WebListener
public class GeoLabelConfig extends GuiceServletContextListener {

    public static final String EXPRESSION_HAD_NO_RESULT_TEXT = "unavailable";

    public static final Object EXPRESSION_HAD_NO_RESULT_NUMBER = Integer.valueOf(0);

    public static int CONNECT_TIMEOUT = 10000;

	public static int READ_TIMEOUT = 20000;

	@Override
	protected Injector getInjector() {
        return Guice.createInjector(new GeoLabelModule());
	}
}
