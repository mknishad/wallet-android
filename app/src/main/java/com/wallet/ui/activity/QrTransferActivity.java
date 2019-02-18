package com.wallet.ui.activity;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.tabs.TabLayout;
import com.wallet.R;
import com.wallet.ui.adapter.FragmentCategoryAdapter;
import com.wallet.ui.adapter.FragmentHolder;
import com.wallet.ui.fragment.QrTransferSendFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QrTransferActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private FragmentCategoryAdapter fragmentCategoryAdapter;
    private List<FragmentHolder> fragmentHolderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_transfer);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.qr_transfer);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        fragmentHolderList = new ArrayList<>();
        fragmentHolderList.add(new FragmentHolder(new QrTransferSendFragment(), getString(R.string.send)));
        fragmentHolderList.add(new FragmentHolder(new QrTransferSendFragment(), getString(R.string.receive)));

        fragmentCategoryAdapter = new FragmentCategoryAdapter(getSupportFragmentManager(),
                fragmentHolderList);

        viewPager.setAdapter(fragmentCategoryAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }
}
