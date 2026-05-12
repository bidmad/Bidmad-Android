package com.adop.example.adopsample.Consumable;

import ad.helper.openbidding.adview.BidmadBannerAd;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.adview.AdViewListener;

import java.util.ArrayList;
import java.util.List;

public class BannerAd extends FrameLayout {
    private static final String TAG = "BannerAd";
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public final String zoneId;
    public final BidmadBannerAd ad;

    private boolean isLoaded = false;
    private boolean isLoading = false;
    private BMAdInfo loadedInfo;
    private final List<AdCallback<BMAdInfo>> loadCallbacks = new ArrayList<>();

    public BannerAd(Activity activity, String zoneId) {
        super(activity);
        this.zoneId = zoneId;
        int heightPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 50,
                activity.getResources().getDisplayMetrics());
        setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, heightPx));
        this.ad = new BidmadBannerAd(activity, zoneId);
        this.ad.setListener(new AdViewListener() {
            @Override
            public void onLoadAd(BMAdInfo info) {
                BannerAd.this.handleLoadAd(info);
            }

            @Override
            public void onLoadFailAd(BMAdError error) {
                BannerAd.this.handleLoadFailAd(error);
            }

            @Override
            public void onClickAd(BMAdInfo info) {
                Log.d(TAG, "click[" + BannerAd.this.zoneId + "] " + info);
            }
        });
        addView(ad.getView());
        Log.d(TAG, "init[" + zoneId + "] " + this);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void onResume() {
        if (ad != null) ad.onResume();
    }

    public void onPause() {
        if (ad != null) ad.onPause();
    }

    public void load(final AdCallback<BMAdInfo> callback) {
        onMain(() -> {
            if (isLoaded && loadedInfo != null) {
                callback.onSuccess(loadedInfo);
                return;
            }
            loadCallbacks.add(callback);
            if (isLoading) return;
            isLoading = true;
            ad.load();
        });
    }

    public void show(final AdCallback<BannerAd> callback) {
        onMain(() -> {
            if (isLoaded) {
                callback.onSuccess(this);
                return;
            }
            load(new AdCallback<BMAdInfo>() {
                @Override
                public void onSuccess(BMAdInfo info) {
                    callback.onSuccess(BannerAd.this);
                }

                @Override
                public void onFailure(BMAdError error) {
                    callback.onFailure(error);
                }
            });
        });
    }

    private void handleLoadAd(BMAdInfo info) {
        onMain(() -> {
            isLoading = false;
            isLoaded = true;
            loadedInfo = info;
            List<AdCallback<BMAdInfo>> pending = new ArrayList<>(loadCallbacks);
            loadCallbacks.clear();
            for (AdCallback<BMAdInfo> cb : pending) cb.onSuccess(info);
        });
    }

    private void handleLoadFailAd(BMAdError error) {
        onMain(() -> {
            isLoading = false;
            isLoaded = false;
            loadedInfo = null;
            List<AdCallback<BMAdInfo>> pending = new ArrayList<>(loadCallbacks);
            loadCallbacks.clear();
            for (AdCallback<BMAdInfo> cb : pending) cb.onFailure(error);
        });
    }

    private static void onMain(Runnable block) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block.run();
        } else {
            MAIN.post(block);
        }
    }
}
