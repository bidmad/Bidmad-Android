package com.adop.example.adopsample.Reward;

import ad.helper.openbidding.reward.BidmadRewardAd;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.reward.RewardListener;

public class RewardActivity extends AppCompatActivity {

    BidmadRewardAd mReward;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        callbackStatus = findViewById(R.id.rewardCallbackStatus);

        //Require
        mReward = new BidmadRewardAd(this,"YOUR ZONE ID");
        mReward.setRewardListener(new RewardListener() {
            public void onLoadAd(String zoneId) {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd(String zoneId) {
                callbackStatus.append("onShowAd() Called\n");
                mReward.load(); //Ad Reload
            }

            @Override
            public void onFailedAd(String zoneId) {
                callbackStatus.append("onFailedAd() Called\n");
            }

            @Override
            public void onCompleteAd(String zoneId) {
                callbackStatus.append("onCompleteAd() Called\n");
            }

            @Override
            public void onOpenAd(String zoneId) {
                callbackStatus.append("onOpenAd() Called\n");
            }

            @Override
            public void onCloseAd(String zoneId) {
                callbackStatus.append("onCloseAd() Called\n");
            }

            @Override
            public void onClickAd(String zoneId) {
                callbackStatus.append("onClickAd() Called\n");
            }

            @Override
            public void onSkippedAd(String zoneId) {
                callbackStatus.append("onSkippedAd() Called\n");
            }
        });

        //Option(Use when needed)
//        mReward.setChildDirected(true); //COPPA
//        mReward.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.
//        mReward.setMute(true); //Only some networks are supported

        mReward.load();

        findViewById(R.id.showReward).setOnClickListener(v -> {
            if(mReward.isLoaded()){
                mReward.show();
            }
        });
    }
}