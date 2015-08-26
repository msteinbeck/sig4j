package so.sig4j.slot;

import so.sig4j.Slot;

import java.util.Objects;

/**
 * A slot with 1 generic argument.
 *
 * @param <T> The type of the argument.
 */
@FunctionalInterface
public interface Slot1<T> extends Slot {

    void accept(final T t);

    default Slot1<T> andThen(final Slot1<? super T> after) {
        Objects.requireNonNull(after);
        return (t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
