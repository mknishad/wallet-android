package com.wallet.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.wallet.R;
import com.wallet.model.QrTransfer;
import com.wallet.model.User;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;
import com.wallet.sharedpreference.WalletPreferences;
import com.wallet.ui.activity.QrCodeActivity;
import com.wallet.util.Constant;
import com.wallet.util.WalletUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrTransferRedeemFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private static final String TAG = "QrTransferReceiveFragme";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1000;

    private Context context;
    private WalletPreferences preferences;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private User user;
    private QrTransfer qrTransfer;
    private ZXingScannerView zXingScannerView;

    public QrTransferRedeemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        zXingScannerView = new ZXingScannerView(getActivity());
        zXingScannerView.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary,
                null));

        init();

        return zXingScannerView;
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

        alertDialogBuilder = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> getActivity().finish())
                .setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        Log.e(TAG, "setUserVisibleHint");

        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onVisible();
        }
    }

    private void onVisible() {
        Log.e(TAG, "onVisible");

        if (!getUserVisibleHint()) {
            return;
        }

        checkForCameraPermission();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!WalletUtil.hasInternetConnection(context)) {
            WalletUtil.showSnackbar(zXingScannerView, getString(R.string.no_internet_connection));
            Log.e(TAG, "getUserStateFromServer: " + getString(R.string.no_internet_connection));
            resumeQrScanner();
            return;
        }

        progressDialog.show();
        String qrCode = rawResult.getText();
        Log.d(TAG, "handleResult: qrCode = " + qrCode);

        Call<QrTransfer> qrTransferCall = apiInterface.redeemQr(
                Constant.BEARER + preferences.getAuthToken(),
                user.get_id(), qrCode);
        qrTransferCall.enqueue(new Callback<QrTransfer>() {
            @Override
            public void onResponse(Call<QrTransfer> call, Response<QrTransfer> response) {
                try {
                    Log.d(TAG, "onResponse: ");
                    progressDialog.dismiss();

                    if (response.code() == 200) {
                        qrTransfer = response.body();
                        Log.d(TAG, "onResponse: qrTransfer = " + qrTransfer);
                        user.setBalance(qrTransfer.getBalance());
                        preferences.putUser(user);
                        alertDialogBuilder.setMessage(qrTransfer.getMessage()).show();
                    } else {
                        WalletUtil.showSnackbar(zXingScannerView, getString(R.string.something_went_wrong));
                        Log.e(TAG, "onResponse: response.code() = " + response.code());
                        resumeQrScanner();
                    }
                } catch (Exception e) {
                    WalletUtil.showSnackbar(zXingScannerView, getString(R.string.something_went_wrong));
                    Log.e(TAG, "onResponse: ", e);
                    resumeQrScanner();
                }
            }

            @Override
            public void onFailure(Call<QrTransfer> call, Throwable t) {
                try {
                    progressDialog.dismiss();
                    WalletUtil.showSnackbar(zXingScannerView, getString(R.string.unable_to_connect));
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                    resumeQrScanner();
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(), e);
                    resumeQrScanner();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    private void checkForCameraPermission() {
        Log.e(TAG, "checkForCameraPermission");

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

            // MY_PERMISSIONS_REQUEST_CAMERA is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            Log.e(TAG, "already have permission");

            zXingScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "permission was granted");

                    zXingScannerView.startCamera();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void resumeQrScanner() {
        zXingScannerView.resumeCameraPreview(QrTransferRedeemFragment.this);
    }
}
