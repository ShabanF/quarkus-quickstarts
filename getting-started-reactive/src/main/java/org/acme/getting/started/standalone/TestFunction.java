package org.acme.getting.started.standalone;

@FunctionalInterface
public interface TestFunction<T, R> {

    R apply(T t);
}
