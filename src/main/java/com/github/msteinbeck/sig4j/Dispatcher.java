package com.github.msteinbeck.sig4j;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.Signal.SlotActuation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * This class allows to actuate dispatched slots.
 *
 * A dispatched slot is actuated in a separate 'dispatcher' thread, e.g., the
 * GUI thread, the database thread, and so on. One way of doing this is to
 * periodically call {@link #waitFor()} and {@link #dispatch()} in a thread of
 * your choice to handle the actuation of slots.
 *
 * You may also use {@link #run()} which calls {@link #waitFor()} and
 * {@link #switchContext()} in a loop until the current thread gets
 * interrupted. {@link #switchContext()}, in turn, may be subclassed to
 * delegate {@link #dispatch()} to another dispatcher thread, for example, the
 * JavaFX thread. By default, {@link #switchContext()} calls
 * {@link #dispatch()} in the same context as {@link #run()} which is a useful
 * default in most cases.
 *
 * Finally, {@link #start()} and {@link #stop()} are convenience methods to
 * start (and stop) an arbitrary thread which executes {@link #run()} in its
 * context.
 *
 * For examples, see {@link com.github.msteinbeck.sig4j.dispatcher}.
 */
public class Dispatcher implements Runnable {

	/**
	 * Is used to block the dispatcher thread until an associated signal has
	 * been emitted.
	 */
	private final Semaphore semaphore = new Semaphore(0);

	/**
	 * The queue of slots to actuate. Needs to be thread safe without locking.
	 */
	private final Queue<SlotActuation> slots = new ConcurrentLinkedQueue<>();

	/**
	 * Is emitted by {@link #dispatch()} if a {@link RuntimeException} has been
	 * thrown by either {@link #beforeActuation()}, {@link #afterActuation()},
	 * or a slot actuation itself.
	 */
	private final Signal1<RuntimeException> onError = new Signal1<>();

	/**
	 * The thread used in {@link #start()} and {@link #stop}.
	 */
	private Thread workerThread = null;

	/**
	 * Adds the given {@link SlotActuation} to the event queue. The slot itself
	 * gets actuated by the next call of {@link #dispatch()}.
	 *
	 * @param slotActuation The {@link SlotActuation} to add.
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
	@SuppressWarnings("WeakerAccess")
	protected final void waitFor() throws InterruptedException {
		semaphore.acquire();
	}

	/**
	 * Polls the next {@link SlotActuation} from the event queue and actuates
	 * it. Does nothing if the event queue is empty. This function will never
	 * throw a {@link RuntimeException} but emit {@link #onError()}.
	 */
	protected final void dispatch() {
		try {
			final Signal.SlotActuation sa = slots.poll();
			if (sa != null) {
				beforeActuation();
				sa.actuate();
				afterActuation();
			}
		} catch (final RuntimeException e) {
			onError.emit(e);
		}
	}

	/**
	 * Allows subclasses to switch the thread context before actuating a slot
	 * by calling {@link #dispatch()} within the desired context. The default
	 * implementation calls {@link #dispatch()} within the caller context.
	 */
	protected void switchContext() {
		dispatch();
	}

	/**
	 * This is a callback which gets executed by {@link #dispatch()} right
	 * before a slot is actuated. Override it to add some custom code. If a
	 * {@link RuntimeException} is thrown by this callback, {@link #dispatch()}
	 * will catch it and emit the signal returned by {@link #onError()}. If
	 * there is no slot to actuate {@link #dispatch()} omits this callback.
	 */
	protected void beforeActuation() {}

	/**
	 * This is a callback which gets executed by {@link #dispatch()} right
	 * after a slot has been actuated. Override it to add some custom code. If
	 * a {@link RuntimeException} is thrown by this callback,
	 * {@link #dispatch()} will catch it and emit the signal returned by
	 * {@link #onError()}. If there is no slot to actuate {@link #dispatch()}
	 * omits this callback.
	 */
	protected void afterActuation() {}

	/**
	 * Returns the signal which gets emitted if actuating a slot failed.
	 *
	 * @return The signal which gets emitted if actuating a slot failed.
	 */
	public final Signal1<RuntimeException> onError() {
		return onError;
	}

	/**
	 * Creates a new {@link Thread} which runs {@link #run()}. Does nothing if
	 * there already is a running thread.
	 */
	public final synchronized void start() {
		if (workerThread == null) {
			workerThread = new Thread(this);
			workerThread.setDaemon(true);
			workerThread.start();
		}
	}

	/**
	 * Stops the current {@link Thread} created by {@link #start()}. Does
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
				switchContext();
			}
		} catch (final InterruptedException e) { /**/ }
	}
}
