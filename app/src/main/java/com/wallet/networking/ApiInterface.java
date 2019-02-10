package com.wallet.networking;

import com.wallet.model.User;
import com.wallet.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("users")
    Call<User> register(@Body User user);

    @POST("auth/login")
    Call<UserLogin> login(@Body User user);
}
