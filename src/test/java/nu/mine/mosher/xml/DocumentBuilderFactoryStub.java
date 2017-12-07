package nu.mine.mosher.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocumentBuilderFactoryStub extends DocumentBuilderFactory {
    public DocumentBuilderFactoryStub() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String name, final Object value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(final String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFeature(final String name, final boolean value) throws ParserConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getFeature(final String name) throws ParserConfigurationException {
        throw new UnsupportedOperationException();
    }
}
