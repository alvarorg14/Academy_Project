package co.empathy.academy.search.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Name {
    String nconst;
    String primaryName;
    int birthYear;
    int deathYear;
    String[] primaryProfessions;
}
