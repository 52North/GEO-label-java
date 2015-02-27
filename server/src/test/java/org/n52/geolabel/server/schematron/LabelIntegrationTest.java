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
package org.n52.geolabel.server.schematron;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Daniel, roughly based on 52°North smartEditor
 */
public class LabelIntegrationTest {

    private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

    public static Document newDocument(boolean isNamespaceAware) throws ParserConfigurationException {
        docFactory.setNamespaceAware(isNamespaceAware);
        DocumentBuilder lBuilder = docFactory.newDocumentBuilder();
        return lBuilder.newDocument();
    }
    private static TransformerFactory tFactory;

    @BeforeClass
    public static void prepareTransformerFactory() {
        tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);
    }
    
    private Transformer schematronTransformer;

    @Before
    public void prepareTransformer() throws TransformerConfigurationException, IOException {
        URL url = getClass().getResource("/validation/iso_svrl_for_xslt2.xsl");

        schematronTransformer = tFactory.newTransformer(new StreamSource(url.openStream()));
    }

    public Document validate(Document input, Document schematronDocument) throws TransformerException, ParserConfigurationException {
        Document lReport = newDocument(true);
        Source lSource = new DOMSource(input);
        Result lResult = new DOMResult(lReport);
        schematronTransformer.transform(lSource, lResult);
        return lReport;
    }

//    @Test
    public void sensorML101ReferenceIsFound() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new File("sml/hbs-sml101_integrate-reference.xml"));
        
        Document report = validate(document, null);
        System.out.println(report);
    }
}
