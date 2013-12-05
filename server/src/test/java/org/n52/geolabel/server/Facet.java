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

import org.n52.geolabel.commons.Label;
import org.n52.geolabel.commons.LabelFacet;

/**
 * Structure to specify facets for tests against SVG fill color and availability
 * 
 */
public enum Facet {
	PRODUCER_PROFILE("#ED1E7F") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getProducerProfileFacet();
		}
	},
	LINEAGE("#ED1E7F") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getLineageFacet();
		}
	},
	PRODUCER_COMMENTS("#ED1E7F") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getProducerCommentsFacet();
		}
	},
	STANDARDS_COMPLIANCE("#0F9B49") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getStandardsComplianceFacet();
		}
	},
	QUALITY_INFORMATION("#0F9B49") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getQualityInformationFacet();
		}
	},
	USER_FEEDBACK("#F58220") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getUserFeedbackFacet();
		}
	},
	EXPERT_REVIEW("#4374B9") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getExpertFeedbackFacet();
		}
	},
	CITATIONS_INFORMATION("#4374B9") {
		@Override
		public LabelFacet getFacet(Label label) {
			return label.getCitationsFacet();
		}
	};

	private String colorNotAvailable;
	private String colorAvailable;

	private Facet(String colorAvailable, String colorNotAvailable) {
		this.colorAvailable = colorAvailable;
		this.colorNotAvailable = colorNotAvailable;
	}

	private Facet(String colorAvailable) {
		this(colorAvailable, "#FFFFFF");
	}

	public String getColorAvailable() {
		return colorAvailable;
	}

	public String getColorNotAvailable() {
		return colorNotAvailable;
	}

	public abstract LabelFacet getFacet(Label label);
}