package com.techlabs.example.sample;

import ad.helper.openbidding.BidmadCommon;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.techlabs.example.sample.AppOpen.AppOpenActivity;
import com.techlabs.example.sample.Banner.BannerActivity;
import com.techlabs.example.sample.Interstitial.InterstitialActivity;
import com.techlabs.example.sample.Native.NativeActivity;
import com.techlabs.example.sample.Native.NativeCardListActivity;
import com.techlabs.example.sample.Native.NativeSmallCardListActivity;
import com.techlabs.example.sample.Reward.RewardActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Log.d("Bidmad","MainActivity onCreate");

//        BidmadCommon.setDebugging(true);
        BidmadCommon.initializeSdk(this);

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
    }
}
