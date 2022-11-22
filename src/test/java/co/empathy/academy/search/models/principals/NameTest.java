package co.empathy.academy.search.models.principals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NameTest {

    @Test
    void givenAttributes_whenConstructor_thenNameCreated() {
        Name name = new Name("nconst");
        assertEquals("nconst", name.getNconst());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenNameCreated() {
        Name name = new Name();
        assertNull(name.getNconst());
    }

}