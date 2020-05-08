package ephraim;


/**
 * A wrapper around a dispatcher object to make it accessible globally.
 * We'll basically be using a global dispatcher to allow very loose event connection across the packages
 */
public class StaticJDispatcher extends JDispatcher {
    /**
     * Global {@link JDispatcher} object that can have slots attached to it across the entire app
     * Signals can be emitted also from any part of the app
     */
    private static final JDispatcher dispatcher = new JDispatcher() {
    };

    /**
     * Exposes a persistent singleton dispatcher that can be used in a global contexts
     *
     * @return JDispatcher a persistent dispatcher object
     */
    public static JDispatcher getDispatcher() {
        return dispatcher;
    }

}
