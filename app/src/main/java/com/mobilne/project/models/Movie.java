package com.mobilne.project.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movie_table")
public class Movie {

    private Boolean stateWish = false;

    @PrimaryKey(autoGenerate = true)
    private int idDB;

    @Ignore
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("vote_average")
    @Expose
    private float rating;

    @SerializedName("overview")
    @Expose
    private String overview;

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds;

    @SerializedName("backdrop_path")
    @Expose
    private String backdrop;

    @Ignore
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    public void setStateWish(Boolean stateWish) {
        this.stateWish = stateWish;
    }

    public Boolean getStateWish() {
        return stateWish;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getIdDB() {
        return idDB;
    }

    public void setIdDB(int idDB) {
        this.idDB = idDB;
    }
}