package com.mobilne.project.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MoviesPrivateListRepository repository;
//    private LiveData<List<Movie>> allMovies;
    private List<Movie> allMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MoviesPrivateListRepository(application);
        allMovies = repository.getAllMovies();
    }

//    public LiveData<List<Movie>> getAllMovies() {
//        return allMovies;
//    }
    public List<Movie> getAllMovies() {
    return allMovies;
}


    public void addMovieToList (Movie movie) {
        repository.addMovieToList(movie);
    }

    public void deleteMovieFromList (Movie movie) {
        repository.deleteMovieFromList(movie);
    }

}
