package co.empathy.academy.search.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NameTest {

    @Test
    void givenCorrectAttributes_whenCreateName_thenNameCreated() {
        Name name = new Name("nconst", "primaryName", 1, 1, new String[]{"primaryProfession"});

        assertEquals("nconst", name.getNconst());
        assertEquals("primaryName", name.getPrimaryName());
        assertEquals(1, name.getBirthYear());
        assertEquals(1, name.getDeathYear());
        assertEquals("primaryProfession", name.getPrimaryProfessions()[0]);
    }

    @Test
    void givenNoAttributes_whenCreateName_thenNameCreated() {
        Name name = new Name();

        assertNull(name.getNconst());
        assertNull(name.getPrimaryName());
        assertEquals(0, name.getBirthYear());
        assertEquals(0, name.getDeathYear());
        assertNull(name.getPrimaryProfessions());
    }

}