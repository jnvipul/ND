package com.vipulj.project1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.Toast;

import com.vipulj.project1.R;
import com.vipulj.project1.fragments.DetailFragment;
import com.vipulj.project1.fragments.ListFragment;
import com.vipulj.project1.models.Movie;

public class MovieListing extends AppCompatActivity implements ListFragment.Callback{



    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {

        if (findViewById(R.id.detail_fragment_container) == null) {
            // Phone
            mTwoPane = false;
        }
        else {
            // Tablet
            mTwoPane = true;
            attachDetailFragment();

        }


    }

    private void attachDetailFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        DetailFragment detailFragment = new DetailFragment();
        ft.replace(R.id.detail_fragment_container, detailFragment);
        ft.commit();
    }


    @Override
    public void onGridItemClick(Movie movie) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, MovieDetail.class);
            intent.putExtra(MovieDetail.KEY_MOVIE, movie);
            startActivity(intent);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            DetailFragment detailFragment =  DetailFragment.newInstance(movie);
            ft.replace(R.id.detail_fragment_container, detailFragment);
            ft.commit();
        }
    }
}
