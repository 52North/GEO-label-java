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

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.xml.sax.InputSource;

public class XPathTest {

    @Test
    public void problematicPaths() {
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("4.0/DigitalClimaticAtlas_mt_an_GEOlabel.xml");
        InputSource input = new InputSource(metadataStream);

        String path = "boolean(normalize-space(string(//*[local-name()='contact']/*[local-name()='CI_ResponsibleParty'] | //*[local-name()='ptcontac']/*[local-name()='cntinfo'] | //*[local-name()='pointOfContact']/*[local-name()='CI_ResponsibleParty'])))";

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();

        try {
            XPathExpression pathExpression = xPath.compile(path);
            Object evaluate = pathExpression.evaluate(input);
            System.out.println(evaluate);
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

}
