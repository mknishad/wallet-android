package com.wallet.ui.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;
import com.wallet.model.QrTransfer;
import com.wallet.model.User;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;
import com.wallet.sharedpreference.WalletPreferences;
import com.wallet.ui.activity.QrCodeActivity;
import com.wallet.util.Constant;
import com.wallet.util.WalletUtil;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrTransferCreateFragment extends Fragment {

    @BindView(R.id.phoneNumberWrapper)
    TextInputLayout phoneNumberWrapper;
    @BindView(R.id.amountWrapper)
    TextInputLayout amountWrapper;
    @BindView(R.id.pinCodeWrapper)
    TextInputLayout pinCodeWrapper;
    @BindView(R.id.generateQrButton)
    Button generateQrButton;
    Unbinder unbinder;

    private static final String TAG = "QrTransferCreateFragmen";

    private View rootView;
    private Context context;
    private WalletPreferences preferences;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private User user;
    private QrTransfer qrTransfer;

    public QrTransferCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qr_transfer_create, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        init();

        return rootView;
    }

    private void init() {
        context = getActivity();
        preferences = new WalletPreferences(context);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = preferences.getUser();
        qrTransfer = new QrTransfer();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.generateQrButton)
    public void onViewClicked() {
        generateQr();
    }

    private void generateQr() {
        if (!WalletUtil.hasInternetConnection(context)) {
            WalletUtil.showSnackbar(generateQrButton, getString(R.string.no_internet_connection));
            Log.e(TAG, "getUserStateFromServer: " + getString(R.string.no_internet_connection));
            return;
        }

        String phoneNumber = phoneNumberWrapper.getEditText().getText().toString().trim();
        String amount = amountWrapper.getEditText().getText().toString().trim();
        String pinCode = pinCodeWrapper.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            WalletUtil.showSnackbar(generateQrButton, getString(R.string.enter_phone_number));
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            WalletUtil.showSnackbar(generateQrButton, getString(R.string.enter_amount));
            return;
        }
        if (TextUtils.isEmpty(pinCode)) {
            WalletUtil.showSnackbar(generateQrButton, getString(R.string.enter_pin_code));
            return;
        }

        qrTransfer.setMobileNumber(phoneNumber);
        qrTransfer.setAmount(amount);
        qrTransfer.setPinCode(pinCode);

        progressDialog.show();
        Call<QrTransfer> qrTransferCall = apiInterface.createQr(
                Constant.BEARER + preferences.getAuthToken(),
                user.get_id(), qrTransfer);
        qrTransferCall.enqueue(new Callback<QrTransfer>() {
            @Override
            public void onResponse(Call<QrTransfer> call, Response<QrTransfer> response) {
                try {
                    Log.d(TAG, "onResponse: ");
                    progressDialog.dismiss();

                    if (response.code() == 200) {
                        qrTransfer = response.body();
                        Log.d(TAG, "onResponse: qrTransfer = " + qrTransfer);
                        startActivity(new Intent(getActivity(), QrCodeActivity.class)
                                .putExtra(Constant.QR_CODE, qrTransfer.getQrCode()));
                    } else {
                        WalletUtil.showSnackbar(generateQrButton, getString(R.string.something_went_wrong));
                        Log.e(TAG, "onResponse: response.code() = " + response.code());
                    }
                } catch (Exception e) {
                    WalletUtil.showSnackbar(generateQrButton, getString(R.string.something_went_wrong));
                    Log.e(TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<QrTransfer> call, Throwable t) {
                try {
                    progressDialog.dismiss();
                    WalletUtil.showSnackbar(generateQrButton, getString(R.string.unable_to_connect));
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                }
            }
        });
    }
}
