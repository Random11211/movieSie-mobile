package com.mobilne.project.api;

import com.mobilne.project.models.Movie;

import java.util.List;

public interface OnGetMoviesCallback {

    void onSuccess(int page, List<Movie> movies);

    void onError();
}
