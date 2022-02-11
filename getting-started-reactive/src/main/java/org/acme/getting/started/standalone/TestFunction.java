package org.acme.getting.started.standalone;

@FunctionalInterface
public interface TestFunction<T, U, R> {

    R test(T t, U u);
}
