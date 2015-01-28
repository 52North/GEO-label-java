/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.geolabel.server.mapping.description;

import java.util.ArrayList;
import org.n52.geolabel.server.config.GeoLabelConfig;

/**
 *
 * @author Daniel Nüst
 */
public class NumericExpressionResultFunction implements FacetTransformationDescription.ExpressionResultFunction {
    
    private final ArrayList<Object> values;

    public NumericExpressionResultFunction(ArrayList<Object> values) {
        this.values = values;
    }

    @Override
    public boolean eval(String value) {
        if (!value.isEmpty()) {
            try {
                Double d = Double.parseDouble(value);
                if ((d % 1) == 0) {
                    values.add((Number) d.longValue());
                } else {
                    values.add((Number) d);
                }
                //                    Number number = NumberFormat.getInstance(Locale.UK).parse(value);
                //                    values.add(number);
            } catch (NumberFormatException e) {
                // log.error("Could not parse number returned by expression.");
                values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
            }
            return false;
        }
        values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
        return true;
    }
    
}
