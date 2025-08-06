package com.adop.example.adopsample.AppOpen;

import ad.helper.openbidding.BidmadCommon;
import ad.helper.openbidding.appopen.BidmadAppOpenAd;
import ad.helper.openbidding.initialize.BidmadInitializeListener;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.appopen.AppOpenListener;

public class AppOpenActivity extends BaseActivity {
    BidmadAppOpenAd mAppOpen;
    TextView callbackStatus;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_appopen);
        super.onCreate(savedInstanceState);
        initUI();
        initAd();
    }
    
    public void initUI() {
        callbackStatus = findViewById(R.id.appopenCallbackStatus);

        findViewById(R.id.appHideBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackStatus.append("app hide() Called\n");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.appOpenAdRestartBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAppOpen != null) {
                    return;
                }

                callbackStatus.append("ad restart() Called\n");
                initAd();
            }
        });

        findViewById(R.id.appOpenAdStop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAppOpen != null) {
                    callbackStatus.append("destory() Called\n");
                    mAppOpen.destory();
                    mAppOpen = null;
                }
            }
        });
    }

    public void initAd() {
        if(mAppOpen != null) {
            release();
        }

        mAppOpen = new BidmadAppOpenAd(
                this,
                "33906f96-dae8-4790-8ce4-d1f287ba00b2"
        );

        mAppOpen.setListener(new AppOpenListener() {
            @Override
            public void onLoadAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onLoadAd() Called\n");
                mAppOpen.adShow();
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
            public void onCloseAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onCloseAd() Called\n");
            }

            @Override
            public void onExpireAd(@NonNull BMAdInfo info) {
                callbackStatus.append("onExpireAd() Called\n");
                mAppOpen.adLoad();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release() {
        if(mAppOpen != null) {
            // Continues depending on app activation/deactivation, and executes the code below when the app terminates
            // Or call below code if you no longer use app open ads.
            mAppOpen.destory();
            mAppOpen = null;
        }
    }
}
