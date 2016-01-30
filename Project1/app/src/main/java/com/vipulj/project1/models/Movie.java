package com.vipulj.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by VJ on 11/01/16.
 */
public class Movie implements Parcelable {
    private String imageUrl;
    private String plot;
    private String title;
    private String rating;
    private String releaseDate;
    private String popularity;

    public String getPopularity() {
        return popularity;
    }

    public String getPlot() {
        return plot;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public Movie(final String imageUrl, final String plot, final String title, final String rating, final String
            releaseDate, String popularity) {
        this.imageUrl = imageUrl;
        this.plot = plot;
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }


    // Parcelable
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(final Parcel parcel) {
            return fromJsonString(parcel.readString());
        }

        @Override
        public Movie[] newArray(final int i) {
            return new Movie[0];
        }
    };

    @Override
    public void writeToParcel(final Parcel parcel, final int flag) {
        parcel.writeString(toJsonString());
    }

    public String toJsonString() {
        return new Gson().toJson(this, Movie.class);
    }

    public static Movie fromJsonString(final String json) {
        try {
            return new Gson().fromJson(json, Movie.class);
        } catch (final Exception e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
