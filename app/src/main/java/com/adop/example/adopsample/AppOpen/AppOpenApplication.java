package com.adop.example.adopsample.AppOpen;

import ad.helper.openbidding.BidmadCommon;
import ad.helper.openbidding.appopen.BidmadAppOpenAd;
import ad.helper.openbidding.initialize.BidmadInitializeListener;

import android.app.Application;
import android.util.Log;

import com.adop.sdk.BMAdError;
import com.adop.sdk.appopen.AppOpenListener;

public class AppOpenApplication extends  Application {
    BidmadAppOpenAd mAppOpen;
    @Override
    public void onCreate() {
        super.onCreate();
        BidmadCommon.setDebugging(true);
        BidmadCommon.initializeSdk(this, new BidmadInitializeListener() {
            @Override
            public void onInitialized(boolean isComplete) {
				init();
            }
        });
    }

    public void init() {
        mAppOpen = new BidmadAppOpenAd(this, "33906f96-dae8-4790-8ce4-d1f287ba00b2");

        mAppOpen.setAppOpenListener(new AppOpenListener() {
            @Override
            public void onLoadAd() {
                mAppOpen.adShow();
            }
            @Override
            public void onShowAd() {
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
            }

            @Override
            public void onShowFailAd(BMAdError bmAdError) {
            }

            @Override
            public void onCloseAd() {
            }

            @Override
            public void onExpireAd() {
                mAppOpen.adLoad();
            }
        });

        mAppOpen.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("release ttt11", "onTerminate");
        mAppOpen.end();
    }
}
