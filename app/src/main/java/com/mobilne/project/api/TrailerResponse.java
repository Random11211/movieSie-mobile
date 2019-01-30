package com.mobilne.project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobilne.project.models.Trailer;

import java.util.List;

public class TrailerResponse {

    @SerializedName("results")
    @Expose
    private List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}