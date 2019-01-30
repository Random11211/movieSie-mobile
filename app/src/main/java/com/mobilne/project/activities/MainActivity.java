package com.mobilne.project.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilne.project.api.OnGetGenresCallback;
import com.mobilne.project.api.OnMoviesClickCallback;
import com.mobilne.project.models.Genre;
import com.mobilne.project.models.Movie;
import com.mobilne.project.api.MoviesRepository;
import com.mobilne.project.api.OnGetMoviesCallback;
import com.mobilne.project.R;
import com.mobilne.project.models.MovieRoomDatabase;
import com.mobilne.project.models.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private PrivateMoviesListAdapter privateAdapter;
    private List<Genre> movieGenres;
    private List<Movie> moviesFromDatabase;

    private MoviesRepository moviesRepository;
    private TextView headline;

    public static MovieViewModel movieViewModel;
    public MovieRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRepository = MoviesRepository.getInstance();
        moviesList = findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        db = MovieRoomDatabase.getDatabase(this);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        headline = findViewById(R.id.headline);
        getGenres();

//        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
//        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(@Nullable List<Movie> movies) {
//                moviesFromDatabase = movies;
//            }
//        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getApplicationContext().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResults(searchView.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                adapter.clearMovies();
                getMovies();
                return true;

            case R.id.upcoming:
                adapter.clearMovies();
                getUpcoming();
                return true;

            case R.id.your_movies:
//                headline.setText(getString(R.string.private_movies_list));
//                moviesList.setLayoutManager(new GridLayoutManager(this, 2));
//                getYourMovies();
//                moviesList.setAdapter(privateAdapter);
                Intent intent = new Intent(MainActivity.this, DatabaseMoviesActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies() {
        int page = 1;
        headline.setText(getString(R.string.popular));
        moviesList.setLayoutManager(new LinearLayoutManager(this));
        moviesRepository.getMovies(page, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                adapter = new MoviesAdapter(movies, movieGenres, callback);
                moviesList.setAdapter(adapter);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getUpcoming() {
        int page = 1;
        headline.setText(getString(R.string.upcoming));
        moviesList.setLayoutManager(new LinearLayoutManager(this));
        moviesRepository.getUpcoming(page, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                adapter = new MoviesAdapter(movies, movieGenres, callback);
                moviesList.setAdapter(adapter);
            }


            @Override
            public void onError() {
                showError();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getYourMovies() {
        privateAdapter = new PrivateMoviesListAdapter(moviesFromDatabase, callback);

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                List<Movie> all = db.movieDao().getAllMovies();
//                moviesFromDatabase = all;
//                MainActivity.this.runOnUiThread(new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        privateAdapter = new PrivateMoviesListAdapter(moviesFromDatabase, callback);
//                    }
//                }) {
//                });
//                return null;
//            }
//        }.execute();
    }


    private void getSearchResults(String title) {
        int page = 1;
        headline.setText(getString(R.string.search, title));
        moviesRepository.getSearchResults(page, title, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void showError() {
        Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

}