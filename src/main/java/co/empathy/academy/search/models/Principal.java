package co.empathy.academy.search.models;

import co.empathy.academy.search.models.principals.Name;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Principal {
    Name name;
    String characters;
}
