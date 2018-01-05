package raven.rpc.httpprototocol.function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {

    R apply(T t) throws Exception;
}
