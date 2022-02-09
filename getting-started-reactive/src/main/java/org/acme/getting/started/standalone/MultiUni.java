package org.acme.getting.started.standalone;

import io.smallrye.mutiny.Uni;

public class MultiUni {

    public static void main(String[] args) {

        invoke();

    }

    private static void invoke() {
        Uni<String> uni = Uni.createFrom()
                .<String>emitter(emitter ->
                        new Thread(() -> emitter.complete("hello from " + Thread.currentThread().getName()))
                                .start()).onItem()
                .transform(String::toUpperCase);

        uni.onItem().invoke(item -> {
            System.out.println("Received item " + item);
        }).await().indefinitely();
    }
}
