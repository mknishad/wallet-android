package com.wallet.ui.activity;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;
import com.wallet.model.RegisterRequest;
import com.wallet.networking.ApiClient;
import com.wallet.networking.ApiInterface;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @OnClick(R.id.registerButton)
    public void onViewClicked() {
        //finish();

        String phone = phoneNumberWrapper.getEditText().getText().toString().trim();
        String name = nameWrapper.getEditText().getText().toString().trim();
        String email = emailWrapper.getEditText().getText().toString().trim();
        String password = passwordWrapper.getEditText().getText().toString().trim();

        RegisterRequest registerRequest = new RegisterRequest(phone, name, email, password);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<RegisterRequest>> registerCall = apiInterface.register(registerRequest);
        registerCall.enqueue(new Callback<ArrayList<RegisterRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<RegisterRequest>> call, Response<ArrayList<RegisterRequest>> response) {
                //progressDialog.dismiss();
                //ArrayList<RegisterRequest> registerRequests = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<RegisterRequest>> call, Throwable t) {
                //progressDialog.dismiss();
            }
        });
    }
}
