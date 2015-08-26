package so.sig4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * This class is necessary to actuate slots connected with
 * {@link Signal#connect(SlotDispatcher, Slot)}, namely dispatched slots.
 *
 * A dispatched slot is actuated in a dispatcher thread, e.g. the GUI thread,
 * the DB thread and so on. Extend this class and call {@link #waitFor()} and
 * {@link #dispatch()} periodically to actuate a slot within the thread context
 * of the dispatcher thread if an associated signal was emitted.
 */
public class SlotDispatcher {

    // Is used to block the dispatcher thread until
    // an associated signal was emitted.
    private Semaphore semaphore = new Semaphore(0);

    // The queue of slots to actuate. Needs to be thread safe.
    private Queue<Signal.SlotActuation> slots = new ConcurrentLinkedQueue<>();

    /**
     * Adds the given {@link so.sig4j.Signal.SlotActuation} to the event queue.
     * The slot itself gets actuated by the next call of {@link #dispatch()}.
     *
     * @param slotActuation The {@link so.sig4j.Signal.SlotActuation} to add.
     * @throws IllegalArgumentException If {@code slotActuation} is null.
     */
    final void actuate(final Signal.SlotActuation slotActuation) {
        if (slotActuation == null) {
            throw new IllegalArgumentException("slot actuation is null");
        }
        slots.add(slotActuation);
        semaphore.release();
    }

    /**
     * Blocks the current thread until a slot needs to be actuated. Throws an
     * {@link InterruptedException} if the threads gets interrupted while
     * waiting.
     *
     * @throws InterruptedException If the the current thread was interrupted.
     */
    protected final void waitFor() throws InterruptedException {
        semaphore.acquire();
    }

    /**
     * Polls the next {@link so.sig4j.Signal.SlotActuation} and actuates it.
     * Does nothing if there is no next {@link so.sig4j.Signal.SlotActuation}.
     */
    protected final void dispatch() {
        final Signal.SlotActuation sa = slots.poll();
        if (sa != null) {
            sa.actuate();
        }
    }
}
