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
}
