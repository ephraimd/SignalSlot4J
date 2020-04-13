import java.util.HashMap;

/**
 * A wrapper around a dispatcher object to make it accessible globally.
 * We'll basically be using a global dispatcher to allow very loose event connection accross the packages
 */
public class JSSDispatcher {
    private static Dispatcher dispatcher = new Dispatcher() {};
    private HashMap<String, Integer> subSignalMeta = new HashMap<>(20);

    /**
     * Exposes a persistent singleton dispatcher that can be used in a global contexts
     * @return Dispatcher a persistent dispatcher object
     */
    public static Dispatcher getDispatcher(){
        return dispatcher;
    }

}
