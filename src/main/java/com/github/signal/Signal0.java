package com.github.signal;

import com.github.ConnectionType;
import com.github.Signal;
import com.github.Slot;
import com.github.SlotDispatcher;
import com.github.slot.Slot0;

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
     * @see Signal#connect(Slot, ConnectionType)
     */
    public void connect(final Slot0 slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(Slot, SlotDispatcher)
     */
    public void connect(final Slot0 slot, final SlotDispatcher dispatcher) {
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
