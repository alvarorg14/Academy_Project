package co.empathy.academy.search.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DirectorTest {

    @Test
    void givenAttributes_whenConstructor_thenDirectorCreated() {
        Director director = new Director("nconst");
        assertEquals("nconst", director.getNconst());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenDirectorCreated() {
        Director director = new Director();
        assertNull(director.getNconst());
    }

}