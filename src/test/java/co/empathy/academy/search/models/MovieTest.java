package co.empathy.academy.search.models;

import co.empathy.academy.search.util.ResourcesUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieTest {

    @Test
    void givenCorrectAttributes_whenCreateMovie_thenMovieCreated() {
        Movie movie = new Movie("tconst", "titleType", "primaryTitle",
                "originalTitle", true, 1, 1, 1, new String[]{"genre"},
                1.0, 1, ResourcesUtil.getAkas(), ResourcesUtil.getDirectors());

        assertEquals("tconst", movie.getTconst());
        assertEquals("titleType", movie.getTitleType());
        assertEquals("primaryTitle", movie.getPrimaryTitle());
        assertEquals("originalTitle", movie.getOriginalTitle());
        assertEquals(true, movie.getIsAdult());
        assertEquals(1, movie.getStartYear());
        assertEquals(1, movie.getEndYear());
        assertEquals(1, movie.getRuntimeMinutes());
        assertEquals("genre", movie.getGenres()[0]);
        assertEquals(1.0, movie.getAverageRating());
        assertEquals(1, movie.getNumVotes());
        assertEquals(ResourcesUtil.getAkas(), movie.getAkas());
        assertEquals(ResourcesUtil.getDirectors(), movie.getDirectors());
    }

}