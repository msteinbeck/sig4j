package com.github.msteinbeck.sig4j.signal;

import com.github.msteinbeck.sig4j.Dispatcher;
import com.github.msteinbeck.sig4j.Signal;
import com.github.msteinbeck.sig4j.Slot;
import com.github.msteinbeck.sig4j.slot.Slot3;
import com.github.msteinbeck.sig4j.Type;

/**
 * A signal with 3 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 */
public class Signal3<T, U, V> extends Signal {

	/**
	 * @see Signal#connect(Slot)
	 */
	public void connect(final Slot3<T, U, V> slot) {
		super.connect(slot);
	}

	/**
	 * @see Signal#connect(Slot, Type)
	 */
	public void connect(final Slot3<T, U, V> slot, final Type type) {
		super.connect(slot, type);
	}

	/**
	 * @see Signal#connect(Slot, Dispatcher)
	 */
	public void connect(final Slot3<T, U, V> slot,
						final Dispatcher dispatcher) {
		super.connect(slot, dispatcher);
	}

	/**
	 * @see Signal#emit(Object...)
	 */
	public void emit(final T t, final U u, final V v) {
		super.emit(t, u, v);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void actuate(final Slot slot, final Object[] args) {
		((Slot3<T, U, V>) slot).accept((T)args[0], (U)args[1], (V)args[2]);
	}
}
