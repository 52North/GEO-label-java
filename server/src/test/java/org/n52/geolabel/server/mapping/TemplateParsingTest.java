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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription.HoveroverInformation;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder;
import org.n52.geolabel.server.mapping.description.FacetTransformationDescription.TypedPlaceholder.Type;

public class TemplateParsingTest {

    @Test
    public void testThatPlaceholderTypesAreCorrectlyParsed() {
        HoveroverInformation hoverover = new HoveroverInformation();
        hoverover.setTemplate("this is a decimal %d and a string %s");

        ArrayList<TypedPlaceholder> actual = hoverover.getTypedPlaceholders();

        assertThat("placeholder array is correctly parsed", actual, hasItem(new TypedPlaceholder("%d", Type.DECIMAL)));
        assertThat("placeholder array is correctly parsed", actual, hasItem(new TypedPlaceholder("%s", Type.STRING)));
    }

}
