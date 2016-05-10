package com.sam_chordas.android.stockhawk.network;

import com.sam_chordas.android.stockhawk.network.POJO.Example;
import com.sam_chordas.android.stockhawk.network.POJO.Query;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by VJ on 07/05/16.
 */
public interface APIEndpointInterface {

    @GET("v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20(%27YHOO%27)" +
            "%20and%20startDate%20=%20%272009-09-11%27%20and%20endDate%20=%20%272010-03-10%27&format=json&diagnostics" +
            "=true&env=store://datatables.org/alltableswithkeys")
    Call<Example> getPastData();
}
