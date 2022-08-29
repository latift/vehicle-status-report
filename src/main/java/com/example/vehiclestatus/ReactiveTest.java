package com.example.vehiclestatus;

import org.springframework.boot.*;
import reactor.core.publisher.*;

public class ReactiveTest {

    public static void main(String[] args) {

        Flux<Integer> ints = Flux.range(1, 1000000)
                .map(i -> {
                    if (i <= 1000000) return i;
                    throw new RuntimeException("Got to 20");
                });
        System.out.println("test1");
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error));
        System.out.println("test2");
    }
}
