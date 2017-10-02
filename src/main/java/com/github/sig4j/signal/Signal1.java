package com.github.sig4j.signal;

import com.github.sig4j.ConnectionType;
import com.github.sig4j.Signal;
import com.github.sig4j.Slot;
import com.github.sig4j.SlotDispatcher;
import com.github.sig4j.slot.Slot1;

/**
 * A signal with 1 generic argument.
 *
 * @param <T> The Type of the argument.
 */
public class Signal1<T> extends Signal {

    /**
     * @see Signal#connect(Slot)
     */
    public void connect(final Slot1<T> slot) {
        super.connect(slot);
    }

    /**
     * @see Signal#connect(Slot, ConnectionType)
     */
    public void connect(final Slot1<T> slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(Slot, SlotDispatcher)
     */
    public void connect(final Slot1<T> slot, final SlotDispatcher dispatcher) {
        super.connect(slot, dispatcher);
    }

    /**
     * @see Signal#emit(Object...)
     */
    public void emit(final T t) {
        super.emit(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot1<T>) slot).accept((T)args[0]);
    }
}
