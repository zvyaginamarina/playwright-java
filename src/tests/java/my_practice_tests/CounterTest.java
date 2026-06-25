package tests.java.my_practice_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

//запускает каждый тест с первоначальным значением переменной
@TestInstance(TestInstance.Lifecycle.PER_METHOD)

// запускает каждый тест со значением переменной из предыдущего теста
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CounterTest {

    public int counter = 0;

    public int count() {
        counter = counter + 1;
        return counter;
    }

    @Test
    void counter1() {
        System.out.println(count());
    }

    @Test
    void counter2() {
        System.out.println(count());
    }

    @Test
    void counter3() {
        System.out.println(count());
    }
}
