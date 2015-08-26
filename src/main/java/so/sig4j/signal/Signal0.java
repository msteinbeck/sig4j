package so.sig4j.signal;

import so.sig4j.ConnectionType;
import so.sig4j.Signal;
import so.sig4j.SlotDispatcher;
import so.sig4j.slot.Slot0;
import so.sig4j.Slot;

/**
 * A signal with 0 arguments.
 */
public class Signal0 extends Signal {

    public void connect(final Slot0 slot) {
        super.connect(slot);
    }

    public void connect(final Slot0 slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    public void connect(final SlotDispatcher dispatcher, final Slot0 slot) {
        super.connect(dispatcher, slot);
    }

    public void emit() {
        super.emit();
    }

    @Override
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot0) slot).accept();
    }
}
