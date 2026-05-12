package com.adop.example.adopsample.Consumable;

import ad.helper.openbidding.fullscreenad.BidmadFullScreenAd;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConsumableExampleActivity extends BaseActivity {
    private static final String TAG = "ConsumableExample";

    private static final List<String> FULLSCREEN_ZONE_IDS = Arrays.asList(
            "dcd42036-e54c-4b63-bdce-295bbfdc2ed6",
            "dcd42036-e54c-4b63-bdce-295bbfdc2ed6",
            "dcd42036-e54c-4b63-bdce-295bbfdc2ed6",
            "dcd42036-e54c-4b63-bdce-295bbfdc2ed6"
    );

    private static final List<String> BANNER_ZONE_IDS = Arrays.asList(
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43",
            "944fe870-fa3a-4d1b-9cc2-38e50b2aed43"
    );

    private LinearLayout bannerStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_consumable_example);
        super.onCreate(savedInstanceState);

        bannerStack = findViewById(R.id.consumableBannerStack);

        Button start = findViewById(R.id.consumableStart);
        Button consumeFullscreen = findViewById(R.id.consumableShowFullscreen);
        Button consumeBanner = findViewById(R.id.consumableShowBanner);
        Button removeBanner = findViewById(R.id.consumableRemoveBanner);

        start.setOnClickListener(v -> {
            Consumable.shared.load(this, FULLSCREEN_ZONE_IDS, BANNER_ZONE_IDS);
            Log.d(TAG, "Started loading "
                    + FULLSCREEN_ZONE_IDS.size() + " fullscreen and "
                    + BANNER_ZONE_IDS.size() + " banner ads");
        });

        consumeFullscreen.setOnClickListener(v -> {
            String zoneId = FULLSCREEN_ZONE_IDS.get(0);
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
            String zoneId = BANNER_ZONE_IDS.get(0);
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
}
