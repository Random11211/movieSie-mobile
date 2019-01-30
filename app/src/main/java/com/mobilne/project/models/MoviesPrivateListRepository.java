package com.mobilne.project.models;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MoviesPrivateListRepository {
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;
//    private List<Movie> allMovies;

    MoviesPrivateListRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        movieDao = db.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

//    List<Movie> getAllMovies() {
//        return allMovies;
//    }


    public void addMovieToList (Movie movie) {
        new insertAsyncTask(movieDao).execute(movie);
    }

    public void deleteMovieFromList (Movie movie) {
        new deleteAsyncTask(movieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.addMovieToList(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.deleteMovieFromList(params[0]);
            return null;
        }
    }
}
