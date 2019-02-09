package com.wallet.networking;

import com.wallet.model.RegisterRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("users")
    Call<ArrayList<RegisterRequest>> register(@Body RegisterRequest registerRequest);
}
