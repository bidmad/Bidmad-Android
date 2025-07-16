package com.adop.example.adopsample;

import ad.helper.openbidding.appopen.BidmadAppOpenAd;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.DefaultLifecycleObserver;

public class BaseActivity extends AppCompatActivity implements DefaultLifecycleObserver {
    BidmadAppOpenAd mAppOpen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= VERSION_CODES.VANILLA_ICE_CREAM) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }
    }
}
