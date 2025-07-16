package com.adop.example.adopsample.Reward;

import ad.helper.openbidding.reward.BidmadRewardAd;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.reward.RewardListener;

public class RewardActivity extends BaseActivity {

    BidmadRewardAd mReward;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reward);
        super.onCreate(savedInstanceState);

        callbackStatus = findViewById(R.id.rewardCallbackStatus);

//        BidmadRewardAd.setAutoReload(false);
        //Require
        mReward = new BidmadRewardAd(this,"7d9a2c9e-5755-4022-85f1-6d4fc79e4418");
        mReward.setRewardListener(new RewardListener() {
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