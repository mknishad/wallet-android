package com.wallet.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;
import com.wallet.model.User;
import com.wallet.model.UserLogin;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;
import com.wallet.sharedpreference.WalletPreferences;
import com.wallet.util.Constant;
import com.wallet.util.WalletUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phoneNumberWrapper)
    TextInputLayout phoneNumberWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.registerTextView)
    TextView registerTextView;

    private static final String TAG = "LoginActivity";

    private Context context;
    private ApiInterface apiInterface;
    private WalletPreferences preferences;
    private ProgressDialog progressDialog;
    private User user;
    private UserLogin userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(toolbar);

        context = LoginActivity.this;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        preferences = new WalletPreferences(context);
        user = new User();
        userLogin = new UserLogin();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.loginButton, R.id.registerTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                login();
                break;
            case R.id.registerTextView:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void login() {
        if (!WalletUtil.hasInternetConnection(context)) {
            WalletUtil.showSnackbar(toolbar, getString(R.string.no_internet_connection));
            Log.e(TAG, "getUserStateFromServer: " + getString(R.string.no_internet_connection));
            return;
        }

        String phone = phoneNumberWrapper.getEditText().getText().toString().trim();
        String password = passwordWrapper.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            WalletUtil.showSnackbar(loginButton, getString(R.string.enter_phone_number));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            WalletUtil.showSnackbar(loginButton, getString(R.string.enter_password));
            return;
        }

        user.setMobileNumber(phone);
        user.setPassword(password);

        progressDialog.show();
        Call<UserLogin> loginCall = apiInterface.login(user);
        loginCall.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                try {
                    Log.d(TAG, "onResponse: ");
                    progressDialog.dismiss();

                    if (response.code() == 200) {
                        userLogin = response.body();
                        Log.d(TAG, "onResponse: userLogin = " + userLogin);
                        preferences.putAuthToken(userLogin.getToken());
                        startActivity(new Intent(context, MainActivity.class)
                                .putExtra(Constant.USER, userLogin.getUser()));
                        finishAffinity();
                    } else {
                        WalletUtil.showSnackbar(loginButton, getString(R.string.something_went_wrong));
                        Log.e(TAG, "onResponse: response.code() = " + response.code());
                    }
                } catch (Exception e) {
                    WalletUtil.showSnackbar(loginButton, getString(R.string.something_went_wrong));
                    Log.e(TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                try {
                    progressDialog.dismiss();
                    WalletUtil.showSnackbar(loginButton, getString(R.string.unable_to_connect));
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                }
            }
        });
    }
}
