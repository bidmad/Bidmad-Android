package com.adop.example.adopsample.Native;

import ad.helper.openbidding.nativead.BidmadNativeAd;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.BaseActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.BMAdError;
import com.adop.sdk.BMAdInfo;
import com.adop.sdk.nativead.NativeListener;
import java.util.ArrayList;

public class NativeCardListActivity extends BaseActivity {
    private String LOG_TAG = "NativeCardListActivity";

    ListView listview;
    ListAdapter mAdapter;
    ArrayList<AdItem> itemList;

    private String zoneId = "2d04afb5-99e9-4739-9970-2303da2be24c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_native_list);
        super.onCreate(savedInstanceState);

        mAdapter = new ListAdapter();
        itemList = new ArrayList<>();
        itemList.add(new AdItem(zoneId));
        itemList.add(new AdItem(zoneId));
        itemList.add(new AdItem(zoneId));
        itemList.add(new AdItem(zoneId));

        listview = (ListView) findViewById(R.id.native_ad_list_container);
        listview.setAdapter(mAdapter);
    }

    class ListAdapter extends BaseAdapter {
        public ListAdapter()
        {
        }
        public int getCount() {
            return itemList.size();
        }

        public AdItem getItem(int position) {
            return itemList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(LOG_TAG, "getView("+position+") Called\n");

            return getItem(position).load();
        }
    }

    class AdItem {
        private boolean isLoadedCall= false;
        private String mZoneId;
        private View adView;
        private BidmadNativeAd nativeAd;
        private FrameLayout layoutNative;

        public AdItem(String zoneId) {
            mZoneId = zoneId;
            init();
        }

        private void init(){
            adView = View.inflate(NativeCardListActivity.this, R.layout.native_ad_view, null);

            nativeAd = new BidmadNativeAd(NativeCardListActivity.this, mZoneId);
            layoutNative = adView.findViewById(R.id.native_ad_container);

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
                public void onLoadAd(@NonNull BMAdInfo info) {
                    Log.d(LOG_TAG, "onLoadAd() Called\n");
                    layoutNative.addView(nativeAd.getNativeLayout());
                }

                @Override
                public void onLoadFailAd(BMAdError bmAdError) {
                    Log.d(LOG_TAG, "onLoadFailAd() Called\n");
                    isLoadedCall = false;
                }

                @Override
                public void onClickAd(@NonNull BMAdInfo info) {
                    Log.d(LOG_TAG, "onClickAd() Called\n");
                }
            });

            //Option HouseAd Setting(Use when needed)
            //        nativeAd.setChildDirected(true); //COPPA
        }

        public View load() {
            if(!isLoadedCall) {
                nativeAd.load();
                isLoadedCall = true;
            }

            return adView;
        }
    }
}
