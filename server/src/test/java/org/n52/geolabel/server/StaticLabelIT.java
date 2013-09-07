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
package org.n52.geolabel.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EnumSet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

public class StaticLabelIT extends XMLTestCaseLabelBase {
	@Test
	public void testStaticAllFactesAvailable() throws MalformedURLException, IOException, XpathException, SAXException {
		String allAvailableSVG = getServerResourceAsString("11111111.svg?size=100");

		performCommonLabelChecks(allAvailableSVG, 100);
		performFacetChecks(allAvailableSVG, EnumSet.allOf(Facet.class));
	}

	@Test
	public void testStaticNoFactesAvailable() throws MalformedURLException, IOException, XpathException, SAXException {
		String noAvailableSVG = getServerResourceAsString("00000000.svg?size=100");

		performCommonLabelChecks(noAvailableSVG, 100);
		performFacetChecks(noAvailableSVG, EnumSet.noneOf(Facet.class));
	}

	@Test
	public void testStaticPPCIFactesAvailable() throws MalformedURLException, IOException, XpathException, SAXException {
		String noAvailableSVG = getServerResourceAsString("10000001.svg?size=100");

		performCommonLabelChecks(noAvailableSVG, 100);
		performFacetChecks(noAvailableSVG, EnumSet.of(Facet.PRODUCER_PROFILE, Facet.CITATIONS_INFORMATION));
	}

	private String getServerResourceAsString(String url) throws ClientProtocolException, IOException {
		url = getTestServiceUrl() + url;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity responseEntity = httpClient.execute(new HttpGet(url)).getEntity();
		return IOUtils.toString(responseEntity.getContent());
	}
}
