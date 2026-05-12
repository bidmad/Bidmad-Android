package com.adop.example.adopsample.Consumable;

import ad.helper.openbidding.fullscreenad.BidmadFullScreenAd;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.fullscreenad.FullScreenAdListener;

import java.util.ArrayList;
import java.util.List;

public class FullscreenAd implements FullScreenAdListener {
    private static final String TAG = "FullscreenAd";
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public final String zoneId;
    public final BidmadFullScreenAd ad;

    private boolean isLoaded = false;
    private boolean isLoading = false;
    private BMAdInfo loadedInfo;
    private final List<AdCallback<BMAdInfo>> loadCallbacks = new ArrayList<>();
    private AdCallback<BidmadFullScreenAd> showCallback;
    private FullscreenAd holdUntilClose;

    public FullscreenAd(Activity activity, String zoneId) {
        this.zoneId = zoneId;
        this.ad = new BidmadFullScreenAd(activity, zoneId);
        BidmadFullScreenAd.setAutoReload(false);
        this.ad.setListener(this);
        Log.d(TAG, "init[" + zoneId + "] " + this);
    }

    public boolean isLoaded() {
        return isLoaded;
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

    public void show(final Activity activity,
                     final AdCallback<BidmadFullScreenAd> callback) {
        onMain(() -> {
            if (showCallback != null) {
                callback.onFailure(new BMAdError(-2));
                return;
            }
            if (isLoaded) {
                showCallback = callback;
                holdUntilClose = this;
                ad.show();
                return;
            }
            load(new AdCallback<BMAdInfo>() {
                @Override
                public void onSuccess(BMAdInfo info) {
                    showCallback = callback;
                    holdUntilClose = FullscreenAd.this;
                    ad.show();
                }

                @Override
                public void onFailure(BMAdError error) {
                    callback.onFailure(error);
                }
            });
        });
    }

    @Override
    public void onLoadAd(BMAdInfo info) {
        onMain(() -> {
            isLoading = false;
            isLoaded = true;
            loadedInfo = info;
            List<AdCallback<BMAdInfo>> pending = new ArrayList<>(loadCallbacks);
            loadCallbacks.clear();
            for (AdCallback<BMAdInfo> cb : pending) cb.onSuccess(info);
        });
    }

    @Override
    public void onLoadFailAd(BMAdError error) {
        onMain(() -> {
            isLoading = false;
            isLoaded = false;
            loadedInfo = null;
            List<AdCallback<BMAdInfo>> pending = new ArrayList<>(loadCallbacks);
            loadCallbacks.clear();
            for (AdCallback<BMAdInfo> cb : pending) cb.onFailure(error);
        });
    }

    @Override
    public void onShowAd(BMAdInfo info) {
        onMain(() -> {
            isLoaded = false;
            loadedInfo = null;
            AdCallback<BidmadFullScreenAd> cb = showCallback;
            showCallback = null;
            if (cb != null) cb.onSuccess(ad);
        });
    }

    @Override
    public void onShowFailAd(BMAdError error, BMAdInfo info) {
        onMain(() -> {
            isLoaded = false;
            loadedInfo = null;
            AdCallback<BidmadFullScreenAd> cb = showCallback;
            showCallback = null;
            if (cb != null) cb.onFailure(error);
            holdUntilClose = null;
        });
    }

    @Override
    public void onClickAd(BMAdInfo info) {
        Log.d(TAG, "click[" + zoneId + "] " + info);
    }

    @Override
    public void onCompleteAd(BMAdInfo info) {
        Log.d(TAG, "complete[" + zoneId + "] " + info);
    }

    @Override
    public void onSkipAd(BMAdInfo info) {
        Log.d(TAG, "skip[" + zoneId + "] " + info);
    }

    @Override
    public void onCloseAd(BMAdInfo info) {
        Log.d(TAG, "close[" + zoneId + "] " + info);
        onMain(() -> holdUntilClose = null);
    }

    private static void onMain(Runnable block) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block.run();
        } else {
            MAIN.post(block);
        }
    }
}
