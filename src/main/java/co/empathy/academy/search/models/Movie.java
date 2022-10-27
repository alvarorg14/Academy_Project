package co.empathy.academy.search.models;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class Movie {

    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    boolean adult;
    String startYear;
    String endYear;
    int runtimeMinutes;
    String genres;
}
