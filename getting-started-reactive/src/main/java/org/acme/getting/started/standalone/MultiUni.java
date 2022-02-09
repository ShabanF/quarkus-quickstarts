package org.acme.getting.started.standalone;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.ArrayList;
import java.util.List;

public class MultiUni {

    public static void main(String[] args) {

        //invokeUni();
        //subscribe();

        //subscribeMulti();

        mapAndFlatMap();
    }

    private static void invokeUni() {
        Uni<String> uni = Uni.createFrom()
                .<String>emitter(emitter ->
                        new Thread(() -> emitter.complete("hello from " + Thread.currentThread().getName()))
                                .start()).onItem()
                .transform(String::toUpperCase);

        uni.onItem().invoke(item -> {
            System.out.println("Received item " + item);
        }).await().indefinitely();
    }

    private static void subscribe() {
        Uni<String> helloUni = Uni.createFrom()
                .item("hello")
                .onItem().transform(item -> item + " mutiny")
                .onItem().transform(String::toUpperCase);

        helloUni.onSubscription().invoke(() -> System.out.println()).subscribe().with(item -> System.out.println(item));

    }

    private static void subscribeMulti() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        Multi<List<String>> multi = Multi.createFrom().item(list);

        multi.onSubscription().invoke(items -> {
                    items.request(0);
                })
                .onCompletion().invoke(() -> {
                    System.out.println("On completion invoked");
                }).subscribe().with(strings -> {
                    System.out.println(strings);
                });
    }

    private static void mapAndFlatMap() {

        Uni<String> uni = Uni.createFrom().item("hello");

        Uni<String> hello2 = uni.flatMap(s -> {

            Uni<String> s1 = Uni.createFrom().item(s.concat("hello2"));

            return s1;
        });

        hello2.subscribe().with(System.out::println);

    }
}
