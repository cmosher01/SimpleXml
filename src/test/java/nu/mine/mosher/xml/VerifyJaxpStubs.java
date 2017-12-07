package nu.mine.mosher.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;

/**
 * Run the JVM with these arguments:
 * <code>-Djavax.xml.parsers.SAXParserFactory=nu.mine.mosher.xml.SaxParserFactoryStub</code>
 * <code>-Djavax.xml.transform.TransformerFactory=nu.mine.mosher.xml.TransformerFactoryStub</code>
 * <code>-Djavax.xml.parsers.DocumentBuilderFactory=nu.mine.mosher.xml.DocumentBuilderFactoryStub</code>
 */
public class VerifyJaxpStubs {
    public static void verify() {
        verify(SAXParserFactory.class, SaxParserFactoryStub.class);
        verify(DocumentBuilderFactory.class, DocumentBuilderFactoryStub.class);
        verify(TransformerFactory.class, TransformerFactoryStub.class);
    }

    private static <P,N> void verify(final Class<P> classProp, final Class<N> className) {
        final String v = System.getProperty(classProp.getName(), "");
        if (!v.equals(className.getName())) {
            throw new IllegalStateException("value of system property "+classProp.getName()+" must be "+className.getName()+", but was "+v);
        }
    }
}
