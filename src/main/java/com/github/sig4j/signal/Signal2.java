package com.github.sig4j.signal;

import com.github.sig4j.Dispatcher;
import com.github.sig4j.Type;
import com.github.sig4j.Signal;
import com.github.sig4j.Slot;
import com.github.sig4j.slot.Slot2;

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
     * @see Signal#connect(Slot, Type)
     */
    public void connect(final Slot2<T, U> slot, final Type type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(Slot, Dispatcher)
     */
    public void connect(final Slot2<T, U> slot,
                        final Dispatcher dispatcher) {
        super.connect(slot, dispatcher);
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
