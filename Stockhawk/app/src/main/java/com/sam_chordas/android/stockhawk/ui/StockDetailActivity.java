package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.network.APIEndpointInterface;
import com.sam_chordas.android.stockhawk.network.POJO.Example;
import com.sam_chordas.android.stockhawk.network.POJO.Query;
import com.sam_chordas.android.stockhawk.network.POJO.Quote;


import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockDetailActivity extends Activity {

    public static final String STOCK_SYMBOL = "Stock Symbol";

    //View
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        setup();
    }

    private void setup() {
        getStockHistory();
        mLineChart = (LineChart) findViewById(R.id.chart_view);
        String stockSymbol = getIntent().getStringExtra(STOCK_SYMBOL);

        mLineChart.setDescription(stockSymbol);

    }


    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void showChartData(Query query) {
        ArrayList<String> xVals = new ArrayList<String>();

        Gson gson = new Gson();
        Log.v("Query", gson.toJson(query));
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < query.results.quote.size(); i++) {
            Quote quote = query.results.quote.get(i);
            xVals.add(quote.Date);
            entries.add(new Entry(Float.valueOf(quote.Close), i));

        }

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        LineData data = new LineData(xVals, dataset);
        mLineChart.setData(data);
        mLineChart.notifyDataSetChanged();
        mLineChart.performClick();
        mLineChart.animate();
        mLineChart.callOnClick();
        mLineChart.invalidate();
        mLineChart.requestFocus();
        mLineChart.setNoDataText(getString(R.string.historical_data));
    }

    public void getStockHistory() {


        // Retrofit Logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        // Create Retrofit instance
        String BASE_URL = "http://query.yahooapis.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        APIEndpointInterface apiEndpointInterface = retrofit.create(APIEndpointInterface.class);
        Call<Example> call = apiEndpointInterface.getPastData();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(final Call<Example> call, final Response<Example> response) {
                Log.v("Response", new Gson().toJson(response));
                Query query = response.body().query;
                if(query != null){
                    showChartData(query);
                }
            }

            @Override
            public void onFailure(final Call<Example> call, final Throwable t) {
                showToast(t.toString());
            }
        });

    }
}
