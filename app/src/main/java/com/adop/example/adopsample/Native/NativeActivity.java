package com.adop.example.adopsample.Native;

import ad.helper.openbidding.nativead.BidmadNativeAd;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.nativead.NativeListener;

public class NativeActivity extends AppCompatActivity {

    FrameLayout layoutNative;
    BidmadNativeAd nativeAd;
    TextView callbackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        callbackStatus = findViewById(R.id.nativeCallbackStatus);
        layoutNative = findViewById(R.id.native_ad_container);

        //Require
        nativeAd = new BidmadNativeAd(this, "YOUR ZONE ID");

        nativeAd.setViewForInteraction(
                R.layout.native_large_ad,
                R.id.mediaView,
                R.id.img_icon,
                R.id.txt_body,
                R.id.txt_title,
                R.id.adCallToActionButton
        );

        nativeAd.setNativeListener(new NativeListener() {
            @Override
            public void onLoadAd() {
                layoutNative.removeAllViews();
                layoutNative.addView(nativeAd.getNativeLayout());
                callbackStatus.append("onLoadAd() Called\n");
            }

            @Override
            public void onFailedAd() {
                callbackStatus.append("onFailedAd() Called\n");
            }

            @Override
            public void onClickAd(){
                callbackStatus.append("onClickAd() Called\n");
            }
        });

        //Option HouseAd Setting(Use when needed)
//        nativeAd.setChildDirected(true); //COPPA
//        nativeAd.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.

        nativeAd.load();
    }
}
