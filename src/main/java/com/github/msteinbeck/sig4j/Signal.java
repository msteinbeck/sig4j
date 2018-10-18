package com.github.msteinbeck.sig4j;

import com.github.msteinbeck.sig4j.dispatcher.JavaFXDispatcher;
import com.github.msteinbeck.sig4j.dispatcher.SwingDispatcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.msteinbeck.sig4j.Type.DIRECT;
import static com.github.msteinbeck.sig4j.Type.JAVAFX;
import static com.github.msteinbeck.sig4j.Type.SWING;

/**
 * The base class of all signals. Note: Connecting and emitting slots
 * concurrently is thread-safe without blocking.
 */
public abstract class Signal {
	/**
	 * The {@link Dispatcher} of {@link Type#QUEUED} connected slots.
	 */
	private static final Dispatcher DISPATCHER = new Dispatcher();

	static {
		DISPATCHER.start();
	}

	/**
	 * Indicates whether a signal is enabled/disabled.
	 * @see #enable()
	 * @see #disable()
	 */
	private final AtomicBoolean enabled = new AtomicBoolean(true);

	/**
	 * The queue of {@link Type#DIRECT} connected slots.
	 */
	private Queue<Slot> direct = new ConcurrentLinkedDeque<>();

	/**
	 * The queue of {@link Type#QUEUED} connected slots.
	 */
	private Queue<Slot> queued = new ConcurrentLinkedDeque<>();

	/**
	 * The queue of dispatched slots {@see Dispatcher}.
	 */
	private Queue<Entry<Slot, Dispatcher>>
			dispatched = new ConcurrentLinkedDeque<>();

	/**
	 * Enables this signal.
	 * @see #disable()
	 */
	public void enable() {
		enabled.set(true);
	}

	/**
	 * Disables this signal. A disabled signal will not actuate its connected
	 * slots.
	 * @see #enable()
	 */
	public void disable() {
		enabled.set(false);
	}

	/**
	 * Removes all connected slots. Clearing a signal is not an atomic
	 * operation and may result in a non-empty slot queue if one of the
	 * 'connect' methods is used concurrently.
	 */
	public void clear() {
		direct.clear();
		queued.clear();
		dispatched.clear();
	}

	/**
	 * Connects the given slot using {@link Type#DIRECT}. This method is
	 * equivalent to {@code connect(slot, Type.DIRECT)}.
	 *
	 * @param slot The slot to connect.
	 * @throws NullPointerException If {@code slot} is {@code null}.
	 */
	protected void connect(final Slot slot) {
		Objects.requireNonNull(slot);
		direct.add(slot);
	}

	/**
	 * Connects the given slot according to {@link Type}.
	 *
	 * @param slot The slot to connect.
	 * @param type The connection type.
	 * @throws NullPointerException If {@code slot} or {@code type} is
	 * {@code null}.
	 */
	protected void connect(final Slot slot, final Type type) {
		Objects.requireNonNull(slot);
		Objects.requireNonNull(type);
		if (type == DIRECT) {
			direct.add(slot);
		} else if (type == JAVAFX) {
			connect(slot, JavaFXDispatcher.getInstance());
		} else if (type == SWING) {
			connect(slot, SwingDispatcher.getInstance());
		} else {
			queued.add(slot);
		}
	}

	/**
	 * Connects the given slot and actuates it within the thread context of the
	 * given {@link Dispatcher} if the signal is emitted.
	 *
	 * @param slot The slot to connect.
	 * @param dispatcher The {@link Dispatcher} to use.
	 * @throws NullPointerException If {@code slot} or {@code dispatcher} is
	 * {@code null}.
	 */
	protected void connect(final Slot slot, final Dispatcher dispatcher) {
		Objects.requireNonNull(slot);
		Objects.requireNonNull(dispatcher);
		dispatched.add(new SimpleEntry<>(slot, dispatcher));
	}

	/**
	 * Emits this signal with the given arguments.
	 *
	 * @param args The arguments to use pass to the connected slots.
	 */
	protected void emit(final Object... args) {
		if (enabled.get()) {
			dispatched.forEach(da -> {
				final SlotActuation sa = new SlotActuation(da.getKey(), args);
				da.getValue().actuate(sa);
			});
			queued.forEach(s -> {
				final SlotActuation sa = new SlotActuation(s, args);
				DISPATCHER.actuate(sa);
			});
			direct.forEach(s -> actuate(s, args));
		}
	}

	/**
	 * A callback method used for slot actuation.
	 *
	 * The implementer of this method does not need to create any threads, but
	 * cast down the given slot and actuate it with the given arguments.
	 *
	 * This method should not have any side effects to this class.
	 *
	 * @param slot The slot to actuate.
	 * @param args The arguments of the actuated slot.
	 */
	protected abstract void actuate(final Slot slot, final Object[] args);

	/**
	 * Stores a slot and its arguments and allows to actuate this slot with
	 * {@link #actuate()}.
	 */
	class SlotActuation {

		/**
		 * The slot to actuate.
		 */
		private final Slot slot;

		/**
		 * The arguments to pass to {@link #slot}.
		 */
		private final Object[] arguments;

		/**
		 * Creates a new instance with given slot and arguments.
		 *
		 * @param s The slot to store.
		 * @param args The arguments to store.
		 * @throws NullPointerException If {@code s} or {@code args} is
		 * {@code null} ({@code args} may contain {@code null} values though).
		 */
		private SlotActuation(final Slot s, final Object[] args) {
			slot = Objects.requireNonNull(s);
			arguments = Objects.requireNonNull(args);
		}

		/**
		 * Actuates {@link #slot} with its arguments {@link #arguments}.
		 */
		void actuate() {
			Signal.this.actuate(slot, arguments);
		}
	}
}
