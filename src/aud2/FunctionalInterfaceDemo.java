package aud2;

import java.util.function.*;

public class FunctionalInterfaceDemo {
    static void main() {
        Function<String, Integer> stringLength = str -> str.length();
        System.out.println("Length of 'Hello': " + stringLength.apply("Hello"));

        BiFunction<Integer,Integer,Integer> sum = (a,b) -> a + b;
        System.out.println("Sum of 5 and 3: " + sum.apply(5, 3));

        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("Is 4 even?" + isEven.test(4));
        System.out.println("Is 5 even?" + isEven.test(5));

        Consumer<String> printString = str -> System.out.println("Printing: " + str);
        printString.accept("Hello, World");

        Supplier<Long> currentTimeMillis = () -> System.currentTimeMillis();
        System.out.println("Current time in milliseconds: " + currentTimeMillis.get());
    }
}
