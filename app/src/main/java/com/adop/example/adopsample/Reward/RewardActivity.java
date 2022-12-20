package com.adop.example.adopsample.Reward;

import ad.helper.openbidding.reward.BidmadRewardAd;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.adop.adapter.fc.reward.RewardAdColony;
import com.adop.adapter.fnc.reward.RewardPubMatic;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.reward.RewardListener;

public class RewardActivity extends AppCompatActivity {

    BidmadRewardAd mReward;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        callbackStatus = findViewById(R.id.rewardCallbackStatus);

//        BidmadRewardAd.setAutoReload(false);
        //Require
        mReward = new BidmadRewardAd(this,"7d9a2c9e-5755-4022-85f1-6d4fc79e4418");
        mReward.setRewardListener(new RewardListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onShowAd() {
                callbackStatus.append("onShowAd() Called\n");
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
                callbackStatus.append("onLoadFailAd() Called\n");
            }

            @Override
            public void onCompleteAd() {
                callbackStatus.append("onCompleteAd() Called\n");
            }

            @Override
            public void onCloseAd() {
                callbackStatus.append("onCloseAd() Called\n");
            }

            @Override
            public void onClickAd() {
                callbackStatus.append("onSkippedAd() Called\n");
            }

            @Override
            public void onSkipAd() {

            }


        });
        
        //Option(Use when needed)
//        mReward.setChildDirected(true); //COPPA
//        mReward.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.
//        mReward.setMute(true); //Only some networks are supported

        mReward.load();

        findViewById(R.id.showReward).setOnClickListener(v -> {
            mReward.show();
        });
    }
}