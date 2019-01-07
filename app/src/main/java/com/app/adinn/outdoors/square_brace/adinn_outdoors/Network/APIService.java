package com.app.adinn.outdoors.square_brace.adinn_outdoors.Network;


import com.app.android.deal.dealclub.Data.FeaturedDealData;
import com.app.android.deal.dealclub.Data.HotDealData;
import com.app.android.deal.dealclub.Data.LoginData;
import com.app.android.deal.dealclub.Data.LoginResponse;
import com.app.android.deal.dealclub.Data.RegistrationResponse;
import com.app.android.deal.dealclub.Data.UserData;
import com.app.android.deal.dealclub.Data.UserId;
import com.app.android.deal.dealclub.Movie;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {


    @POST("/DealClub/Authentication")
    Call<LoginResponse> postLogin(@Body LoginData loginData);

    @POST("/DealClub/CreateCustomer")
    Call<RegistrationResponse> createUser(@Body UserData userData);

    @POST("/DealClub/UpdateCustomer")
    Call<RegistrationResponse> updateUser(@Body UserData userData);

    @POST("/DealClub/Customer/ProfileUpdation")
    Call<LoginResponse> getProfileCompleteStatus(@Body UserId userId);

    @GET("/DealClub/GetDeal")
    Call<List<Movie>> getMovies();

    @GET("/DealClubAuthentication")
    Call<ServerResponse> get(

            @Query("email") String username, @Query("password") String password);


    @POST("/DealClub/Authentication")
    Call<ServerResponse> postSingle(

            @Body RequestBody password

    );
    @GET("/DealClub/GetDeal/HotDeals")
    Call<List<HotDealData>> getHotDeal();

    @GET("/DealClub/GetDeal/FeaturedDeals")
    Call<List<FeaturedDealData>> getFeaturedDeal();
}
