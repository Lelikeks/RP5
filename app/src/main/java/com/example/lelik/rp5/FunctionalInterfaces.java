package com.example.lelik.rp5;

/**
 * Created by lelik on 06.06.2017.
 */

interface Predicate<T> {
    boolean test(T value);
}

interface Function<T, R> {
    R apply(T value);
}
