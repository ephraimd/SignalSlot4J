import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatcher object is basically the emiter of events and has the corresponding slots attached to it
 */
public abstract class Dispatcher {
    private final HashMap<String, HashMap<Integer, Slot>> connections = new HashMap<>(10);
    public Dispatcher(){}

    /**
     * Gets a registered slot if it exists under the provided signalID. It can be used to check is signalID has been registered
     * @param signalID Unique signalID string as keys for each Slot
     * @return Slot object registered under the signalID, if not located, returns null
     */
    protected boolean isSignalRegistered(String signalID){
        return connections.getOrDefault(signalID, null) != null;
    }

    /**
     * Connects a signal(String) to an equivalent Slot using direct Mapping<key, value>
     *     If signalID has already been registered for a particular slot, then the new Slot value
     *     will be added to the list of slots under the signalID
     * @param signalID Unique signalID string as keys for each Slot
     * @param slot The Slot object to be executed when Dispatcher#emit is called with the corresponding signalID
     */
    protected void addSlot(String signalID, Slot slot){
        if(connections.get(signalID) == null){
            connections.put(signalID, new HashMap<>());
        }
        HashMap<Integer, Slot> slots = connections.get(signalID);
        slots.put(slots.size()+1, slot);
        connections.put(signalID, slots);//connections.get(signalID).size()
    }

    /**
     * Emits a signalID which will cause the corresponding slots registered under the it to be executed using the SLot#exec method
     * @param signalID Unique signalID under which the desired slot to be executed is stored
     * @param slotArgs arrays of parameters to send to the Slot to be executed
     */
    protected void emit(String signalID, Object ... slotArgs){
        try{
            connections.get(signalID).forEach((Integer index, Slot slot) -> slot.exec(slotArgs));
        }catch(NullPointerException ex){
            throw new IllegalArgumentException("No Slot is registered under signalID Argument: "+signalID);
        }
    }

    /**
     * Removes the signalID and all registered slots under it
     * @param signalID the signalID we wish to remove. This will remove the aasociated slot object
     */
    protected void disconnect(String signalID){
        connections.remove(signalID);
    }
}
