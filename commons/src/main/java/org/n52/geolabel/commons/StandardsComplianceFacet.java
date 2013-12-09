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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "standardsCompliance")
public class StandardsComplianceFacet extends LabelFacet {
	@XmlElement(name = "metadataStandardName")
	private Set<String> standards;

	public Collection<String> getStandards() {
        if (this.standards == null)
			return Collections.emptySet();

        return this.standards;
	}

	public void addStandard(String standard) {
        if (this.standards == null)
            this.standards = new HashSet<String>();
		this.standards.add(standard);
	}
}