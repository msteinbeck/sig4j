package so.sig4j.slot;

import so.sig4j.Slot;

import java.util.Objects;

/**
 * A slot with 34 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 * @param <W> The type of the forth argument.
 */
@FunctionalInterface
public interface Slot4<T, U, V, W> extends Slot {

    void accept(final T t, final U u, final V v, final W w);

    default Slot4<T, U, V, W> andThen(
            final Slot4<? super T, ? super U, ? super V, ? super W>
                    after) {
        Objects.requireNonNull(after);
        return (t, u, v, w) -> {
            accept(t, u, v, w);
            after.accept(t, u, v, w);
        };
    }
}
