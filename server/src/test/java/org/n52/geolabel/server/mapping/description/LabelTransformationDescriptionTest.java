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
package org.n52.geolabel.server.mapping.description;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.n52.geolabel.server.mapping.MetadataTransformer;
import org.n52.geolabel.server.mapping.MetadataTransformerTest;
import org.n52.geolabel.server.mapping.description.LabelTransformationDescription;

public class LabelTransformationDescriptionTest {

	@Test
	public void testParseTransformationDescription() throws IOException {

		MetadataTransformer metadataTransformer = MetadataTransformerTest
				.newMetadataTransformer();
		metadataTransformer
				.readTransformationDescription(MetadataTransformerTest.class
						.getResourceAsStream("transformer.xml"));

		LabelTransformationDescription description = metadataTransformer
				.getTransformationDescriptions().get(0);

		assertTrue(description.namespaceMappings.length == 1);
		assertTrue(description.namespaceMappings[0].prefix.equals("gmd"));
		assertTrue(description.namespaceMappings[0].namespace
				.equals("http://www.isotc211.org/2005/gmd"));

		assertTrue(description.facetDescriptions.length == 8);
	}
}
