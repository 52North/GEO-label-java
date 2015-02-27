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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.n52.geolabel.server.config.GeoLabelConfig;
import static org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder.Type.FLOAT;
import static org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder.Type.INTEGER;

/**
 *
 * @author Daniel
 */
public class NumericExpressionResultFunctionTest {

    @Test
    public void doubleWithoutFractionIsParsed() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%d", FLOAT), values);

        boolean b = nerf.eval("1.0");
        assertFalse(b);
        assertThat("double is in values as long", values, hasItem(new Double("1")));
        assertThat("double is not in values as double", values, not(hasItem(new Long("1"))));
    }

    @Test
    public void doubleZeroIsParsedAsDouble() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%f", FLOAT), values);

        boolean b = nerf.eval("0.0");
        assertFalse(b);
        assertThat("double is in values as double", values, hasItem(new Double("0.0")));
        assertThat("double is not in values as long", values, not(hasItem(new Long("1"))));
    }

    @Test
    public void doubleWithCommaFractionIsNotParsed() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%f", FLOAT), values);

        boolean b = nerf.eval("1,1");
        assertFalse(b);
        assertThat("no long is in values", values, not(hasItem(new Long("1"))));
        assertThat("no double is in values", values, not(hasItem(new Double("1.1"))));
        assertThat("unknown value number is in values", values, hasItem(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER));
    }

    @Test
    public void nonNumberIsNotParsed() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%f", FLOAT), values);

        boolean b = nerf.eval("hello, world.");
        assertFalse(b);
        assertThat("unknown value number is in values", values, hasItem(GeoLabelConfig.EXPRESSION_HAD_NO_RESULT_NUMBER));
    }

    @Test
    public void doubleIsParsed() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%f", FLOAT), values);

        boolean b = nerf.eval("1.1");
        assertFalse(b);
        assertThat("no long is in values", values, not(hasItem(new Long("1"))));
        assertThat("double is in values", values, hasItem(new Double("1.1")));
    }

    @Test
    public void longIsParsed() {
        ArrayList<Object> values = new ArrayList<>();
        NumericExpressionResultFunction nerf = new NumericExpressionResultFunction(new FacetTransformationDescription.TypedPlaceholder("%d", INTEGER), values);

        boolean b = nerf.eval("42");
        assertFalse(b);
        assertThat("long is in values", values, hasItem(new Long("42")));
        assertThat("no double is in values", values, not(hasItem(new Double("42.0"))));
    }

}
