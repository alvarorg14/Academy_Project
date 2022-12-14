package co.empathy.academy.search.util;

import co.empathy.academy.search.models.*;
import co.empathy.academy.search.models.principals.PrincipalsName;

import java.util.ArrayList;
import java.util.List;

public class ResourcesUtil {

    public static List<Movie> getMovies() {
        return new ArrayList<>() {{
            add(getMovie("1"));
            add(getMovie("2"));
        }};
    }

    public static Movie getMovie(String sufix) {
        return new Movie("test" + sufix, "type" + sufix, "primary" + sufix, "original" + sufix,
                false, 2000, 0, 90, new String[]{"genre1", "genre2"},
                8.5, 100, getAkas(), getDirectors(), getPrincipals());
    }

    public static Aka getAka(String sufix) {
        return new Aka("testTitle" + sufix, "testRegion" + sufix,
                "testLanguage" + sufix, true);
    }

    public static List<Aka> getAkas() {
        return new ArrayList<>() {{
            add(getAka("1"));
            add(getAka("2"));
            add(getAka("3"));
        }};
    }

    public static Director getDirector(String sufix) {
        return new Director("testNConst" + sufix);
    }

    public static List<Director> getDirectors() {
        return new ArrayList<>() {{
            add(getDirector("1"));
            add(getDirector("2"));
        }};
    }

    public static Principal getPrincipal(String sufix) {
        return new Principal(new PrincipalsName("testName" + sufix), "testCategory" + sufix);
    }

    public static List<Principal> getPrincipals() {
        return new ArrayList<>() {{
            add(getPrincipal("1"));
            add(getPrincipal("2"));
        }};
    }

    public static Name getName(String sufix) {
        return new Name("testNConst" + sufix, "testPrimaryName" + sufix, 0, 0, new String[]{"testProfession1", "testProfession2"});
    }

    public static List<Name> getNames() {
        return new ArrayList<>() {{
            add(getName("1"));
            add(getName("2"));
        }};
    }


}
