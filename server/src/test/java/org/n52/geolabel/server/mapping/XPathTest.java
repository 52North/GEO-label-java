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
        InputStream metadataStream = getClass().getClassLoader().getResourceAsStream("testfiles/metadata/DigitalClimaticAtlas_mt_an_GEOlabel.xml");
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
