package com.mobilne.project.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobilne.project.R;
import com.mobilne.project.api.OnMoviesClickCallback;
import com.mobilne.project.models.Movie;
import com.mobilne.project.models.MovieRoomDatabase;

import java.util.List;

public class PrivateMoviesListAdapter extends RecyclerView.Adapter<PrivateMoviesListAdapter.MovieViewHolder>{

    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private List<Movie> movies;
    private OnMoviesClickCallback callback;

    public PrivateMoviesListAdapter(List<Movie> movies, OnMoviesClickCallback callback) {
        this.movies = movies;
        this.callback = callback;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_list_card, parent, false);
        return new PrivateMoviesListAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        ImageView poster;
        Button delete;
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(movie);
                }
            });

            releaseDate = itemView.findViewById(R.id.card_release_date);
            title = itemView.findViewById(R.id.card_title);
            poster = itemView.findViewById(R.id.card_poster);
            delete = itemView.findViewById(R.id.card_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    db.movieDao().deleteMovieFromList(movie);
                    Toast.makeText(delete.getContext(), R.string.dontwantToSeeToast, Toast.LENGTH_SHORT).show();
                }
            });

        }

        public void bind(Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate());
            title.setText(movie.getTitle());

            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .into(poster);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

}
