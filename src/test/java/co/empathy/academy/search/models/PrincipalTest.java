package co.empathy.academy.search.models;

import co.empathy.academy.search.models.principals.PrincipalsName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrincipalTest {

    @Test
    void givenAttributes_whenConstructor_thenPrincipalCreated() {
        Principal principal = new Principal(new PrincipalsName("nconst"), "characters");
        assertEquals("nconst", principal.getPrincipalsName().getNconst());
        assertEquals("characters", principal.getCharacters());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenPrincipalCreated() {
        Principal principal = new Principal();
        assertNull(principal.getPrincipalsName());
        assertNull(principal.getCharacters());
    }

}