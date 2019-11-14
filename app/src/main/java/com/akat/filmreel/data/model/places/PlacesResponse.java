package com.akat.filmreel.data.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesResponse {

    @SerializedName("results")
    @Expose
    private List<Cinema> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Cinema> getResults() {
        return results;
    }

    public void setResults(List<Cinema> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
