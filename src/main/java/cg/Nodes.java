package cg;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Nodes {
    ;

    public static <T> Node<T> node(Supplier<T> tSupplier) {

        return new Node<T>() {
            private Function<T, T> function = Function.identity();
            private final Supplier<T> supplier = () -> function.apply(tSupplier.get());

            @Override
            public <U> Node<T> compose(Supplier<U> uSupplier, Function<U, T> mapper, BinaryOperator<T> merger) {
                function = function.andThen(t -> merger.apply(t, mapper.apply(uSupplier.get())));
                return this;
            }

            @Override
            public <U> Node<U> transform(Function<T, U> mapper) {
                return node(() -> mapper.apply(get()));
            }

            @Override
            public T get() {
                return supplier.get();
            }
        };
    }
}
