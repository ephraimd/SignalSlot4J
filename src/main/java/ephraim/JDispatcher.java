package ephraim;

import java.util.HashMap;

/**
 * {@link JDispatcher} object is basically the emitter of events and has the corresponding slots attached to it.
 * One issue with events systems is that we end up with a lot of useless slots, but in this case,
 * slots are attached to JDispatcher class instance, once the instance is no longer in used, the GC destroys it all.
 */
public abstract class JDispatcher {
    /**
     * The signalID is the String Map Key, the HashMap<Integer, Slot> contains the slot ID as key
     * which is basically an Auto increment primary key (A_I PK) and the actual slot to be triggered
     */
    private final HashMap<String, HashMap<Integer, Slot>> connections = new HashMap<>(10);

    public JDispatcher() {
    }

    /**
     * Gets a registered slot if it exists under the provided signalID. It can be used to check if signalID has been registered
     *
     * @param signalID Unique signalID string as keys for each {@link Slot}
     * @return boolean {@code true} if signalID is registered with a slot connected, return false otherwise
     */
    public boolean isSignalRegistered(final String signalID) {
        return connections.containsKey(signalID);
    }

    /**
     * Removes the signalID and all registered slots under it
     *
     * @param signalID the signalID we wish to remove. This will remove the associated slot object
     */
    public void disconnect(final String signalID) {
        if (!connections.containsKey(signalID))
            throw new IllegalArgumentException(String.format("No signal ID %s exists", signalID));
        connections.remove(signalID);
    }

    /**
     * Connects a signal(String) to an equivalent {@link Slot} using direct Mapping<key, value>
     * If signalID has already been registered for a particular slot, then the new ephraim.Slot value
     * will be added to the list of slots under the signalID
     *
     * @param signalID Unique signalID string as keys for each {@link Slot}
     * @param slot     The {@link Slot} object to be executed when {@link JDispatcher#emit(String, Object...)} is called with the corresponding signalID
     */
    public void addSlot(final String signalID, final Slot slot) {
        if (!connections.containsKey(signalID)) {
            //if signal key has not been registered before, add it
            connections.put(signalID, new HashMap<>());
        }
        HashMap<Integer, Slot> slots = connections.get(signalID);
        //Adds a new Slot with its A_I PK ID
        slots.put(slots.size() + 1, slot);
        connections.put(signalID, slots);//connections.get(signalID).size()
    }

    /**
     * Emits a signalID which will cause the corresponding slots registered under the it to be executed using the {@link Slot#exec(Object...)} method
     * Emitting a signal without any slots connected to it will cause an {@link IllegalArgumentException}.
     *
     * @param signalID Unique signalID under which the desired slot to be executed is stored
     * @param slotArgs arrays of parameters to send to the ephraim.Slot to be executed
     */
    public void emit(final String signalID, Object... slotArgs) {
        try {
            connections.get(signalID).forEach((Integer index, Slot slot) -> slot.exec(slotArgs));
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("Failed to execute any slot registered under signalID Argument: " + signalID, ex);
        }
    }
}
