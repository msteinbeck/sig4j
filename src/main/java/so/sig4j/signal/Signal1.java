package so.sig4j.signal;

import so.sig4j.ConnectionType;
import so.sig4j.Signal;
import so.sig4j.SlotDispatcher;
import so.sig4j.Slot;
import so.sig4j.slot.Slot1;

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
     * @see Signal#connect(SlotDispatcher, Slot)
     */
    public void connect(final SlotDispatcher dispatcher, final Slot1<T> slot) {
        super.connect(dispatcher, slot);
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
