package co.empathy.academy.search.models.principals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrincipalsNameTest {

    @Test
    void givenAttributes_whenConstructor_thenNameCreated() {
        PrincipalsName principalsName = new PrincipalsName("nconst");
        assertEquals("nconst", principalsName.getNconst());
    }

    @Test
    void givenNoAttributes_whenConstructor_thenNameCreated() {
        PrincipalsName principalsName = new PrincipalsName();
        assertNull(principalsName.getNconst());
    }

}