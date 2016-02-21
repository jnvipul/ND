package com.vipulj.project1.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vipulj.project1.R;
import com.vipulj.project1.models.Review;
import com.vipulj.project1.network.Credentials;
import com.vipulj.project1.network.Endpoints;
import com.vipulj.project1.network.NetworkHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

    @Bind(R.id.list)
    ListView list;
    private static final String TAG = ReviewsActivity.class.getName();
    public static String            MOVIE_ID;
    private ArrayList<Review> mMovieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getReviewsData();
    }

    public void showReviews() {
        Log.v(TAG, "Showing reviews");
        list.setAdapter(new ReviewsAdapter(this, R.layout.activity_reviews, mMovieReviews));
    }

    class ReviewsAdapter extends ArrayAdapter<Review> {

        public ReviewsAdapter(final Context context, final int resource, final List<Review> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            Log.v(TAG, "Inside getview");
            Review review = mMovieReviews.get(position);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_review, null);
            }

            TextView author = (TextView) convertView.findViewById(R.id.author);
            author.setText(review.getAuthor());
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(review.getText());
            return convertView;
        }
    }


    public void getReviewsData() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(final String... params) {
                NetworkHelper nh = new NetworkHelper();
                String trailer_endpoint = String.format(Endpoints.GET_REVIEWS, MOVIE_ID);
                String jsonResponse = nh.getRequest(Endpoints.BASE_URL_MOVIEDB + trailer_endpoint + "&api_key=" +
                        Credentials.MOVIE_DB_API_KEY);

                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    Log.e(TAG, "EMPTY JSON");
                }
                else {
                    mMovieReviews = parseMovieReviewData(jsonResponse);
                }
                return jsonResponse;
            }

            private ArrayList<Review> parseMovieReviewData(final String jsonResponse) {
                Log.v("JSON", jsonResponse);
                mMovieReviews = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray moviesA = jsonObject.getJSONArray("results");
                    for (int i = 0; i < moviesA.length(); i++) {
                        JSONObject movieO = moviesA.getJSONObject(i);
                        String author = movieO.getString("author");
                        String content = movieO.getString("content");
                        mMovieReviews.add(new Review(content, author));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mMovieReviews;
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                if (mMovieReviews != null && mMovieReviews.size() > 0) {
                    showReviews();
                }else{
                    Log.v(TAG, "No Data");
                }

            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
