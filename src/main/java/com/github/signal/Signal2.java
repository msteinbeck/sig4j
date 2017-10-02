package com.github.signal;

import com.github.ConnectionType;
import com.github.Signal;
import com.github.Slot;
import com.github.SlotDispatcher;
import com.github.slot.Slot2;

/**
 * A signal with 2 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 */
public class Signal2<T, U> extends Signal {

    /**
     * @see Signal#connect(Slot)
     */
    public void connect(final Slot2<T, U> slot) {
        super.connect(slot);
    }

    /**
     * @see Signal#connect(Slot, ConnectionType)
     */
    public void connect(final Slot2<T, U> slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(SlotDispatcher, Slot)
     */
    public void connect(final SlotDispatcher dispatcher,
                        final Slot2<T, U> slot) {
        super.connect(dispatcher, slot);
    }

    /**
     * @see Signal#emit(Object...)
     */
    public void emit(final T t, final U u) {
        super.emit(t, u);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot2<T, U>) slot).accept((T)args[0], (U)args[1]);
    }
}
