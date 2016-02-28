package com.vipulj.project1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vipulj.project1.R;
import com.vipulj.project1.SharedPrefUtility;
import com.vipulj.project1.fragments.DetailFragment;
import com.vipulj.project1.models.Movie;
import com.vipulj.project1.models.MovieTrailer;
import com.vipulj.project1.network.Credentials;
import com.vipulj.project1.network.Endpoints;
import com.vipulj.project1.network.NetworkHelper;
import com.vipulj.project1.specs.Specifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {

    public static final  String KEY_MOVIE           = "key_movie";
    private static final String TAG                 = MovieDetail.class.getName();
    private static final String DETAIL_FRAGMENT_TAG = "DFT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            attachDetailFragment();
        }
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
    }

    private void attachDetailFragment() {
        Movie movie = getIntent().getParcelableExtra(KEY_MOVIE);
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        DetailFragment detailFragment = DetailFragment.newInstance(movie);
        ft.add(R.id.detail_fragment_container, detailFragment, DETAIL_FRAGMENT_TAG);
        ft.commit();

    }


}
