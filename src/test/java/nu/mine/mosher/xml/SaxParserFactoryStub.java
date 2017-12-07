package nu.mine.mosher.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParserFactoryStub extends SAXParserFactory {
    public SaxParserFactoryStub() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFeature(final String name, final boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getFeature(final String name) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException();
    }
}
