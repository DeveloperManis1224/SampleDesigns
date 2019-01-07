package com.app.adinn.outdoors.square_brace.adinn_outdoors.Network;


import com.app.android.deal.dealclub.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("DealClub/GetDeal")
    Call<List<Movie>> getMovies();
}