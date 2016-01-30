package com.vipulj.project1.network;

import com.squareup.picasso.Picasso;

/**
 * Created by VJ on 27/12/15.
 */
public class Endpoints {

    public static final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2" +
            ".5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
    public static final String BASE_URL_MOVIEDB_IMAGES = "http://image.tmdb.org/t/p/";
    public static final String BASE_URL_MOVIEDB = "http://api.themoviedb.org/3";
    public static final String GET_MOVIES       = "/discover/movie?";
//    Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
}
