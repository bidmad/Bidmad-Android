package com.adop.example.adopsample.Banner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.adop.example.adopsample.R;
import com.adop.sdk.adview.AdViewListener;
import com.adop.sdk.adview.BaseAdView;

public class BannerActivity extends AppCompatActivity {

    ConstraintLayout layout;
    BaseAdView mAdView;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        callbackStatus = findViewById(R.id.bannerCallbackStatus);

        //Require
        mAdView = new BaseAdView(this);
        mAdView.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
        mAdView.setAdViewListener(new AdViewListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onFailedAd() {
                callbackStatus.append("onFailedAd() Called\n");
            }

            @Override
            public void onClickAd() {
                callbackStatus.append("onClickAd() Called\n");
            }
        });

        //Option(Use when needed)
//        mAdView.setChildDirected(true); //COPPA

//        mAdView.setInterval(120); //Refresh Interval 60~120s

        mAdView.load(); //Banner Ad Load
        layout = findViewById(R.id.bannerLayout);
        layout.addView(mAdView); //attach Banner
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView != null)
            mAdView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAdView != null)
            mAdView.onPause();
    }
}
