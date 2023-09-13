package com.adop.example.adopsample.AppOpen;

import ad.helper.openbidding.appopen.BidmadAppOpenAd;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.appopen.AppOpenListener;
import org.jetbrains.annotations.NotNull;

public class AppOpenActivity extends Activity {

    BidmadAppOpenAd mAppOpen;
    TextView callbackStatus;

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appopen);

        callbackStatus = findViewById(R.id.appopenCallbackStatus);

        mAppOpen = new BidmadAppOpenAd(this.getApplication(), "33906f96-dae8-4790-8ce4-d1f287ba00b2");
        mAppOpen.setAppOpenListener(new AppOpenListener() {
            @Override
            public void onLoadAd() {
                callbackStatus.append("onLoadAd() Called\n");
                mAppOpen.adShow();
            }
            @Override
            public void onShowAd() {
                callbackStatus.append("onShowAd() Called\n");
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
                callbackStatus.append("onLoadFailAd() Called\n");
            }

            @Override
            public void onCloseAd() {
                callbackStatus.append("onCloseAd() Called\n");
            }

            @Override
            public void onExpireAd() {
                callbackStatus.append("onExpireAd() Called\n");
                mAppOpen.adLoad();
            }
        });

//        mAppOpen.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.
        mAppOpen.start();

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
        mAppOpen.end();
    }
}
