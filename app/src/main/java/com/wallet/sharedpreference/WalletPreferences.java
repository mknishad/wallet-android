package com.wallet.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wallet.model.User;
import com.wallet.util.Constant;

public class WalletPreferences {

    private static final String PREFERENCE_TITLE = "WalletPreferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public WalletPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putAuthToken(String authToken) {
        editor.putString(Constant.AUTH_TOKEN, authToken);
        editor.commit();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(Constant.AUTH_TOKEN, "");
    }

    public void putUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constant.USER, json);
        editor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constant.USER, "");
        return gson.fromJson(json, User.class);
    }
}
