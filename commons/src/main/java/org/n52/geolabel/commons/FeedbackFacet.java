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
package org.n52.geolabel.commons;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

// TODO consistency when unmarshalled
@XmlSeeAlso({ UserFeedbackFacet.class, ExpertFeedbackFacet.class })
public class FeedbackFacet extends LabelFacet {

	private int totalFeedbacks;

	private List<Double> ratings = new ArrayList<Double>(1);
	private Double avgRating = null;

	public int getTotalFeedbacks() {
		return totalFeedbacks;
	}

	@XmlElement
	public Double getAverageRating() {
		if (avgRating == null && !ratings.isEmpty()) {
			avgRating = 0d;
			for (Double rating : ratings)
				avgRating += rating;
			avgRating /= ratings.size();
		}

		return avgRating;
	}

	public int getTotalRatings() {
		return ratings.size();
	}

	public void addRating(double rating) {
		ratings.add(rating);
		avgRating = null;
	}

	public void addFeedbacks(int feedbackCount) {
		totalFeedbacks += feedbackCount;
	}
}