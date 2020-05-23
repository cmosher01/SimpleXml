# SimpleXml

DEPRECATED: This library is no longer maintained. It will remain archived, for reference purposes only.

A simple library to handle XML from a Java application.

Pass in XML as Strings, for Xerces parsing, RELAX NG validation, and SAXON 9 transformation.

```java
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
```
