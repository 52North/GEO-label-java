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
package org.n52.geolabel.client;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeoLabelCache {
	protected static int MAXSIZE = 50;

	private static LinkedHashMap<String, String> svgMap = new LinkedHashMap<String, String>(
			16, 0.75f, true) {

		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
			return size() > 50;
		};
	};

	public static boolean hasSVG(String identifier) {
		return svgMap.containsKey(identifier);
	}

	public static String getSVG(String identifier) {
		return svgMap.get(identifier);
	}

	public static void putSVG(String identifier, String svg) {
		svgMap.put(identifier, svg);
	}
}
