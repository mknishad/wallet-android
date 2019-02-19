package com.wallet.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.wallet.R;
import com.wallet.ui.activity.QrCodeActivity;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrTransferSendFragment extends Fragment {

    @BindView(R.id.phoneNumberWrapper)
    TextInputLayout phoneNumberWrapper;
    @BindView(R.id.amountWrapper)
    TextInputLayout amountWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.generateQrButton)
    Button generateQrButton;
    Unbinder unbinder;

    private View rootView;

    public QrTransferSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qr_transfer_send, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.generateQrButton)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), QrCodeActivity.class));
    }
}
