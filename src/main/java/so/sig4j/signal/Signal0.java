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
     * @see Signal#connect(SlotDispatcher, Slot)
     */
    public void connect(final SlotDispatcher dispatcher, final Slot0 slot) {
        super.connect(dispatcher, slot);
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
