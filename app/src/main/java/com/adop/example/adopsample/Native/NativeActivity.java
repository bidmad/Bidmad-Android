package com.adop.example.adopsample.Native;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adop.example.adopsample.R;
import com.adop.sdk.nativead.BaseNativeAd;
import com.adop.sdk.nativead.CustomNativeAdLayout;
import com.adop.sdk.nativead.NativeListener;

public class NativeActivity extends AppCompatActivity {

    CustomNativeAdLayout layoutNative;
    BaseNativeAd nativeAd;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        callbackStatus = findViewById(R.id.nativeCallbackStatus);

        //Require
        nativeAd = new BaseNativeAd(this);
        nativeAd.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
        layoutNative = findViewById(R.id.native_ad_container);
        nativeAd.setNativeAdContainer(layoutNative, R.layout.newslist_native_item_ad);
        nativeAd.setNativeAdListener(new NativeListener() {
            @Override
            public void onSuccessHouseAd() {
                callbackStatus.append("onSuccessHouseAd() Called\n");
            }

            @Override
            public void onSuccessAd() {
                callbackStatus.append("onSuccessAd() Called\n");
            }

            @Override
            public void onFailedAd() {
                callbackStatus.append("onFailedAd() Called\n");
            }

            @Override
            public void onClickedAd(){
                callbackStatus.append("onClickedAd() Called\n");
            }
        });

        nativeAd.registerViewForInteraction(
                R.id.mediaView_common,
                R.id.mediaView,
                R.id.img_icon_common,
                R.id.img_icon,
                R.id.txt_body,
                R.id.txt_title,
                R.id.adCallToActionButton,
                R.id.adChoicesContainer,
                R.id.img_icon_privacy);

        //Option HouseAd Setting(Use when needed)
//        nativeAd.setChildDirected(true); //COPPA

        nativeAd.load();

        layoutNative.addView(nativeAd);
    }
}
