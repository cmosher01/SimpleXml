package nu.mine.mosher.xml;

import javax.xml.transform.*;

public class TransformerFactoryStub extends TransformerFactory {
    public TransformerFactoryStub() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transformer newTransformer(final Source source) throws TransformerConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Templates newTemplates(final Source source) throws TransformerConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Source getAssociatedStylesheet(final Source source, final String media, final String title, final String charset) throws TransformerConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setURIResolver(final URIResolver resolver) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URIResolver getURIResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFeature(final String name, final boolean value) throws TransformerConfigurationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getFeature(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setErrorListener(final ErrorListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ErrorListener getErrorListener() {
        throw new UnsupportedOperationException();
    }
}
