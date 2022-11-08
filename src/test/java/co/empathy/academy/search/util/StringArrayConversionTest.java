package co.empathy.academy.search.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringArrayConversionTest {

    @Test
    void givenTwoString_whenToArray_thenArrayGenerated() {
        String[] array = StringArrayConversion.toArray("a,b");

        assertEquals(2, array.length);
        assertEquals("a", array[0]);
        assertEquals("b", array[1]);
    }

    @Test
    void givenOneString_whenToArray_thenArrayGenerated() {
        String[] array = StringArrayConversion.toArray("a");

        assertEquals(1, array.length);
        assertEquals("a", array[0]);
    }

    @Test
    void givenEmptyString_whenToArray_thenEmptyArrayGenerated() {
        String[] array = StringArrayConversion.toArray("");

        assertEquals(0, array.length);
    }

    @Test
    void givenNewLine_whenToArray_thenEmptyArrayGenerated() {
        String[] array = StringArrayConversion.toArray("\\N");

        assertEquals(0, array.length);
    }

}