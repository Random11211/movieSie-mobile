package com.mobilne.project.api;

import com.mobilne.project.models.Genre;

import java.util.List;

public interface OnGetGenresCallback {

    void onSuccess(List<Genre> genres);

    void onError();
}