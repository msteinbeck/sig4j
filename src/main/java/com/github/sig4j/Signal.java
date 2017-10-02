package com.github.sig4j;

import com.github.sig4j.dispatcher.JavaFXDispatcher;
import com.github.sig4j.dispatcher.SwingDispatcher;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.sig4j.ConnectionType.DIRECT;
import static com.github.sig4j.ConnectionType.JAVAFX;
import static com.github.sig4j.ConnectionType.SWING;

/**
 * The base class of all signals.
 *
 * Note:
 *  Connecting and emitting slots concurrently is thread safe without blocking.
 */
public abstract class Signal {
    /**
     * The {@link SlotDispatcher} of {@link ConnectionType#QUEUED} connected
     * slots.
     */
    private static final SlotDispatcher DISPATCHER = new SlotDispatcher();

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
     * The queue of {@link ConnectionType#DIRECT} connected slots.
     */
    private Queue<Slot> direct = new ConcurrentLinkedDeque<>();

    /**
     * The queue of {@link ConnectionType#QUEUED} connected slots.
     */
    private Queue<Slot> queued = new ConcurrentLinkedDeque<>();

    /**
     * The queue of dispatched slots {@see SlotDispatcher}.
     */
    private Queue<Entry<Slot, SlotDispatcher>>
            dispatched = new ConcurrentLinkedDeque<>();

    /**
     * Enables the signal.
     * @see #disable()
     */
    @SuppressWarnings("WeakerAccess")
    public void enable() {
        enabled.set(true);
    }

    /**
     * Disables the signal. A disabled signal will not actuate its connected
     * slots.
     * @see #enable()
     */
    @SuppressWarnings("WeakerAccess")
    public void disable() {
        enabled.set(false);
    }

    /**
     * Removes all connected slots. Clearing a signal is not an atomic
     * operation and may result in a non-empty slot queue if one of the
     * 'connect' methods is used concurrently.
     */
    @SuppressWarnings("WeakerAccess")
    public void clear() {
        direct.clear();
        queued.clear();
        dispatched.clear();
    }

    /**
     * Connects the given slot using {@link ConnectionType#DIRECT}. This
     * function is equivalent to {@code connect(slot, ConnectionType.DIRECT)}.
     *
     * @param slot The slot to connect.
     * @throws IllegalArgumentException If {@code slot} is null.
     */
    protected void connect(final Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        }
        direct.add(slot);
    }

    /**
     * Connects the given slot according to {@link ConnectionType}.
     *
     * @param slot The slot to connect.
     * @param type The {@link ConnectionType} to use.
     * @throws IllegalArgumentException If {@code slot} or {@code type} is null.
     */
    protected void connect(final Slot slot, final ConnectionType type) {
        if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        } else if (type == null) {
            throw new IllegalArgumentException("connection type is null");
        } else if (type == DIRECT) {
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
     * Connects the given slot and actuates it within the thread context
     * of the given {@link SlotDispatcher} if the signal is emitted.
     *
     * @param slot       The slot to connect.
     * @param dispatcher The {@link SlotDispatcher} to use.
     * @throws IllegalArgumentException If {@code dispatcher} or
     *                      {@code slot} is null.
     */
    protected void connect(final Slot slot, final SlotDispatcher dispatcher) {
        if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        } else if (dispatcher == null) {
            throw new IllegalArgumentException("dispatcher is null");
        }
        dispatched.add(new SimpleEntry<>(slot, dispatcher));
    }

    /**
     * Emits this signal with the given arguments.
     *
     * @param args The arguments to use while emitting this signal.
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
     * A callback function used to actuate a single slot.
     *
     * The implementer of this function does not need to create any threads
     * but cast down the given slot and actuate it with the given arguments.
     *
     * This function should not have any side effects to this class.
     *
     * @param slot The slot to actuate.
     * @param args The arguments of the actuated slot.
     */
    protected abstract void actuate(final Slot slot, final Object[] args);



    /**
     * Represents a slot actuation. Use {@link #actuate()} to actuate the slot
     * with its arguments.
     */
    class SlotActuation {
        private final Slot slot;
        private final Object[] arguments;

        private SlotActuation(final Slot s, final Object[] args) {
            slot = s;
            arguments = args;
        }

        void actuate() {
            Signal.this.actuate(slot, arguments);
        }
    }
}