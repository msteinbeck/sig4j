package com.github.slot;

import com.github.Slot;

import java.util.Objects;

/**
 * A slot with 0 arguments.
 */
@FunctionalInterface
public interface Slot0 extends Slot {

    void accept();

    default Slot0 andThen(final Slot0 after) {
        Objects.requireNonNull(after);
        return () -> {
            accept();
            after.accept();
        };
    }
}
