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
     * The slot is actuated within the thread context of the emitter
     * (immediately).
     */
    DIRECT,

    /**
     * The slot is actuated by a worker thread used in {@link Signal}.
     */
    QUEUED,

    /**
     * Same as {@link #QUEUED}, except that the current thread blocks until
     * the slot has been actuated.
     */
    BLOCKING_QUEUED
}
