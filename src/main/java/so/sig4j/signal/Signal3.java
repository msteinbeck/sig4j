package so.sig4j.signal;

import so.sig4j.ConnectionType;
import so.sig4j.Signal;
import so.sig4j.SlotDispatcher;
import so.sig4j.Slot;
import so.sig4j.slot.Slot3;

/**
 * A signal with 3 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 */
public class Signal3<T, U, V> extends Signal {

    public void connect(final Slot3<T, U, V> slot) {
        super.connect(slot);
    }

    public void connect(final Slot3<T, U, V> slot, final ConnectionType type) {
        super.connect(slot, type);
    }

    public void connect(final SlotDispatcher dispatcher,
                        final Slot3<T, U, V> slot) {
        super.connect(dispatcher, slot);
    }

    public void emit(final T t, final U u, final V v) {
        super.emit(t, u, v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot3<T, U, V>) slot).accept((T)args[0], (U)args[1], (V)args[2]);
    }
}
