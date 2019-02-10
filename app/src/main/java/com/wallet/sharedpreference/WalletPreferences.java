package com.wallet.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

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
}
