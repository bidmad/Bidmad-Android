package com.adop.example.adopsample.Interstitial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.R;
import com.adop.sdk.interstitial.BaseInterstitial;
import com.adop.sdk.interstitial.InterstitialListener;

public class InterstitialActivity extends AppCompatActivity {

    BaseInterstitial mInterstitial;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        callbackStatus = findViewById(R.id.interstitialCallbackStatus);

        //Require
        mInterstitial = new BaseInterstitial(this);
        mInterstitial.setAdInfo("YOUR ZONE ID");//ADOP ZONE ID Setting
        mInterstitial.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd() {
                callbackStatus.append("onShowAd() Called\n");
                mInterstitial.load(); //Ad Reload
            }

            @Override
            public void onFailedAd() {
                callbackStatus.append("onFailedAd() Called\n");
            }

            @Override
            public void onCloseAd() {
                callbackStatus.append("onCloseAd() Called\n");
            }
        });

        //Option(Use when needed)
//        mInterstitial.setChildDirected(true); //COPPA

        mInterstitial.load();

        findViewById(R.id.showIntersitial).setOnClickListener(v -> {
            if(mInterstitial.isLoaded()){
                mInterstitial.show();
            }
        });
    }
}
