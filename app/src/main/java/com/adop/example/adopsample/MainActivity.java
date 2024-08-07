package com.adop.example.adopsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.Banner.BannerActivity;
import com.adop.example.adopsample.Interstitial.InterstitialActivity;
import com.adop.example.adopsample.Native.NativeActivity;
import com.adop.example.adopsample.Native.NativeCardListActivity;
import com.adop.example.adopsample.Native.NativeSmallCardListActivity;
import com.adop.example.adopsample.Reward.RewardActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
