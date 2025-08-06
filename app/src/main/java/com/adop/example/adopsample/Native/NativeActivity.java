package com.adop.example.adopsample.Native;

import ad.helper.openbidding.nativead.BidmadNativeAd;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.nativead.NativeListener;

public class NativeActivity extends BaseActivity {

    FrameLayout layoutNative;
    BidmadNativeAd nativeAd;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_native);
        super.onCreate(savedInstanceState);

        callbackStatus = findViewById(R.id.nativeCallbackStatus);
        layoutNative = findViewById(R.id.native_ad_container);

        //Require
        nativeAd = new BidmadNativeAd(this, "2d04afb5-99e9-4739-9970-2303da2be24c");

        nativeAd.setViewForInteraction(
                R.layout.native_large_ad,
                R.id.mediaView,
                R.id.img_icon,
                R.id.txt_body,
                R.id.txt_title,
                R.id.adCallToActionButton
        );

        nativeAd.setListener(new NativeListener() {
            @Override
            public void onLoadAd(@NonNull BMAdInfo info) {
                layoutNative.removeAllViews();
                layoutNative.addView(nativeAd.getNativeLayout());
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onLoadFailAd(BMAdError bmAdError) {
                callbackStatus.append("onLoadFailAd() Called\n");
            }


            @Override
            public void onClickAd(@NonNull BMAdInfo info){
                callbackStatus.append("onClickAd() Called\n");
            }
        });

        nativeAd.load();
    }
}
