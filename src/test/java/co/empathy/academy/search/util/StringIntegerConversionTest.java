package co.empathy.academy.search.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringIntegerConversionTest {

    @Test
    void givenACorrectNumber_whenToInt_thenReturnsTheNumber() {
        assertEquals(1, StringIntegerConversion.toInt("1"));
    }

    @Test
    void givenANumberWithSpaces_whenToInt_thenReturnsTheNumber() {
        assertEquals(1, StringIntegerConversion.toInt(" 1 "));
    }

    @Test
    void givenANewLineCharacter_whenToInt_thenReturnsZero() {
        assertEquals(0, StringIntegerConversion.toInt("\\N"));
    }

    @Test
    void givenText_whenToInt_thenReturnsZero() {
        assertEquals(0, StringIntegerConversion.toInt("text"));
    }

}