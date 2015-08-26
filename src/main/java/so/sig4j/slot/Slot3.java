package so.sig4j.slot;

import so.sig4j.Slot;

import java.util.Objects;

/**
 * A slot with 3 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 */
@FunctionalInterface
public interface Slot3<T, U, V> extends Slot {

    void accept(final T t, final U u, final V v);

    default Slot3<T, U, V> andThen(
            final Slot3<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
