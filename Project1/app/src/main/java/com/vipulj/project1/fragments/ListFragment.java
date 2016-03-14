package com.vipulj.project1.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vipulj.project1.R;
import com.vipulj.project1.SharedPrefUtility;
import com.vipulj.project1.activities.MovieDetail;
import com.vipulj.project1.models.Movie;
import com.vipulj.project1.network.Credentials;
import com.vipulj.project1.network.Endpoints;
import com.vipulj.project1.network.NetworkHelper;
import com.vipulj.project1.specs.Specifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VJ on 28/02/16.
 */

public class ListFragment extends Fragment {
    private final String TAG = this.getClass().getName();
    private Callback iCallback;


    GridView         mGridView;
    ArrayList<Movie> moviesData;
    ImageAdapter     adapter;
    private Context mContext;

    @Bind(R.id.actionRating)
    Button actionRating;

    @Bind(R.id.actionPopularity)
    Button actionPopularity;

    @Bind(R.id.actionFavourite)
    Button actionFavourite;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.content_listing, container);
        mGridView = (GridView) view.findViewById(R.id.grid);
        ButterKnife.bind(this, view);

        setup();
        getMovieData();
        return view;
    }

    private void setup() {
        actionRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                onRating();
            }
        });

        actionPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                onPopularity();
            }
        });

        actionFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                onFavourite();
            }
        });
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            mContext = context;
            iCallback = (Callback) context;
        }
        else {
            Log.v(TAG, "Problem");
        }

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
                        moviesData.add(new Movie(id, imageUrl, plot, title, rating, releaseDate, popularity));
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
                    adapter = new ImageAdapter(getActivity(), moviesData);
                    mGridView.setAdapter(adapter);


                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                                                final long id) {
                            iCallback.onGridItemClick(moviesData.get(position));
                        }
                    });

                    iCallback.setToDefault(moviesData.get(0));
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


    public void onRating() {
        Toast.makeText(getActivity(), "Inside", Toast.LENGTH_SHORT).show();
        Collections.sort(moviesData, new RatingComparator());
        adapter.notifyDataSetChanged();
    }

    public void onPopularity() {
        Toast.makeText(getActivity(), "Inside", Toast.LENGTH_SHORT).show();
        Collections.sort(moviesData, new PopularityComparator());
        adapter.notifyDataSetChanged();
    }

    public void onFavourite() {
        Toast.makeText(getActivity(), "Inside", Toast.LENGTH_SHORT).show();
        ArrayList<Movie> copy = new ArrayList<Movie>();
        copy.addAll(moviesData);
        sortByFavourite(copy);
        adapter.notifyDataSetChanged();
    }

    private void sortByFavourite(final ArrayList<Movie> moviesData) {
        Set<String> favourites = SharedPrefUtility.getFavoriteMoview(getActivity());
        for (Movie movie : this.moviesData) {
            if (favourites.contains(movie.getId())) {
                int index = moviesData.indexOf(movie);
                moviesData.remove(index);
                moviesData.add(0, movie);
            }
        }
        this.moviesData = moviesData;
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

    public interface Callback {

        void onGridItemClick(Movie movie);

        void setToDefault(Movie movie);
    }
}
