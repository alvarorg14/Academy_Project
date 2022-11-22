package co.empathy.academy.search.models;

import co.empathy.academy.search.models.principals.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrincipalTest {

    @Test
    void givenAttributes_whenConstructor_thenPrincipalCreated() {
        Principal principal = new Principal(new Name("nconst"), "characters");
        assertEquals("nconst", principal.getName().getNconst());
        assertEquals("characters", principal.getCharacters());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenPrincipalCreated() {
        Principal principal = new Principal();
        assertNull(principal.getName());
        assertNull(principal.getCharacters());
    }

}