package com.adop.example.adopsample.AppOpen;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adop.example.adopsample.R;
import com.adop.sdk.LogUtil;
import com.adop.sdk.appopen.AppOpenListener;
import com.adop.sdk.appopen.BaseAppOpen;
import com.adop.sdk.appopen.BaseAppOpenManager;

public class AppOpenAdvancedActivity extends AppCompatActivity implements LifecycleObserver, Application.ActivityLifecycleCallbacks{

    BaseAppOpen mAppOpen;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appopen);

        callbackStatus = findViewById(R.id.appopenCallbackStatus);

        this.getApplication().registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        findViewById(R.id.goBackGround).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.getApplication().unregisterActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callbackStatus.append("onStart() Called\n");
        if(mAppOpen != null) {
            mAppOpen.load();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        callbackStatus.append("onActivityResumed() Called\n");
        if(mAppOpen == null) {
            mAppOpen = new BaseAppOpen(this);
            mAppOpen.setAdInfo("YOUR ZONE ID");
            mAppOpen.setOrientation(BaseAppOpen.ORIENTATION_PORTRAIT);
            mAppOpen.setAppOpenListener(new AppOpenListener() {
                public void onLoadAd() {
                    callbackStatus.append("onLoadAd() Called\n");
                    mAppOpen.show();
                }
                @Override
                public void onShowAd() {
                    callbackStatus.append("onShowAd() Called\n");
                }
                @Override
                public void onFailedAd() {
                    callbackStatus.append("onFailedAd() Called\n");
                }
                @Override
                public void onCloseAd() {
                    callbackStatus.append("onCloseAd() Called\n");
                }
                @Override
                public void onExpired() {
                    callbackStatus.append("onExpired() Called\n");
                    mAppOpen.load();
                }
            });
        }else if(mAppOpen != null) {
            mAppOpen.setActivity(activity);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
