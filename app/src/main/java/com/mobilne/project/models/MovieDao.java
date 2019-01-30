package com.mobilne.project.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface MovieDao {

    @Query("SELECT * from movie_table ORDER BY title ASC")
//    List<Movie> getAllMovies();
    LiveData<List<Movie>> getAllMovies();

    @Insert
    void addMovieToList(Movie movie);

    @Delete
    void deleteMovieFromList(Movie movie);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Insert
    void insertAll(Movie... dataEntities);
}
