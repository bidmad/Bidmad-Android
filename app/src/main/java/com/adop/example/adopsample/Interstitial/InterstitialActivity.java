package com.adop.example.adopsample.Interstitial;

import ad.helper.openbidding.interstitial.BidmadInterstitialAd;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.interstitial.InterstitialListener;

public class InterstitialActivity extends BaseActivity {

    BidmadInterstitialAd mInterstitial;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_interstitial);
        super.onCreate(savedInstanceState);

        callbackStatus = findViewById(R.id.interstitialCallbackStatus);

//        BidmadInterstitialAd.setAutoReload(false);
        //Require
        mInterstitial = new BidmadInterstitialAd(this, "e9acd7fc-a962-40e4-aaad-9feab1b4f821");
        mInterstitial.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onLoadAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onShowAd() Called\n");
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
                callbackStatus.append("onLoadFailAd() Called\n");
            }

            @Override
            public void onShowFailAd(BMAdError bmAdError, @NonNull BMAdInfo info) {
                callbackStatus.append("onShowFailAd() Called\n");
            }

            @Override
            public void onClickAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onClickAd Called\n");
            }

            @Override
            public void onCloseAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onCloseAd() Called\n");
            }
        });

        //Option(Use when needed)
//        mInterstitial.setChildDirected(true); //COPPA
//        mInterstitial.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.

        mInterstitial.load();

        findViewById(R.id.showIntersitial).setOnClickListener(v -> {
            mInterstitial.show();
        });
    }
}
