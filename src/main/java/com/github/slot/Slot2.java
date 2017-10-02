package com.github.slot;

import com.github.Slot;

import java.util.Objects;

/**
 * A slot with 2 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 */
@FunctionalInterface
public interface Slot2<T, U> extends Slot {

    void accept(final T t, final U u);

    default Slot2<T, U> andThen(final Slot2<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (t, u) -> {
            accept(t, u);
            after.accept(t, u);
        };
    }
}
