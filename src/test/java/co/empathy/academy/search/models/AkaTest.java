package co.empathy.academy.search.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AkaTest {

    @Test
    void givenAttributes_whenConstructor_thenAkaCreated() {
        Aka aka = new Aka("title", "region", "language", true);
        assertEquals("title", aka.getTitle());
        assertEquals("region", aka.getRegion());
        assertEquals("language", aka.getLanguage());
        assertTrue(aka.getIsOriginalTitle());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenAkaCreated() {
        Aka aka = new Aka();
        assertNull(aka.getTitle());
        assertNull(aka.getRegion());
        assertNull(aka.getLanguage());
        assertNull(aka.getIsOriginalTitle());
    }
}