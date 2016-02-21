package com.vipulj.project1.models;

/**
 * Created by VJ on 21/02/16.
 */
public class MovieTrailer {
    String id;
    String name;

    public MovieTrailer(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }
}
