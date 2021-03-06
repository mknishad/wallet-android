package com.wallet.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;
import com.wallet.model.User;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;
import com.wallet.util.WalletUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phoneNumberWrapper)
    TextInputLayout phoneNumberWrapper;
    @BindView(R.id.nameWrapper)
    TextInputLayout nameWrapper;
    @BindView(R.id.emailWrapper)
    TextInputLayout emailWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.registerButton)
    Button registerButton;

    private static final String TAG = "RegisterActivity";

    private Context context;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrater);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        context = RegisterActivity.this;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        alertDialogBuilder = new AlertDialog.Builder(context)
                .setMessage("Registration Successful!")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setCancelable(false);
    }

    @OnClick(R.id.registerButton)
    public void onViewClicked() {
        register();
    }

    private void register() {
        if (!WalletUtil.hasInternetConnection(context)) {
            WalletUtil.showSnackbar(registerButton, getString(R.string.no_internet_connection));
            Log.e(TAG, "getUserStateFromServer: " + getString(R.string.no_internet_connection));
            return;
        }

        String phone = phoneNumberWrapper.getEditText().getText().toString().trim();
        String name = nameWrapper.getEditText().getText().toString().trim();
        String email = emailWrapper.getEditText().getText().toString().trim();
        String password = passwordWrapper.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            WalletUtil.showSnackbar(registerButton, getString(R.string.enter_phone_number));
            return;
        }
        if (TextUtils.isEmpty(name)) {
            WalletUtil.showSnackbar(registerButton, getString(R.string.enter_name));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            WalletUtil.showSnackbar(registerButton, getString(R.string.enter_password));
            return;
        }

        user.setMobileNumber(phone);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        progressDialog.show();
        Call<User> registerCall = apiInterface.register(user);
        registerCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Log.d(TAG, "onResponse: ");
                    progressDialog.dismiss();

                    if (response.code() == 200) {
                        user = response.body();
                        Log.d(TAG, "onResponse: user = " + user);
                        alertDialogBuilder.show();
                    } else {
                        WalletUtil.showSnackbar(registerButton, getString(R.string.something_went_wrong));
                        Log.e(TAG, "onResponse: response.code() = " + response.code());
                    }
                } catch (Exception e) {
                    WalletUtil.showSnackbar(registerButton, getString(R.string.something_went_wrong));
                    Log.e(TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                try {
                    progressDialog.dismiss();
                    WalletUtil.showSnackbar(registerButton, getString(R.string.unable_to_connect));
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                }
            }
        });
    }
}
