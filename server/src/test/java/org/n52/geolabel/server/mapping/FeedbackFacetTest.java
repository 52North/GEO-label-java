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
package org.n52.geolabel.server.mapping;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.n52.geolabel.commons.FeedbackFacet;

public class FeedbackFacetTest {

	@Test
	public void testGetTotalFeedbacks() {
		FeedbackFacet facet = new FeedbackFacet();
		assertTrue(facet.getTotalFeedbacks() == 0);
		facet.addFeedbacks(5);
		facet.addFeedbacks(5);
		assertTrue(facet.getTotalFeedbacks() == 10);
	}

	@Test
	public void testGetAverageRating() {
		FeedbackFacet facet = new FeedbackFacet();
		assertTrue(facet.getAverageRating() == null);
		facet.addRating(1);
		assertTrue(facet.getAverageRating().doubleValue() == 1);
		facet.addRating(2);
		facet.addRating(3);
		assertTrue(facet.getAverageRating().doubleValue() == 2);
	}

	@Test
	public void testGetTotalRatings() {
		FeedbackFacet facet = new FeedbackFacet();
		assertTrue(facet.getTotalRatings() == 0);
		facet.addRating(1);
		facet.addRating(2);
		facet.addRating(3);
		assertTrue(facet.getTotalRatings() == 3);
	}

}
