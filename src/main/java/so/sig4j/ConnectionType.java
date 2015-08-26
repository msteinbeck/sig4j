package so.sig4j;

/**
 * The enum of supported connection types.
 *
 * To actuate a slot by a dispatcher ({@link SlotDispatcher}) use
 * {@link Signal#connect(SlotDispatcher, Slot)}.
 *
 */
public enum ConnectionType {
    /**
     * The slot is actuated within the thread context of the emitter
     * (immediately).
     */
    DIRECT,

    /**
     * The slot is actuated by a thread of the thread pool.
     */
    QUEUED,

    /**
     * The slot is actuated by the dispatcher loop.
     */
    //DISPATCHED
}
