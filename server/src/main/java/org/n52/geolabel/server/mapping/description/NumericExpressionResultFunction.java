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

import java.util.ArrayList;
import org.n52.geolabel.server.config.GeoLabelConfig;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Nüst
 */
public class NumericExpressionResultFunction implements FacetTransformationDescription.ExpressionResultFunction {

    protected static final Logger log = LoggerFactory.getLogger(NumericExpressionResultFunction.class);
    
    private final ArrayList<Object> values;

    private final FacetTransformationDescription.TypedPlaceholder tp;

    public NumericExpressionResultFunction(TypedPlaceholder tp, ArrayList<Object> values) {
        this.values = values;
        this.tp = tp;
    }

    @Override
    public boolean eval(String value) {
        if (!value.isEmpty()) {
            try {
                if (tp.type.equals(TypedPlaceholder.Type.INTEGER)) {
                    values.add(Long.valueOf(value));
                } else if (tp.type.equals(TypedPlaceholder.Type.FLOAT)) {
                    values.add(Double.valueOf(value));
                } else {
                    values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
                }
            } catch (NumberFormatException e) {
                log.trace("Could not parse number returned by expression: {} | defined type: {}", value, this.tp);
                values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
            }
            return false;
        }
        values.add(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER);
        return true;
    }

}
