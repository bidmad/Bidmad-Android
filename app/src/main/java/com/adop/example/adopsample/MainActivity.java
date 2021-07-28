package com.adop.example.adopsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.AppOpen.AppOpenActivity;
import com.adop.example.adopsample.AppOpen.AppOpenAdvancedActivity;
import com.adop.example.adopsample.Banner.BannerActivity;
import com.adop.example.adopsample.Interstitial.InterstitialActivity;
import com.adop.example.adopsample.Native.NativeActivity;
import com.adop.example.adopsample.Offerwall.OfferwallActivity;
import com.adop.example.adopsample.Reward.RewardActivity;
import com.adop.example.adopsample.RewardInterstitial.RewardInterstitialActivity;
import com.adop.sdk.Common;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Common.setDebugging(true);

        findViewById(R.id.goBannerSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BannerActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goNativeSampel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NativeActivity.class);
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

        findViewById(R.id.goOfferwallSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, OfferwallActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goRewardInterstitialSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RewardInterstitialActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goAppopenSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AppOpenActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goAppopenAdvancedSample).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AppOpenAdvancedActivity.class);
                startActivity(i);
            }
        });
    }
}
