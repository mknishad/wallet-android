package com.wallet.networking;

import com.wallet.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("users")
    Call<User> register(@Body User user);
}
