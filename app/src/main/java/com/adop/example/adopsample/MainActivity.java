package com.adop.example.adopsample;

import ad.helper.openbidding.BidmadCommon;
import ad.helper.openbidding.initialize.BidmadInitializeListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.adop.example.adopsample.AppOpen.AppOpenActivity;
import com.adop.example.adopsample.Banner.BannerActivity;
import com.adop.example.adopsample.Consumable.ConsumableExampleActivity;
import com.adop.example.adopsample.Consumable.ConsumableExampleActivity.Consumable;
import com.adop.example.adopsample.Interstitial.InterstitialActivity;
import com.adop.example.adopsample.Native.NativeActivity;
import com.adop.example.adopsample.Native.NativeCardListActivity;
import com.adop.example.adopsample.Native.NativeSmallCardListActivity;
import com.adop.example.adopsample.Reward.RewardActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final List<String> FULLSCREEN_ZONE_IDS = Arrays.asList(
            "b9992eb6-e8fc-41f4-a63e-a86c63730a10",
            "b9992eb6-e8fc-41f4-a63e-a86c63730a10",
            "b9992eb6-e8fc-41f4-a63e-a86c63730a10",
            "b9992eb6-e8fc-41f4-a63e-a86c63730a10"
    );

    private static final List<String> BANNER_ZONE_IDS = Arrays.asList(
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Log.d("Bidmad","MainActivity onCreate");

        BidmadCommon.setDebugging(true);
        BidmadCommon.initializeSdk(this, new BidmadInitializeListener() {
            @Override
            public void onInitialized(boolean isComplete) {
                Log.d("Bidmad", "initializeSdk onInitialized=" + isComplete);
                if (isComplete) {
                    Consumable.shared.load(
                            MainActivity.this,
                            FULLSCREEN_ZONE_IDS,
                            BANNER_ZONE_IDS);
                }
            }
        });

        findViewById(R.id.goBannerSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BannerActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goNativeSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NativeActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goNativeCardListSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NativeCardListActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goNativeSmallCardListSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NativeSmallCardListActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goInterstitialSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InterstitialActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goRewardSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RewardActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goAppOpenSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AppOpenActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goConsumableSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ConsumableExampleActivity.class);
                startActivity(i);
            }
        });
    }
}
