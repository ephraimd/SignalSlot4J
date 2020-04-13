/**
 * Single slot object that gets executed on signal emission
 */
public interface Slot {
    public void exec(Object ... args);
}
