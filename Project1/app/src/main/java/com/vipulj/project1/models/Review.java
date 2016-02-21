package com.vipulj.project1.models;

/**
 * Created by VJ on 21/02/16.
 */
public class Review {
    String text;
    String author;

    public Review(final String text, final String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
