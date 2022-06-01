package cg;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Node<T> extends Supplier<T> {

    <U> Node<T> compose(Supplier<U> uSupplier, Function<U, T> mapper, BinaryOperator<T> merger);

    <U> Node<U> transform(Function<T, U> mapper);

    default Node<T> compose(Supplier<T> uSupplier, BinaryOperator<T> merger){
        return compose(uSupplier, Function.identity(), merger);
    }

    @Override
    T get();
}
