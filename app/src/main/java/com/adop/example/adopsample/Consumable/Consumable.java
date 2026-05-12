package com.adop.example.adopsample.Consumable;

import ad.helper.openbidding.fullscreenad.BidmadFullScreenAd;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Consumable {
    public static final Consumable shared = new Consumable();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

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
        if (gen != loadGeneration) return; // a destroy or newer load() bumped the generation
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

    // No-op placeholder; keeping a captured wrapper reachable inside a lambda.
    @SuppressWarnings("unused")
    private static void keepAlive(Object o) {}

    private static final AdCallback<BMAdInfo> NO_OP_LOAD = new AdCallback<BMAdInfo>() {
        @Override public void onSuccess(BMAdInfo info) {}
        @Override public void onFailure(BMAdError error) {}
    };

    private static void onMain(Runnable block) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block.run();
        } else {
            MAIN.post(block);
        }
    }

    private static final class Task {
        final boolean isFullscreen;
        final String zoneId;
        Task(boolean isFullscreen, String zoneId) {
            this.isFullscreen = isFullscreen;
            this.zoneId = zoneId;
        }
    }
}
