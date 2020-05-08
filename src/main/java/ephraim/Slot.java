package ephraim;

/**
 * Single slot object that gets executed on signal emission
 */
public interface Slot {
    void exec(Object... args);
}
