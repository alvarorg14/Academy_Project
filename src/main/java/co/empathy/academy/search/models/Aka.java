package co.empathy.academy.search.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Aka {

    String title;
    String region;
    String language;
    Boolean isOriginalTitle;
}
