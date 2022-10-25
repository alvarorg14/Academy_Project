package co.empathy.academy.search.models;

import lombok.Data;

@Data
public class Movie {

    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private String startYear;
    private String endYear;
    private int runtimeMinutes;
    private String genres;
}
