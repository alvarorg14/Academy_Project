package co.empathy.academy.search.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringDoubleConversionTest {

    @Test
    void givenCorrectDouble_whenToDouble_thenDoubleGenerated() {
        double value = StringDoubleConversion.toDouble("1.0");
        assertEquals(1.0, value);
    }

    @Test
    void givenText_whenToDouble_thenZeroGenerated() {
        double value = StringDoubleConversion.toDouble("a");
        assertEquals(0.0, value);
    }

    @Test
    void givenNewLine_whenToDouble_thenZeroGenerated() {
        double value = StringDoubleConversion.toDouble("\\N");
        assertEquals(0.0, value);
    }

    @Test
    void givenEmptyString_whenToDouble_thenZeroGenerated() {
        double value = StringDoubleConversion.toDouble("");
        assertEquals(0.0, value);
    }

    @Test
    void givenDoubleWithSpaces_whenToDouble_thenDoubleGenerated() {
        double value = StringDoubleConversion.toDouble(" 1.0 ");
        assertEquals(1.0, value);
    }

}