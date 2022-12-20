package com.adop.example.adopsample.Banner;

import ad.helper.openbidding.adview.BidmadBannerAd;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.adview.AdViewListener;

public class BannerActivity extends AppCompatActivity {

    ConstraintLayout layout;
    BidmadBannerAd mAdView;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        callbackStatus = findViewById(R.id.bannerCallbackStatus);

        //Require
        mAdView = new BidmadBannerAd(this, "944fe870-fa3a-4d1b-9cc2-38e50b2aed43");
        mAdView.setAdViewListener(new AdViewListener() {

            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
                callbackStatus.append("onLoadFailAd() Called\n");
            }

            @Override
            public void onClickAd() {
                callbackStatus.append("onClickAd() Called\n");
            }
        });

        //Option(Use when needed)
//        mAdView.setChildDirected(true); //COPPA
//        mAdView.setInterval(120); //Refresh Interval 60~120s
//        mAdView.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.

        mAdView.load(); //Banner Ad Load

        layout = findViewById(R.id.bannerLayout);
        layout.addView(mAdView.getView()); //attach Banner
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
