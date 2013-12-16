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
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlSeeAlso({ProducerProfileFacet.class,
             LineageFacet.class,
             ProducerCommentsFacet.class,
             StandardsComplianceFacet.class,
             QualityInformationFacet.class,
             FeedbackFacet.class,
             CitationsFacet.class})
public abstract class LabelFacet {

    /**
     * Mapping LML availability indicator
     */
    public enum Availability {
        AVAILABLE(1), NOT_AVAILABLE(0), AVAILABLE_HIGHER(2), NA( -1);

        protected int code;

        private Availability(int code) {
            this.code = code;
        }

        public static Availability fromString(String v) {
            try {
                return fromLmlCode(Integer.valueOf(v));
            }
            catch (NumberFormatException e) {
                return NA;
            }
        }

        public static Availability fromLmlCode(Integer v) {
            switch (v.intValue()) {
            case 0:
                return NOT_AVAILABLE;
            case 1:
                return AVAILABLE;
            case 2:
                return AVAILABLE_HIGHER;
            default:
                return NA;
            }
        }

        public static class AvailabilityAdapter extends XmlAdapter<Integer, Availability> {

            @Override
            public Integer marshal(Availability v) throws Exception {
                return Integer.valueOf(v.code);
            }

            @Override
            public Availability unmarshal(Integer v) throws Exception {
                return fromLmlCode(v);
            }

        }
    }

    @XmlElement
    private Availability availability = Availability.NA;

    @XmlElement
    private String drilldownURL;

    public Availability getAvailability() {
        return this.availability;
    }

    public String getDrilldownURL() {
        return this.drilldownURL;
    }

    /**
     * Updating availability status. AVAILABLE beats all, NOT_AVAILABLE beats NA
     *
     * @param availabilityP
     */
    public void updateAvailability(Availability availabilityP) {
        if (this.availability == Availability.NA || availabilityP == Availability.AVAILABLE)
            this.availability = availabilityP;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LabelFacet [");
        if (this.availability != null) {
            builder.append("availability=");
            builder.append(this.availability);
            builder.append(", ");
        }
        if (this.drilldownURL != null) {
            builder.append("drilldownURL=");
            builder.append(this.drilldownURL);
        }
        if (getClass().getCanonicalName() != null) {
            builder.append("class=");
            builder.append(getClass().getCanonicalName());
        }
        builder.append("]");
        return builder.toString();
    }

}
