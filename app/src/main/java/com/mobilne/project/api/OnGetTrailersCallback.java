package com.mobilne.project.api;

import com.mobilne.project.models.Trailer;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}