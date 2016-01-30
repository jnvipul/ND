package com.vipulj.project1.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vipulj.project1.R;
import com.vipulj.project1.models.Movie;

import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {

    public static final String KEY_MOVIE = "key_movie";
    Movie mMovie;

    @Bind(R.id.title)
    TextView  title;
    @Bind(R.id.rating)
    TextView  rating;
    @Bind(R.id.plot)
    TextView  plot;
    @Bind(R.id.releaseDate)
    TextView  releaseDate;
    @Bind(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMovie = getIntent().getParcelableExtra(KEY_MOVIE);
        if (mMovie != null) {
            Log.v("Movie", "Received");
            displayMovieDate();
        }


    }

    private void displayMovieDate() {
        title.setText(mMovie.getTitle());
        rating.setText(mMovie.getRating());
        plot.setText(mMovie.getPlot());
        releaseDate.setText(mMovie.getReleaseDate());
        Picasso.with(this).load(mMovie.getImageUrl()).into(image);
    }

}
