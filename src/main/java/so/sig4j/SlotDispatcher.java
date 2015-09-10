package so.sig4j;

import so.sig4j.signal.Signal1;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * This class is necessary to actuate slots connected with
 * {@link Signal#connect(SlotDispatcher, Slot)}, namely dispatched slots.
 *
 * A dispatched slot is actuated in a separate 'dispatcher' thread, e.g. the
 * GUI thread, the database thread and so on. The idea is to periodically call
 * {@link #waitFor()} and {@link #dispatch()} in a thread of your choice which
 * will handle the actuation of slots. Another approach is to use
 * {@link #run()} which runs in a loop until the current thread gets
 * interrupted. Use {@link #start()} and {@link #stop()} to create a Thread and
 * execute {@link #run()} in its context.
 */
public class SlotDispatcher implements Runnable {

    /**
     * Is used to block the dispatcher thread until an associated signal was
     * emitted.
     */
    private final Semaphore semaphore = new Semaphore(0);

    /**
     * The queue of slots to actuate. Needs to be thread safe without locking.
     */
    private final Queue<Signal.SlotActuation> slots =
            new ConcurrentLinkedQueue<>();

    /**
     * Is emitted if {@link #dispatch()} actuates a slot which throws a
     * {@link RuntimeException}.
     */
    private final Signal1<RuntimeException> actuationFailed = new Signal1<>();

    /**
     * The thread used in {@link #start()} and {@link #stop}
     */
    private Thread workerThread = null;

    /**
     * Adds the given {@link Signal.SlotActuation} to the event queue. The
     * slot itself gets actuated by the next call of {@link #dispatch()}.
     *
     * @param slotActuation The {@link Signal.SlotActuation} to add.
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
     * Polls the next {@link Signal.SlotActuation} from the event queue and
     * actuates it. Does nothing if the event queue is empty. This function
     * will never throw a {@link RuntimeException} but emit
     * {@link #actuationFailed}.
     */
    protected final void dispatch() {
        final Signal.SlotActuation sa = slots.poll();
        if (sa != null) {
            try {
                sa.actuate();
            } catch (final RuntimeException e) {
                actuationFailed.emit(e);
            }
        }
    }

    /**
     * Returns the signal which gets emitted if actuating a slot failed.
     *
     * @return The signal which gets emitted if actuating a slot failed.
     */
    public final Signal1<RuntimeException> getActuationFailed() {
        return actuationFailed;
    }

    /**
     * Creates a new {@link Thread} which runs {@link #run()}. Does nothing if
     * there is already a running thread.
     */
    public final synchronized void start() {
        if (workerThread == null) {
            workerThread = new Thread(this);
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    /**
     * Stops the current {@link Thread} created in {@link #start()}. Does
     * nothing if there is no running thread.
     */
    public final synchronized void stop() {
        if (workerThread != null) {
            workerThread.interrupt();
            workerThread = null;
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                waitFor();
                dispatch();
            }
        } catch (final InterruptedException e) { /**/ }
    }
}
