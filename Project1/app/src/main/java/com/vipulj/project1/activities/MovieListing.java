package com.vipulj.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.squareup.picasso.Picasso;
import com.vipulj.project1.R;
import com.vipulj.project1.specs.Specifications;
import com.vipulj.project1.models.Movie;
import com.vipulj.project1.network.Credentials;
import com.vipulj.project1.network.Endpoints;
import com.vipulj.project1.network.NetworkHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MovieListing extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    GridView         mGridView;
    ArrayList<Movie> moviesData;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGridView = (GridView) findViewById(R.id.grid);
        getMovieData();
    }

    public void getMovieData() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(final String... params) {
                NetworkHelper nh = new NetworkHelper();
                String jsonResponse = nh.getRequest(Endpoints.BASE_URL_MOVIEDB + Endpoints.GET_MOVIES + "&api_key=" +
                        Credentials.MOVIE_DB_API_KEY);

                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    Log.e(TAG, "EMPTY JSON");
                }
                else {
                    moviesData = parseMovieData(jsonResponse);
                }
                return jsonResponse;
            }

            private ArrayList<Movie> parseMovieData(final String jsonResponse) {
                Log.v("JSON", jsonResponse);
                moviesData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray moviesA = jsonObject.getJSONArray("results");
                    for (int i = 0; i < moviesA.length(); i++) {
                        JSONObject movieO = moviesA.getJSONObject(i);
                        String imageUrl = movieO.getString("poster_path");
                        String plot = movieO.getString("overview");
                        String title = movieO.getString("original_title");
                        String rating = movieO.getString("vote_average");
                        String releaseDate = movieO.getString("release_date");
                        String popularity = movieO.getString("popularity");
                        String id = movieO.getString("id");
                        imageUrl = Endpoints.BASE_URL_MOVIEDB_IMAGES + Specifications.IMAGE_SIZE + imageUrl;
                        moviesData.add(new Movie(id ,imageUrl, plot, title, rating, releaseDate, popularity));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return moviesData;
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                if (moviesData != null && moviesData.size() > 0) {
                    adapter = new ImageAdapter(MovieListing.this, moviesData);
                    mGridView.setAdapter(adapter);
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                                                final long id) {
                            Intent intent = new Intent(MovieListing.this, MovieDetail.class);
                            intent.putExtra(MovieDetail.KEY_MOVIE, moviesData.get(position));
                            startActivity(intent);
                        }
                    });
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<Movie> mMovies;

        public ImageAdapter(Context c, ArrayList<Movie> moviesData) {
            mContext = c;
            this.mMovies = moviesData;
        }

        public int getCount() {
            Log.v("Count", mMovies.size() + "");
            return mMovies.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
            }
            else {
                imageView = (ImageView) convertView;
            }
            String imageUrl = moviesData.get(position).getImageUrl();
            Picasso.with(mContext).load(imageUrl).into(imageView);
            return imageView;
        }
    }

    public void onRating(View v) {
        Collections.sort(moviesData, new RatingComparator());
        adapter.notifyDataSetChanged();
    }

    public void onPopularity(View v) {
        Collections.sort(moviesData, new PopularityComparator());
        adapter.notifyDataSetChanged();
    }


    public class RatingComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return Float.valueOf(o2.getRating()).compareTo(Float.valueOf(o1.getRating()));
        }
    }


    public class PopularityComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return Float.valueOf(o2.getPopularity()).compareTo(Float.valueOf(o1.getPopularity()));
        }
    }

}
