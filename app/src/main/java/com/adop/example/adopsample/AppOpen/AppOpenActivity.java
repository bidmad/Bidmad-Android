package com.adop.example.adopsample.AppOpen;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.R;
import com.adop.sdk.appopen.AppOpenLifecycleListener;
import com.adop.sdk.appopen.AppOpenListener;
import com.adop.sdk.appopen.BaseAppOpen;
import com.adop.sdk.appopen.BaseAppOpenManager;

import org.jetbrains.annotations.NotNull;

public class AppOpenActivity extends AppCompatActivity {

    BaseAppOpenManager mAppOpen;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appopen);

        callbackStatus = findViewById(R.id.appopenCallbackStatus);

        mAppOpen = new BaseAppOpenManager(this.getApplication(), "YOUR ZONE ID", BaseAppOpen.ORIENTATION_PORTRAIT);
        mAppOpen.setAppOpenListener(new AppOpenListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
                mAppOpen.adShow();
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
            public void onExpired() {
                callbackStatus.append("onExpired() Called\n");
                mAppOpen.adLoad();
            }
        });

        mAppOpen.start();

        findViewById(R.id.goBackGround).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAppOpen.end();
    }
}
