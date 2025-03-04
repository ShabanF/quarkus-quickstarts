package org.acme.getting.started.standalone;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.MultiFlatten;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiUni {

    public static void main(String[] args) {

         //invokeUni();
         //subscribe();

        //subscribeMulti();

        mapAndFlatMap();

        //collectItems();

        //flatMapAndConcatMap();

        //filtering();

        // testFunctional();

        // filterListOfMulties();

        // mergeAndConcatinate();
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
        Multi<String> multi = Multi.createFrom().items("a", "b");

        Uni<Uni<String>> uniMap1 = uni.map(s -> {

            Uni<String> s1 = Uni.createFrom().item(s.concat("hello2"));

            return s1;
        });

        Uni<String> uniMap2 = uni.map(s -> {
            return s;
        });

        Multi<Multi<String>> multiMap = multi.map(s -> {
            Multi<String> item = Multi.createFrom().item(s);
            return item;
        });

        Uni<String> uniFlatMap = uni.flatMap(s -> {

            Uni<String> s1 = Uni.createFrom().item(s.concat("hello2"));

            return s1;
        });


        Multi<String> multiFlatMap = multi.flatMap(s -> {
            Multi<String> item = Multi.createFrom().item(s);
            return item;
        });


        // only on multi
        Multi<String> multi1 = multi.concatMap(string -> {
            return Multi.createFrom().item(string);
        });

    }

    private static void collectItems() {
        Multi<String> multi = Multi.createFrom().items("a", "b");
        Uni<List<String>> uni = multi.collect().asList();

        multi.onItem().transform(s -> s).subscribe().with(s -> {
            System.out.println(s);
        });

        uni.onItem().transform(s -> s).subscribe().with(strings -> {
            System.out.println(strings.size());
        });
    }

    private static void joinUnis() {

        Uni<String> u1 = Uni.createFrom().item("a");
        Uni<String> u2 = Uni.createFrom().item("b");
        Uni<String> u3 = Uni.createFrom().item("b");

        Uni<List<String>> unis = Uni.join().all(u1, u2, u3).andCollectFailures();

    }

    private static void mergeAndConcatinate() {

        Multi<String> multi1 = Multi.createFrom().item("a");
        Multi<String> multi2 = Multi.createFrom().item("b");
        Multi<String> multi3 = Multi.createFrom().item("c");

        Multi<String> multiMerge = Multi.createBy().merging().streams(multi1, multi2, multi3);

        Multi<String> multiConcat = Multi.createBy().concatenating().streams(multi1, multi2, multi3);

        multiMerge.subscribe().with(s -> {
            System.out.println(s);
        });

        multiConcat.subscribe().with(s -> {
            System.out.println(s);
        });

    }

    private static void flatMapAndConcatMap() {

        Multi<Integer> multi = Multi.createFrom().items(1, 2, 3);

        MultiFlatten<Integer, Integer> integerIntegerMultiFlatten = multi.onItem().transformToUni(integer -> {
            return Uni.createFrom().item(integer);
        });


        Multi<Integer> integerMulti = multi.onItem().transformToMultiAndConcatenate(integer -> {
            return Multi.createFrom().item(integer);
        });

        System.out.println();
    }

    private static void filterListOfMulties() {

        Multi<String> multi1 = Multi.createFrom().items("a", "b", "c");
        Multi<String> multi2 = Multi.createFrom().items("d", "a", "f");
        Multi<String> multi3 = Multi.createFrom().items("g", "h", "f");

        List<Multi<String>> multies = Arrays.asList(multi1, multi2, multi3);

        List<Multi<String>> multies2 = new ArrayList<>();

        for (Multi<String> multi : multies) {

            Multi<String> foundMulti = multi.filter(value->value.equals("a")).collect().first().toMulti();

        }

    }

    private static void filtering() {

        Multi<String> multi = Multi.createFrom().items("a", "b", "c", "b");
        List<String> b = multi.select()
                .where(string -> string.equals("b"))
                .collect().asList().await().indefinitely();

        System.out.println(b);


    }

    private static void testFunctional() {

        TestFunction<String, String, Boolean> tf = (t,u) -> t.equals(u);

        Boolean isTrue = tf.test("abc", "fasdf");

        System.out.println(isTrue);
    }


}
