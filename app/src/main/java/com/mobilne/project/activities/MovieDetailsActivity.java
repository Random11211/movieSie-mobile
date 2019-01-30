package com.mobilne.project.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobilne.project.R;
import com.mobilne.project.api.MoviesRepository;
import com.mobilne.project.api.OnGetGenresCallback;
import com.mobilne.project.api.OnGetMovieCallback;
import com.mobilne.project.api.OnGetTrailersCallback;
import com.mobilne.project.models.Genre;
import com.mobilne.project.models.Movie;
import com.mobilne.project.models.MovieRoomDatabase;
import com.mobilne.project.models.Trailer;

import java.util.ArrayList;
import java.util.List;

//import static com.mobilne.project.activities.MainActivity.movieViewModel;

public class MovieDetailsActivity extends AppCompatActivity {

    public MovieRoomDatabase db = MovieRoomDatabase.getDatabase(this);

    public static String MOVIE_ID = "movie_id";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private LinearLayout movieTrailers;
    private TextView trailersLabel;
    private Button wantToSee;

    private MoviesRepository moviesRepository;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        moviesRepository = MoviesRepository.getInstance();

        init();

        getMovie();
    }

    private void init() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieTrailers = findViewById(R.id.movieTrailers);
        trailersLabel = findViewById(R.id.trailersLabel);
        wantToSee = findViewById(R.id.movieDetailsWish);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(final Movie movie) {
                movieTitle.setText(movie.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                getGenres(movie);
                getTrailers(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }

                wantToSee.setSelected(movie.getStateWish());
                wantToSee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean newState = !movie.getStateWish();
                        movie.setStateWish(newState);
                        wantToSee.setSelected(movie.getStateWish());

                        if(movie.getStateWish().equals(true)){
//                            movieViewModel.addMovieToList(movie);
                            db.movieDao().addMovieToList(movie);
                            Toast.makeText(wantToSee.getContext(), R.string.wantToSeeToast,  Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    Glide.with(MovieDetailsActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    movieTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void showError() {
        Toast.makeText(MovieDetailsActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}