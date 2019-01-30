package com.mobilne.project.api;

import com.mobilne.project.models.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
