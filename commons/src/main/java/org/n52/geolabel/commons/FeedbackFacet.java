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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({ UserFeedbackFacet.class, ExpertFeedbackFacet.class })
public class FeedbackFacet extends LabelFacet {

    private int totalItems;

    private int ratingCount;

    private double averageRating;

    public int getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @XmlElement
    public int getRatingCount() {
        return this.ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    @XmlElement
    public double getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FeedbackFacet [totalItems=");
        builder.append(this.totalItems);
        builder.append(", ratingCount=");
        builder.append(this.ratingCount);
        builder.append(", averageRating=");
        builder.append(this.averageRating);
        builder.append("]");
        return builder.toString();
    }

    // @XmlElement
    // PUBLIC DOUBLE GETAVERAGERATING() {
    // IF (THIS.AVERAGERATING == NULL && !THIS.RATINGS.ISEMPTY()) {
    // THIS.AVERAGERATING = DOUBLE.VALUEOF(0D);
    // FOR (DOUBLE RATING : THIS.RATINGS)
    // THIS.AVERAGERATING = DOUBLE.VALUEOF(THIS.AVERAGERATING.DOUBLEVALUE() + RATING.DOUBLEVALUE());
    // THIS.AVERAGERATING = DOUBLE.VALUEOF(THIS.AVERAGERATING.DOUBLEVALUE() / THIS.RATINGS.SIZE());
    // }
    //
    // RETURN THIS.AVERAGERATING;
    // }

    // public void addRating(double rating) {
    // this.ratings.add(Double.valueOf(rating));
    // this.averageRating = null;
    // }
    // public void addFeedbacks(int feedbackCount) {
    // this.totalItems += feedbackCount;
    // }
}