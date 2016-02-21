package com.vipulj.project1.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {

    public static final  String KEY_MOVIE = "key_movie";
    private static final String TAG       = MovieDetail.class.getName();
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

    @Bind(R.id.rootLayout)
    LinearLayout rootLayout;
    private ArrayList<MovieTrailer> mMovieTrailers;

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

        getTrailerData();
    }


    public void getTrailerData() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(final String... params) {
                NetworkHelper nh = new NetworkHelper();
                String trailer_endpoint = String.format(Endpoints.GET_TRAILERS, mMovie.getId());
                String jsonResponse = nh.getRequest(Endpoints.BASE_URL_MOVIEDB + trailer_endpoint + "&api_key=" +
                        Credentials.MOVIE_DB_API_KEY);

                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    Log.e(TAG, "EMPTY JSON");
                }
                else {
                    mMovieTrailers = parseMovieData(jsonResponse);
                }
                return jsonResponse;
            }

            private ArrayList<MovieTrailer> parseMovieData(final String jsonResponse) {
                Log.v("JSON", jsonResponse);
                mMovieTrailers = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray moviesA = jsonObject.getJSONArray("results");
                    for (int i = 0; i < moviesA.length(); i++) {
                        JSONObject movieO = moviesA.getJSONObject(i);
                        String name = movieO.getString("name");
                        String id = movieO.getString("id");
                        mMovieTrailers.add(new MovieTrailer(id, name));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mMovieTrailers;
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                if (mMovieTrailers != null && mMovieTrailers.size() > 0) {
                    addTrailers();
                }

            }

            private void addTrailers() {
                for(final MovieTrailer movieTrailer : mMovieTrailers){
                    TextView trailer = new TextView(MovieDetail.this);
                    trailer.setText(movieTrailer.getName());
                    trailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            openYoutube(movieTrailer.getId());

                        }
                    });
                    rootLayout.addView(trailer);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void openYoutube(String videoId){
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
//        intent.putExtra("VIDEO_ID", videoId);
//        startActivity(intent);
        videoId = "n_8xwn_eghc";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId)));
    }
}
