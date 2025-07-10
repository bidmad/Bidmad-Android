package com.adop.example.adopsample.AppOpen;

import ad.helper.openbidding.BidmadCommon;
import ad.helper.openbidding.appopen.BidmadAppOpenAd;
import ad.helper.openbidding.initialize.BidmadInitializeListener;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.util.Log;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.appopen.AppOpenListener;

public class AppOpenApplication extends Application implements DefaultLifecycleObserver {
    BidmadAppOpenAd mAppOpen;
    @Override
    public void onCreate() {
        super.onCreate();
        BidmadCommon.setDebugging(true);
        BidmadCommon.initializeSdk(this);
        init();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void init() {
        mAppOpen = new BidmadAppOpenAd(
                this,
                "33906f96-dae8-4790-8ce4-d1f287ba00b2"
        );

        mAppOpen.setAppOpenListener(new AppOpenListener() {
            @Override
            public void onLoadAd(@NonNull BMAdInfo info) {
                mAppOpen.adShow();
            }
            @Override
            public void onShowAd(@NonNull BMAdInfo info) {
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
            }

            @Override
            public void onShowFailAd(BMAdError bmAdError, @NonNull BMAdInfo info) {
            }

            @Override
            public void onCloseAd(@NonNull BMAdInfo info) {
            }

            @Override
            public void onExpireAd(@NonNull BMAdInfo info) {
                mAppOpen.adLoad();
            }
        });
    }



    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStop(owner);

        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);

        // If the advertisement operates only when the app
        // Continues depending on app activation/deactivation, move the code below to when the app is closed.
        mAppOpen.destory();
        mAppOpen = null;
    }
}
