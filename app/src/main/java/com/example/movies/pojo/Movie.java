package com.example.movies.pojo;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "favorite_movie")
public class Movie implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("year")
    private int year;
    @SerializedName("alternativeName")
    private String alternativeName;
    @Embedded
    @SerializedName("poster")
    private Poster poster;
    @Embedded
    @SerializedName("rating")
    private Rating rating;

    public Movie(int id, String name, String description, int year, String alternativeName, Poster poster, Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.alternativeName = alternativeName;
        this.rating = rating;
        this.poster = poster;

    }


    public int getId() {
        return id;
    }

    public String getName() {
        if ((name == null) && (alternativeName != null)){
            name = alternativeName;
        } else if ((name == null) && (alternativeName == null)){
            name = "<нет названия>";
        }
        return name;
    }

    public String getDescription() {
        if (description == null){
            description = "<нет  описания>";
        }
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public Poster getPoster() {
        if (poster == null){
            poster = new Poster("https://st.kp.yandex.net/images/no-poster.jpg");
        }
        return poster;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", alternativeName='" + alternativeName + '\'' +
                ", poster=" + poster +
                ", rating=" + rating +
                '}';
    }
}
