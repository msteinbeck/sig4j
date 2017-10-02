package com.github.sig4j.signal;

import com.github.sig4j.Type;
import com.github.sig4j.Signal;
import com.github.sig4j.Slot;
import com.github.sig4j.Dispatcher;
import com.github.sig4j.slot.Slot0;

/**
 * A signal with 0 arguments.
 */
public class Signal0 extends Signal {

    /**
     * @see Signal#connect(Slot)
     */
    public void connect(final Slot0 slot) {
        super.connect(slot);
    }

    /**
     * @see Signal#connect(Slot, Type)
     */
    public void connect(final Slot0 slot, final Type type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(Slot, Dispatcher)
     */
    public void connect(final Slot0 slot, final Dispatcher dispatcher) {
        super.connect(slot, dispatcher);
    }

    /**
     * @see Signal#emit(Object...)
     */
    public void emit() {
        super.emit();
    }

    @Override
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot0) slot).accept();
    }
}
