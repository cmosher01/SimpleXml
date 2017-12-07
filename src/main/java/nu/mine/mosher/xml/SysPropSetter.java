package nu.mine.mosher.xml;

/**
 * Allows setting a System property, and easily resetting it to its original value.
 */
public class SysPropSetter {
    private final String prop;
    private final String origValueOrNull;

    public SysPropSetter(final String prop) {
        this.prop = prop;
        this.origValueOrNull = System.getProperty(this.prop);
    }

    public SysPropSetter set(final String valueOrNull) {
        if (valueOrNull == null) {
            System.clearProperty(this.prop);
        } else {
            System.setProperty(this.prop, valueOrNull);
        }
        return this;
    }

    public SysPropSetter clear() {
        return set(null);
    }

    public void reset() {
        set(this.origValueOrNull);
    }
}
