package tests.java.my_practice_tests;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import net.datafaker.Faker;

public class JUnitParamTest {
    boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        } else if (password.length() >= 8) {
            return true;
        } else {
            return false;
        }
    };

    int sum(int a, int b) {
        return a + b;
    }

    private static Stream<Arguments> petCredentials() {
        Faker faker = new Faker();
        int petId1 = faker.number().numberBetween(1, 20);
        int petId2 = faker.number().numberBetween(21, 40);
        int petId3 = faker.number().numberBetween(41, 60);
        int petId4 = faker.number().numberBetween(61, 100);

        String petName1 = faker.name().firstName();
        String petName2 = faker.name().firstName();
        String petName3 = faker.name().firstName();
        String petName4 = faker.name().firstName();

        String petStatus = "available";

        return Stream.of(
                Arguments.of(new Pet(petId1, petName1, petStatus)),
                Arguments.of(new Pet(petId2, petName2, petStatus)),
                Arguments.of(new Pet(petId3, petName3, petStatus)),
                Arguments.of(new Pet(petId4, petName4, petStatus)));

    };

    @ParameterizedTest
    @ValueSource(strings = { "runForestRun", "abcdefghi", "password" })
    void validPasswordLength(String password) {
        System.out.println(password.length());
        assertTrue(isValidPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = { "run", "", " ", "1234567" })
    void invalidPasswordLength(String password) {
        System.out.println(password.length());
        assertFalse(isValidPassword(password));
    }

    @ParameterizedTest
    @CsvSource({ "2, 3, 5", "100, 100, 200", "10, 5, 15" })
    void sumValidation(int a, int b, int expected) {
        int actual = sum(a, b);
        System.out.println("Expected sum: " + expected + ". Actual sum: " + actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("petCredentials")
    void petCreationValidation(Pet pet) {
        System.out.println(pet.getId() + " " + pet.getName() + " " + pet.getStatus());
        assertTrue(pet.getId() > 0);
        assertNotNull(pet.getName());
        assertEquals("available", pet.getStatus());

    }

    @ParameterizedTest
    @NullSource
    void nullPasswordInput(String password) {
        System.out.println(password);
        assertFalse(isValidPassword(password));
    }

    @ParameterizedTest
    @EmptySource
    void emptyPasswordInput(String password) {
        System.out.println(password);
        assertFalse(isValidPassword(password));
    }

    @RepeatedTest(5)
    void correctPassword() {
        assertTrue(isValidPassword("12345678"));
    }

}
