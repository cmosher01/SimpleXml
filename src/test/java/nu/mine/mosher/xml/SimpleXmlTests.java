package nu.mine.mosher.xml;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.TransformerException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleXmlTests {
    static {
        VerifyJaxpStubs.verify();
    }

    @Test
    void nominal() throws TransformerException, SAXException {
        final String xml = "<?xml   version = \"1.0\"   encoding = \"utf-8\" ?>" +
            " <test ><this /></test> ";
        final SimpleXml uut = new SimpleXml(xml);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><test><this/></test>", uut.toString());

        final String rng = "<?xml version=\"1.0\"?>" +
            "<element name=\"test\" xmlns=\"http://relaxng.org/ns/structure/1.0\">" +
            "<element name=\"this\"><empty/></element>" +
            "</element>";
        uut.validate(rng);
        assertTrue(uut.isValid(rng));

        final String xslt = "<?xml version=\"1.0\"?>" +
            "<xsl:stylesheet version=\"3.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
            "  <xsl:template match=\"test\">" +
            "    <xsl:element name=\"A\">" +
            "      <xsl:apply-templates select=\"@* | node()\"/>" +
            "    </xsl:element>" +
            "  </xsl:template>" +
            "  <xsl:template match=\"this\">" +
            "    <xsl:element name=\"B\">" +
            "      <xsl:apply-templates select=\"@* | node()\"/>" +
            "    </xsl:element>" +
            "  </xsl:template>" +
            "</xsl:stylesheet>";
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><A><B/></A>", uut.transform(xslt));
    }

    @Test
    void illegalXml() {
        final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><bad>";
        assertThrows(SAXParseException.class, () -> new SimpleXml(xml));
    }

    @Test
    void invalidXml() throws SAXParseException {
        final SimpleXml uut = new SimpleXml("<?xml version=\"1.0\"?><test><this/></test>");
        final String rng = "<?xml version=\"1.0\"?>" + "<element name=\"test\" xmlns=\"http://relaxng.org/ns/structure/1.0\">" + "<element name=\"that\"><empty/></element>" + "</element>";

        final Throwable throwable = assertThrows(SAXParseException.class, () -> uut.validate(rng));
        assertTrue(throwable.getMessage().contains("this") && throwable.getMessage().contains("that"));
    }

    @Test
    void paramBoolean() throws SAXParseException, TransformerException {
        final SimpleXml uut = new SimpleXml("<?xml version=\"1.0\"?><test><this/></test>");
        final String xslt = "<?xml version=\"1.0\"?>" +
            "<xsl:stylesheet version=\"3.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"" +
            "  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"" +
            "  xmlns:fn=\"http://www.w3.org/2005/xpath-functions\">" +
            "  <xsl:param name=\"p\" as=\"xs:boolean\" select=\"fn:false()\"/>" +
            "  <xsl:template match=\"test\">" +
            "    <xsl:element name=\"A\">" +
            "      <xsl:apply-templates select=\"@* | node()\"/>" +
            "    </xsl:element>" +
            "  </xsl:template>" +
            "  <xsl:template match=\"this\">" +
            "    <xsl:if test=\"$p\">" +
            "      <xsl:element name=\"B\">" +
            "        <xsl:apply-templates select=\"@* | node()\"/>" +
            "      </xsl:element>" +
            "    </xsl:if>" +
            "  </xsl:template>" +
            "</xsl:stylesheet>";
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><A><B/></A>", uut.transform(xslt, Collections.singletonMap("p", true)));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><A/>", uut.transform(xslt, Collections.singletonMap("p", false)));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><A/>", uut.transform(xslt));
    }

    @Test
    void preventXxe() {
        final String xml = "<?xml   version = \"1.0\"   encoding = \"utf-8\" ?>\n" +
            "<!DOCTYPE foo [\n" +
            "  <!ELEMENT foo ANY >\n" +
            "  <!ENTITY xxe SYSTEM \"file:///etc/passwd\" >\n" +
            "]>\n" +
            "<foo>&xxe;</foo>\n";
        assertThrows(SAXParseException.class, () -> new SimpleXml(xml));
    }
}
