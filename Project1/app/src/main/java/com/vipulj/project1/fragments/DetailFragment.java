package com.vipulj.project1.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vipulj.project1.R;
import com.vipulj.project1.SharedPrefUtility;
import com.vipulj.project1.activities.MovieDetail;
import com.vipulj.project1.activities.ReviewsActivity;
import com.vipulj.project1.models.Movie;
import com.vipulj.project1.models.MovieTrailer;
import com.vipulj.project1.network.Credentials;
import com.vipulj.project1.network.Endpoints;
import com.vipulj.project1.network.NetworkHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VJ on 28/02/16.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getName();
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
    @Bind(R.id.fav)
    ImageView favImage;

    @Bind(R.id.rootLayout)
    LinearLayout rootLayout;

    Movie mMovie;
    private ArrayList<MovieTrailer> mMovieTrailers;

    public static DetailFragment newInstance(Movie movie) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieDetail.KEY_MOVIE, movie);
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle
            savedInstanceState) {
        Log.v(TAG, "Working");
        View view = inflater.inflate(R.layout.fragment_detail, null);
        ButterKnife.bind(this, view);
        setup();
        Log.v(TAG, "Working");
        return view;
    }

    private void setup() {
        mMovie = getArguments().getParcelable(MovieDetail.KEY_MOVIE);
        if (mMovie != null) {
            Log.v("Movie", "Received");
            displayMovieDate();
        }
        else {
            Log.v(TAG, "Not Working");
        }


    }


    private void displayMovieDate() {
        title.setText(mMovie.getTitle());
        rating.setText(mMovie.getRating());
        plot.setText(mMovie.getPlot());
        releaseDate.setText(mMovie.getReleaseDate());
        Picasso.with(getActivity()).load(mMovie.getImageUrl()).into(image);
        Log.v(TAG, "Working");
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
                for (final MovieTrailer movieTrailer : mMovieTrailers) {
                    TextView trailer = new TextView(getActivity());
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

    public void openYoutube(String videoId) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
//        intent.putExtra("VIDEO_ID", videoId);
//        startActivity(intent);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId)));
    }

    @OnClick(R.id.reviews)
    public void onReviews(View view) {
        ReviewsActivity.MOVIE_ID = mMovie.getId();
        startActivity(new Intent(getActivity(), ReviewsActivity.class));
    }

    @OnClick(R.id.fav)
    public void onFavourite(View v) {
        Set<String> favourites = SharedPrefUtility.getFavoriteMoview(getActivity());
        if (favourites.contains(mMovie.getId())) {
//            Already in favourites
            favImage.setBackground(getResources().getDrawable(R.drawable.unselected_star, getActivity().getTheme()));
            favourites.remove(mMovie.getId());
            SharedPrefUtility.setFavoriteMoview(getActivity(), favourites);
        }
        else {
//            Add to favourites
            favImage.setBackground(getResources().getDrawable(R.drawable.selected_star, getActivity().getTheme()));
            favourites.add(mMovie.getId());
            SharedPrefUtility.setFavoriteMoview(getActivity(), favourites);

        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
    }


}
