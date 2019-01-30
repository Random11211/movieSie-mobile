package com.mobilne.project.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mobilne.project.R;
import com.mobilne.project.api.MoviesRepository;
import com.mobilne.project.api.OnMoviesClickCallback;
import com.mobilne.project.models.Genre;
import com.mobilne.project.models.Movie;
import com.mobilne.project.models.MovieRoomDatabase;
import com.mobilne.project.models.MovieViewModel;

import java.util.List;

import static com.mobilne.project.activities.MainActivity.movieViewModel;

public class DatabaseMoviesActivity extends AppCompatActivity {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private PrivateMoviesListAdapter privateAdapter;
    private List<Genre> movieGenres;
    private List<Movie> moviesFromDatabase;

    private MoviesRepository moviesRepository;
    private TextView headline;

//    public static MovieViewModel movieViewModel;
    public MovieRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_activity);

        moviesRepository = MoviesRepository.getInstance();
        moviesList = findViewById(R.id.private_movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        db = MovieRoomDatabase.getDatabase(this);

        headline = findViewById(R.id.private_headline);


        headline.setText(getString(R.string.private_movies_list));
        moviesList.setLayoutManager(new GridLayoutManager(this, 2));

//        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesFromDatabase = movies;
                privateAdapter = new PrivateMoviesListAdapter(moviesFromDatabase, callback);
                moviesList.setAdapter(privateAdapter);
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(DatabaseMoviesActivity.this, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };
}
