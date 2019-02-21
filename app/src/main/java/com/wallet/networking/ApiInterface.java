package com.wallet.networking;

import com.wallet.model.QrTransfer;
import com.wallet.model.Transfer;
import com.wallet.model.User;
import com.wallet.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("users")
    Call<User> register(@Body User user);

    @POST("auth/login")
    Call<UserLogin> login(@Body User user);

    @POST("transfer/users/{userId}/receiver/{mobileNumber}")
    Call<Transfer> transfer(@Header("authorization") String authorization,
                            @Path("userId") String userId,
                            @Path("mobileNumber") String mobileNumber,
                            @Body Transfer transfer);

    @POST("transfer/users/{userId}/qrCodes")
    Call<QrTransfer> createQr(@Header("authorization") String authorization,
                              @Path("userId") String userId,
                              @Body QrTransfer qrTransfer);

    @POST("transfer/users/{userId}/qrCodes/{qrCode}")
    Call<QrTransfer> redeemQr(@Header("authorization") String authorization,
                              @Path("userId") String userId,
                              @Path("qrCode") String qrCode);
}
