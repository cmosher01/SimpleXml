package nu.mine.mosher.xml;

import java.io.Closeable;
import java.util.Optional;



/**
 * Allows setting a System property, and easily resetting it to its original value.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SysPropResetter implements Closeable {
    public static SysPropResetter set(final String key, final Optional<String> value) {
        // first capture and save the original value of the property
        final SysPropResetter instance = new SysPropResetter(key);
        setOrClearSysProp(key, value);
        return instance;
    }



    @Override
    public void close() {
        reset();
    }

    public void reset() {
        setOrClearSysProp(this.key, this.originalValue);
    }



    private final String key;
    private final Optional<String> originalValue;



    private SysPropResetter(final String key) {
        this.key = key;
        this.originalValue = Optional.ofNullable(System.getProperty(this.key));
    }



    private static void setOrClearSysProp(final String key, final Optional<String> value) {
        if (value.isPresent()) {
            System.setProperty(key, value.get());
        } else {
            System.clearProperty(key);
        }
    }
}
