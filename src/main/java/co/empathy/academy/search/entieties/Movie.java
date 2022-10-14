package co.empathy.academy.search.entieties;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName="movies")
public class Movie {

    @Id
    private String id;

    @Field(type = FieldType.Keyword, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Keyword, name = "gender")
    private String gender;

    @Field(type = FieldType.Integer, name = "year")
    private int year;

    public Movie(String title, String description, String gender, int year){
        this.title = title;
        this.description = description;
        this.gender = gender;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", gender='" + gender + '\'' +
                ", year=" + year +
                '}';
    }
}
