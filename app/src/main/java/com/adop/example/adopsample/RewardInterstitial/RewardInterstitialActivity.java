package com.adop.example.adopsample.RewardInterstitial;

import ad.helper.openbidding.rewardinterstitial.BidmadRewardInterstitialAd;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.rewardinterstitial.RewardInterstitialListener;

public class RewardInterstitialActivity extends AppCompatActivity {

    BidmadRewardInterstitialAd mRewardInterstitial;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardinterstitial);

        callbackStatus = findViewById(R.id.rewardInterstitialCallbackStatus);

//        BidmadRewardInterstitialAd.setAutoReload(false);
        //Require
        mRewardInterstitial = new BidmadRewardInterstitialAd(this, "bcea5bf7-4082-4691-9401-aeb062edfcb0");
        mRewardInterstitial.setRewardInterstitialListener(new RewardInterstitialListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd() {
                callbackStatus.append("onShowAd() Called\n");
            }
            @Override
            public void onFailedAd() {
                callbackStatus.append("onFailedAd() Called\n");

            }
            @Override
            public void onCloseAd() {
                callbackStatus.append("onCloseAd() Called\n");

            }
            @Override
            public void onSkipAd() {
                callbackStatus.append("onSkipAd() Called\n");

            }
            @Override
            public void onCompleteAd() {
                callbackStatus.append("onCompleteAd() Called\n");

            }
        });

//        Option(Use when needed)
//        mRewardInterstitial.setChildDirected(true); //COPPA
//        mRewardInterstitial.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.
//        mRewardInterstitial.setMute(true); //Only some networks are supported

        mRewardInterstitial.load();

        findViewById(R.id.popupCall).setOnClickListener(v -> {
            alertMessage();
        });
    }

    public void alertMessage(){
        AlertPopup ap = new AlertPopup(this, new AlertPopup.OnClickListener() {
            @Override
            public void OnNegativeButton() {

            }

            @Override
            public void OnPositiveButton() {
                if(mRewardInterstitial.isLoaded()){
                    mRewardInterstitial.show();
                }
            }
        });
        ap.show();
    }
}
