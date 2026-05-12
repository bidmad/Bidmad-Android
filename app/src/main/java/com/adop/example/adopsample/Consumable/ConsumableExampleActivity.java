package com.adop.example.adopsample.Consumable;

import ad.helper.openbidding.adview.BidmadBannerAd;
import ad.helper.openbidding.fullscreenad.BidmadFullScreenAd;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.adview.AdViewListener;
import com.adop.sdk.fullscreenad.FullScreenAdListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumableExampleActivity extends BaseActivity {
    private static final String TAG = "ConsumableExample";

    private static final String FULLSCREEN_ZONE_ID = "b9992eb6-e8fc-41f4-a63e-a86c63730a10";
    private static final String BANNER_ZONE_ID = "944fe870-fa3a-4d1b-9cc2-38e50b2aed43";

    private LinearLayout bannerStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_consumable_example);
        super.onCreate(savedInstanceState);

        bannerStack = findViewById(R.id.consumableBannerStack);

        Button consumeFullscreen = findViewById(R.id.consumableShowFullscreen);
        Button consumeBanner = findViewById(R.id.consumableShowBanner);
        Button removeBanner = findViewById(R.id.consumableRemoveBanner);

        consumeFullscreen.setOnClickListener(v -> {
            String zoneId = FULLSCREEN_ZONE_ID;
            Consumable.shared.consumeFullscreenAd(zoneId, this,
                    new AdCallback<BidmadFullScreenAd>() {
                        @Override
                        public void onSuccess(BidmadFullScreenAd value) {
                            Log.d(TAG, "Fullscreen consumed for " + zoneId);
                        }
                        @Override
                        public void onFailure(BMAdError error) {
                            Log.d(TAG, "Fullscreen consume failed: " + error);
                        }
                    });
        });

        consumeBanner.setOnClickListener(v -> {
            String zoneId = BANNER_ZONE_ID;
            Consumable.shared.consumeBannerAd(zoneId, this,
                    new AdCallback<BannerAd>() {
                        @Override
                        public void onSuccess(BannerAd banner) {
                            bannerStack.addView(banner);
                            Log.d(TAG, "Banner consumed for " + zoneId);
                        }
                        @Override
                        public void onFailure(BMAdError error) {
                            Log.d(TAG, "Banner consume failed: " + error);
                        }
                    });
        });

        removeBanner.setOnClickListener(v -> {
            int count = bannerStack.getChildCount();
            if (count == 0) {
                Log.d(TAG, "No banner to remove");
                return;
            }
            View last = bannerStack.getChildAt(count - 1);
            bannerStack.removeView(last);
            Log.d(TAG, "Removed banner (remaining: " + bannerStack.getChildCount() + ")");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < bannerStack.getChildCount(); i++) {
            View child = bannerStack.getChildAt(i);
            if (child instanceof BannerAd) ((BannerAd) child).onResume();
        }
        for (Map.Entry<String, List<BannerAd>> entry : Consumable.shared.bannerAds.entrySet()) {
            for (BannerAd banner : entry.getValue()) banner.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < bannerStack.getChildCount(); i++) {
            View child = bannerStack.getChildAt(i);
            if (child instanceof BannerAd) ((BannerAd) child).onPause();
        }
        for (Map.Entry<String, List<BannerAd>> entry : Consumable.shared.bannerAds.entrySet()) {
            for (BannerAd banner : entry.getValue()) banner.onPause();
        }
    }

    // ===================== AdCallback =====================

    public interface AdCallback<T> {
        void onSuccess(T value);
        void onFailure(BMAdError error);
    }

    // ===================== onMain helper =====================

    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private static void onMain(Runnable block) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block.run();
        } else {
            MAIN.post(block);
        }
    }

    // ===================== FullscreenAd =====================

    public static class FullscreenAd implements FullScreenAdListener {
        private static final String TAG = "FullscreenAd";

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
    }

    // ===================== BannerAd =====================

    public static class BannerAd extends FrameLayout {
        private static final String TAG = "BannerAd";

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
    }

    // ===================== RootActivityTracker =====================

    public static final class RootActivityTracker {
        public interface Listener {
            void onRootDestroyed();
            void onRootRecreated(@NonNull Activity newRoot);
        }

        private static final String TAG = "RootActivityTracker";
        private static volatile RootActivityTracker instance;

        private final Application.ActivityLifecycleCallbacks callbacks;
        private WeakReference<Activity> currentRoot = new WeakReference<>(null);
        private String rootClassName;
        @Nullable
        private Listener listener;

        private RootActivityTracker(@NonNull Activity currentActivity) {
            currentRoot = new WeakReference<>(currentActivity);
            rootClassName = currentActivity.getClass().getName();
            Application application = currentActivity.getApplication();
            this.callbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    if (!activity.isTaskRoot()) return;
                    onRootCreated(activity);
                }

                @Override public void onActivityStarted(@NonNull Activity activity) {}
                @Override public void onActivityResumed(@NonNull Activity activity) {}
                @Override public void onActivityPaused(@NonNull Activity activity) {}
                @Override public void onActivityStopped(@NonNull Activity activity) {}
                @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    Activity current = currentRoot.get();
                    if (current != null && current == activity) {
                        onRootDestroyed(activity);
                    }
                }
            };
            application.registerActivityLifecycleCallbacks(callbacks);
            Log.d(TAG, "installed on " + application.getClass().getSimpleName());
        }

        @NonNull
        public static RootActivityTracker shared(@NonNull Activity currentActivity) {
            RootActivityTracker local = instance;
            if (local == null) {
                synchronized (RootActivityTracker.class) {
                    local = instance;
                    if (local == null) {
                        local = new RootActivityTracker(currentActivity);
                        instance = local;
                    }
                }
            }
            return local;
        }

        public synchronized void setListener(@Nullable Listener listener) {
            this.listener = listener;
        }

        @Nullable
        public Activity getCurrentRoot() {
            return currentRoot.get();
        }

        private synchronized void onRootCreated(Activity activity) {
            String className = activity.getClass().getName();
            boolean isRecreation = rootClassName != null
                    && currentRoot.get() == null
                    && className.equals(rootClassName);
            currentRoot = new WeakReference<>(activity);
            rootClassName = className;
            if (isRecreation && listener != null) {
                Log.d(TAG, "root RECREATED: " + className
                        + "@" + Integer.toHexString(System.identityHashCode(activity)));
                listener.onRootRecreated(activity);
            }
        }

        private synchronized void onRootDestroyed(Activity activity) {
            String hex = "@" + Integer.toHexString(System.identityHashCode(activity));
            Log.d(TAG, "root DESTROYED: " + activity.getClass().getName() + hex);
            currentRoot.clear();
            if (listener != null) {
                listener.onRootDestroyed();
            }
        }
    }

    // ===================== Consumable =====================

    public static final class Consumable {
        public static final Consumable shared = new Consumable();

        public final Map<String, List<FullscreenAd>> fullscreenAds = new HashMap<>();
        public final Map<String, List<BannerAd>> bannerAds = new HashMap<>();

        public List<String> fullscreenAdZoneIds = new ArrayList<>();
        public List<String> bannerAdZoneIds = new ArrayList<>();

        private RootActivityTracker tracker;
        private long loadGeneration = 0;
        private boolean hasLoaded = false;

        private Consumable() {}

        public void load(Activity activity,
                         List<String> fullscreenAdZoneIds,
                         List<String> bannerAdZoneIds) {
            onMain(() -> {
                if (hasLoaded) {
                    return;
                }
                hasLoaded = true;
                doLoad(activity, fullscreenAdZoneIds, bannerAdZoneIds);
            });
        }

        private void doLoad(Activity activity,
                            List<String> fullscreenAdZoneIds,
                            List<String> bannerAdZoneIds) {
            onMain(() -> {
                this.fullscreenAdZoneIds = fullscreenAdZoneIds != null
                        ? new ArrayList<>(fullscreenAdZoneIds) : new ArrayList<>();
                this.bannerAdZoneIds = bannerAdZoneIds != null
                        ? new ArrayList<>(bannerAdZoneIds) : new ArrayList<>();

                if (tracker == null) {
                    tracker = RootActivityTracker.shared(activity);
                    tracker.setListener(new RootActivityTracker.Listener() {
                        @Override
                        public void onRootDestroyed() {
                            handleRootDestroyed();
                        }
                        @Override
                        public void onRootRecreated(Activity newRoot) {
                            doLoad(newRoot,
                                    Consumable.this.fullscreenAdZoneIds,
                                    Consumable.this.bannerAdZoneIds);
                        }
                    });
                }

                long gen = ++loadGeneration;
                List<Task> tasks = new ArrayList<>();
                for (String id : this.fullscreenAdZoneIds) tasks.add(new Task(true, id));
                for (String id : this.bannerAdZoneIds) tasks.add(new Task(false, id));
                loadSequentially(activity, tasks, 0, gen);
            });
        }

        private void loadSequentially(Activity activity, List<Task> tasks, int index, long gen) {
            if (gen != loadGeneration) return;
            if (index >= tasks.size()) return;
            Task task = tasks.get(index);
            AdCallback<BMAdInfo> advance = new AdCallback<BMAdInfo>() {
                @Override
                public void onSuccess(BMAdInfo info) { schedule(); }
                @Override
                public void onFailure(BMAdError error) { schedule(); }
                private void schedule() {
                    MAIN.postDelayed(
                            () -> loadSequentially(activity, tasks, index + 1, gen),
                            1000);
                }
            };
            if (task.isFullscreen) {
                loadFullscreenAd(activity, task.zoneId, advance);
            } else {
                loadBannerAd(activity, task.zoneId, advance);
            }
        }

        private void handleRootDestroyed() {
            onMain(() -> {
                loadGeneration++;
                fullscreenAds.clear();
                for (List<BannerAd> pool : bannerAds.values()) {
                    if (pool == null) continue;
                    for (BannerAd banner : pool) {
                        if (banner != null && banner.ad != null) banner.ad.onPause();
                    }
                }
                bannerAds.clear();
            });
        }

        public void consumeFullscreenAd(final String zoneId,
                                        final Activity activity,
                                        final AdCallback<BidmadFullScreenAd> callback) {
            onMain(() -> {
                List<FullscreenAd> pool = fullscreenAds.get(zoneId);
                if (pool == null) pool = new ArrayList<>();
                int pickedIndex = -1;
                for (int i = 0; i < pool.size(); i++) {
                    if (pool.get(i).isLoaded()) { pickedIndex = i; break; }
                }
                if (pickedIndex == -1 && !pool.isEmpty()) pickedIndex = 0;

                if (pickedIndex != -1) {
                    final FullscreenAd wrapper = pool.remove(pickedIndex);
                    fullscreenAds.put(zoneId, pool);
                    wrapper.show(activity, new AdCallback<BidmadFullScreenAd>() {
                        @Override
                        public void onSuccess(BidmadFullScreenAd value) {
                            keepAlive(wrapper);
                            callback.onSuccess(value);
                        }
                        @Override
                        public void onFailure(BMAdError error) {
                            keepAlive(wrapper);
                            callback.onFailure(error);
                        }
                    });
                    loadFullscreenAd(activity, zoneId, NO_OP_LOAD);
                    return;
                }

                final FullscreenAd transient_ = new FullscreenAd(activity, zoneId);
                transient_.show(activity, new AdCallback<BidmadFullScreenAd>() {
                    @Override
                    public void onSuccess(BidmadFullScreenAd value) {
                        keepAlive(transient_);
                        callback.onSuccess(value);
                    }
                    @Override
                    public void onFailure(BMAdError error) {
                        keepAlive(transient_);
                        callback.onFailure(error);
                    }
                });
            });
        }

        public void consumeBannerAd(final String zoneId,
                                    final Activity activity,
                                    final AdCallback<BannerAd> callback) {
            onMain(() -> {
                List<BannerAd> pool = bannerAds.get(zoneId);
                if (pool == null) pool = new ArrayList<>();
                int pickedIndex = -1;
                for (int i = 0; i < pool.size(); i++) {
                    if (pool.get(i).isLoaded()) { pickedIndex = i; break; }
                }
                if (pickedIndex == -1 && !pool.isEmpty()) pickedIndex = 0;

                if (pickedIndex != -1) {
                    BannerAd wrapper = pool.remove(pickedIndex);
                    bannerAds.put(zoneId, pool);
                    wrapper.show(callback);
                    loadBannerAd(activity, zoneId, NO_OP_LOAD);
                    return;
                }

                BannerAd transient_ = new BannerAd(activity, zoneId);
                transient_.show(callback);
            });
        }

        private void loadFullscreenAd(Activity activity,
                                      String zoneId,
                                      AdCallback<BMAdInfo> callback) {
            onMain(() -> {
                FullscreenAd wrapper = new FullscreenAd(activity, zoneId);
                List<FullscreenAd> pool = fullscreenAds.get(zoneId);
                if (pool == null) {
                    pool = new ArrayList<>();
                    fullscreenAds.put(zoneId, pool);
                }
                pool.add(wrapper);
                wrapper.load(callback);
            });
        }

        private void loadBannerAd(Activity activity,
                                  String zoneId,
                                  AdCallback<BMAdInfo> callback) {
            onMain(() -> {
                BannerAd wrapper = new BannerAd(activity, zoneId);
                List<BannerAd> pool = bannerAds.get(zoneId);
                if (pool == null) {
                    pool = new ArrayList<>();
                    bannerAds.put(zoneId, pool);
                }
                pool.add(wrapper);
                wrapper.load(callback);
            });
        }

        @SuppressWarnings("unused")
        private static void keepAlive(Object o) {}

        private static final AdCallback<BMAdInfo> NO_OP_LOAD = new AdCallback<BMAdInfo>() {
            @Override public void onSuccess(BMAdInfo info) {}
            @Override public void onFailure(BMAdError error) {}
        };

        private static final class Task {
            final boolean isFullscreen;
            final String zoneId;
            Task(boolean isFullscreen, String zoneId) {
                this.isFullscreen = isFullscreen;
                this.zoneId = zoneId;
            }
        }
    }
}
