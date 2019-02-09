package com.wallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private String phoneNumber;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @OnClick({R.id.loginButton, R.id.registerTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                phoneNumber = phoneNumberWrapper.getEditText().getText().toString().trim();
                password = passwordWrapper.getEditText().getText().toString().trim();
                Log.d(TAG, "onViewClicked: phoneNumber = " + phoneNumber + " passowrd = " + password);
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
                break;
            case R.id.registerTextView:
                break;
        }
    }
}
