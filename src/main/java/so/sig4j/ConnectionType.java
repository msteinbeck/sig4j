package so.sig4j;

/**
 * The enum of supported connection types.
 *
 * To actuate a slot by a dispatcher ({@link SlotDispatcher}) use
 * {@link Signal#connect(SlotDispatcher, Slot)}.
 *
 * @see SlotDispatcher
 */
public enum ConnectionType {
    /**
     * The slot is actuated by the emitter. To be more precise: The slot is
     * actuated within the thread context of the emitter.
     */
    DIRECT,

    /**
     * The slot is queued until the worker thread used in {@link Signal} is
     * able to actuate the slot. Use this connection type if you want the
     * emitter to return immediately without any blocking.
     */
    QUEUED,
}
