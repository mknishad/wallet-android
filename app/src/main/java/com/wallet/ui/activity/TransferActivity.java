package com.wallet.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import com.wallet.R;
import com.wallet.model.Transfer;
import com.wallet.model.User;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;
import com.wallet.sharedpreference.WalletPreferences;
import com.wallet.util.Constant;
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

public class TransferActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phoneNumberWrapper)
    TextInputLayout phoneNumberWrapper;
    @BindView(R.id.amountWrapper)
    TextInputLayout amountWrapper;
    @BindView(R.id.pinCodeWrapper)
    TextInputLayout pinCodeWrapper;
    @BindView(R.id.transferButton)
    Button transferButton;

    private static final String TAG = "TransferActivity";

    private Context context;
    private WalletPreferences preferences;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private User user;
    private Transfer transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setupToolbar();

        context = TransferActivity.this;
        preferences = new WalletPreferences(context);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = preferences.getUser();
        transfer = new Transfer();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        alertDialogBuilder = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .setCancelable(false);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.transfer);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @OnClick(R.id.transferButton)
    public void onViewClicked() {
        transfer();
    }

    private void transfer() {
        if (!WalletUtil.hasInternetConnection(context)) {
            WalletUtil.showSnackbar(toolbar, getString(R.string.no_internet_connection));
            Log.e(TAG, "getUserStateFromServer: " + getString(R.string.no_internet_connection));
            return;
        }

        String phoneNumber = phoneNumberWrapper.getEditText().getText().toString().trim();
        String amount = amountWrapper.getEditText().getText().toString().trim();
        String pinCode = pinCodeWrapper.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            WalletUtil.showSnackbar(transferButton, getString(R.string.enter_phone_number));
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            WalletUtil.showSnackbar(transferButton, getString(R.string.enter_amount));
            return;
        }
        if (TextUtils.isEmpty(pinCode)) {
            WalletUtil.showSnackbar(transferButton, getString(R.string.enter_pin_code));
            return;
        }

        transfer.setAmount(amount);
        transfer.setPinCode(pinCode);

        progressDialog.show();
        Call<Transfer> transferCall = apiInterface.transfer(
                Constant.BEARER + preferences.getAuthToken(),
                user.get_id(), phoneNumber, transfer);
        transferCall.enqueue(new Callback<Transfer>() {
            @Override
            public void onResponse(Call<Transfer> call, Response<Transfer> response) {
                try {
                    Log.d(TAG, "onResponse: ");
                    progressDialog.dismiss();

                    if (response.code() == 200) {
                        transfer = response.body();
                        Log.d(TAG, "onResponse: transfer = " + transfer);
                        user.setBalance(transfer.getBalance());
                        preferences.putUser(user);
                        alertDialogBuilder.setMessage(transfer.getMessage()).show();
                    } else {
                        WalletUtil.showSnackbar(transferButton, getString(R.string.something_went_wrong));
                        Log.e(TAG, "onResponse: response.code() = " + response.code());
                    }
                } catch (Exception e) {
                    WalletUtil.showSnackbar(transferButton, getString(R.string.something_went_wrong));
                    Log.e(TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<Transfer> call, Throwable t) {
                try {
                    progressDialog.dismiss();
                    WalletUtil.showSnackbar(transferButton, getString(R.string.unable_to_connect));
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                }
            }
        });
    }
}
