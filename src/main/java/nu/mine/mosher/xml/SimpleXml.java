package nu.mine.mosher.xml;

import com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory;
import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.lib.AugmentedSource;
import net.sf.saxon.lib.ErrorGatherer;
import net.sf.saxon.lib.ParseOptions;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Simple wrapper for XML processing.
 * <p>
 * Only supports strings, not files.
 * <p>
 * Handles:
 * XML parsing (using Apache Xerces),
 * validating (RELAX NG XML schema using Jing Trang), and
 * transformation (XSLT using Saxon HE).
 */
public class SimpleXml {
    private static final int INITIAL_BUFFER_SIZE = 1024;

    private static final ClassLoader loader = SimpleXml.class.getClassLoader();

    private static final Class<? extends SAXParserFactory> classSaxParserFactory = SAXParserFactoryImpl.class;
    private static final Class<? extends TransformerFactory> classTransformerFactory = TransformerFactoryImpl.class;
    private static final Class<? extends SchemaFactory> classSchemaFactory = XMLSyntaxSchemaFactory.class;



    private final String original;
    private final String roundtrip;

    public SimpleXml(final String xml) throws SAXParseException {
        this.original = xml;
        this.roundtrip = roundtrip(xml);
    }

    public String getOriginal() {
        return this.original;
    }



    public boolean isValid(final String schema) {
        try {
            validate(schema);
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    public void validate(final String rng) throws SAXException  {
        try {
            createSchema(asStreamSource(rng)).newValidator().validate(asSaxSource(this.roundtrip));
        } catch (final IOException io) {
            // we are only reading from strings, so should never get an IOException
            throw new IllegalStateException("Assumed no IOException would be thrown.", io);
        }
    }

    public String transform(final String xslt) throws TransformerException {
        final StringWriter sOut = new StringWriter(SimpleXml.INITIAL_BUFFER_SIZE);
        createTransformer(asAugSaxSource(xslt)).transform(asSaxSource(this.roundtrip), new StreamResult(sOut));
        return sOut.toString();
    }

    @Override
    public String toString() {
        return this.roundtrip;
    }



    private static StreamSource asStreamSource(final String s) {
        return new StreamSource(new StringReader(s));
    }

    private static String roundtrip(final String xml) throws SAXParseException {
        final StringWriter sOut = new StringWriter(SimpleXml.INITIAL_BUFFER_SIZE);
        try {
            createIdentityTransformer().transform(asNoerrSaxSource(xml), new StreamResult(sOut));
        } catch (final TransformerException e) {
            rewrapSaxParseException(e);
        }
        return sOut.toString();
    }

    private static void rewrapSaxParseException(final TransformerException e) throws SAXParseException {
        final Throwable cause = e.getCause();
        if (cause != null) {
            if (cause instanceof SAXParseException) {
                throw (SAXParseException)cause;
            } else {
                final SAXParseException se = new SAXParseException(cause.getMessage(), null);
                se.initCause(cause);
                throw se;
            }
        }
    }

    private static SAXParserFactory createSaxParserFactory() {
        final SAXParserFactory f = SAXParserFactory.newInstance(SimpleXml.classSaxParserFactory.getName(), SimpleXml.loader);
        f.setValidating(false);
        f.setNamespaceAware(true);
        f.setXIncludeAware(false);
        return f;
    }

    private static SAXParser createSaxParser() {
        try {
            return createSaxParserFactory().newSAXParser();
        } catch (final Throwable e) {
            throw new IllegalStateException("Assumed no exceptions would be thrown.", e);
        }
    }

    private static XMLReader createSaxReader() {
        try {
            return createSaxParser().getXMLReader();
        } catch (final Throwable e) {
            throw new IllegalStateException("Assumed no exceptions would be thrown.", e);
        }
    }

    private static SAXSource asSaxSource(final String xml) {
        return new SAXSource(createSaxReader(), new InputSource(new StringReader(xml)));
    }

    private static AugmentedSource asNoerrSaxSource(final String xml) {
        final ParseOptions options = new ParseOptions();
        options.setErrorListener(new ErrorGatherer(new ArrayList<>()));
        return new AugmentedSource(asSaxSource(xml), options);
    }

    private static AugmentedSource asAugSaxSource(final String xml) {
        final ParseOptions options = new ParseOptions();
        options.setXMLReader(createSaxReader());
        return new AugmentedSource(asSaxSource(xml), options);
    }

    private static TransformerFactory createTransformerFactory() {
        return TransformerFactory.newInstance(SimpleXml.classTransformerFactory.getName(), SimpleXml.loader);
    }

    private static Transformer createIdentityTransformer() {
        try {
            return createTransformerFactory().newTransformer();
        } catch (final Throwable e) {
            throw new IllegalStateException("Assumed no exceptions would be thrown.", e);
        }
    }

    private static Transformer createTransformer(final Source s) throws TransformerConfigurationException {
        return createTransformerFactory().newTransformer(s);
    }

    private static SchemaFactory createSchemaFactory() {
        return SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI, SimpleXml.classSchemaFactory.getName(), SimpleXml.loader);
    }

    private static Schema createSchema(final Source s) throws SAXException {
        final SchemaFactory factory = createSchemaFactory();

        final SysPropSetter p = new SysPropSetter(SAXParserFactory.class.getName()).set(SimpleXml.classSaxParserFactory.getName());
        try {
            return factory.newSchema(s);
        } finally {
            p.reset();
        }
    }
}
