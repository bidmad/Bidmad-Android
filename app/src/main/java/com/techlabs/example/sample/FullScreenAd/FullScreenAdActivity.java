package com.techlabs.example.sample.FullScreenAd;

import ad.helper.openbidding.fullscreenad.BidmadFullScreenAd;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.techlabs.example.sample.BaseActivity;
import com.techlabs.example.sample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.fullscreenad.FullScreenAdListener;

public class FullScreenAdActivity extends BaseActivity {

    BidmadFullScreenAd mFullScreenAd;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fullscreen);
        super.onCreate(savedInstanceState);

        callbackStatus = findViewById(R.id.fullScreenAdCallbackStatus);

//        BidmadFullScreenAd.setAutoReload(false);
        //Require
        mFullScreenAd = new BidmadFullScreenAd(this,"452023b1-bacc-42c7-a914-5bb52bd82403");
        mFullScreenAd.setListener(new FullScreenAdListener() {
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
            public void onCompleteAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onCompleteAd() Called\n");
            }

            @Override
            public void onCloseAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onCloseAd() Called\n");
            }

            @Override
            public void onClickAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onClickAd() Called\n");
            }

            @Override
            public void onSkipAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onSkippedAd() Called\n");
            }


        });

        mFullScreenAd.load();

        findViewById(R.id.showFullScreenAd).setOnClickListener(v -> {
            mFullScreenAd.show();
        });
    }
}
