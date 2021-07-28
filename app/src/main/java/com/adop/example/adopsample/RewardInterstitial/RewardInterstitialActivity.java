package com.adop.example.adopsample.RewardInterstitial;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.R;
import com.adop.sdk.rewardinterstitial.BaseRewardInterstitial;
import com.adop.sdk.rewardinterstitial.RewardInterstitialListener;

public class RewardInterstitialActivity extends AppCompatActivity {

    BaseRewardInterstitial mRewardInterstitial;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardinterstitial);

        callbackStatus = findViewById(R.id.rewardInterstitialCallbackStatus);

        //Require
        mRewardInterstitial = new BaseRewardInterstitial(this);
        mRewardInterstitial.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
        mRewardInterstitial.setRewardInterstitialListener(new RewardInterstitialListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd() {
                callbackStatus.append("onShowAd() Called\n");
                mRewardInterstitial.load(); //Ad Reload

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
        mRewardInterstitial.setChildDirected(true); //COPPA

        mRewardInterstitial.setMute(true); //Only some networks are supported

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
