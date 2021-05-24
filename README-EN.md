# BidmadSDK(v1.13.2)
### Shortcuts
1. [SDK Settings](#1-SDK-Settings)
    - [Gradle](#Gradle)
    - [AndroidManifest.xml](#AndroidManifest.xml)
2. [How to use](#2-How-to-use)
    - [Banner Ads](#Banner-Ads)
    - [Interstitial Ads](#Interstitial-Ads)
    - [Reward Ads](#Reward-Ads)
    - [Native Ads](#Native-Ads)
    - [Offerwall Ads](#Offerwall-Ads)
3. [Class Reference](#3-Class-Reference)
    - [Banner Class Reference](#Banner-Class-Reference)
    - [Interstitial Class Reference](#Interstitial-Class-Reference)
    - [Reward Class Reference](#Reward-Class-Reference)
    - [NativeAd Class Reference](#NativeAd-Class-Reference)
    - [Offerwall Class Reference](#Offerwall-Class-Reference)
4. [Note](#4-Note)
5. [Download the latest sample project](https://github.com/bidmad/Bidmad-Android/archive/master.zip)
---
### 1. SDK Settings
#### *Minimum requirements for using the SDK
- Gradle Plugin 3.5.4 or higher
- minSdkVersion 19 or higher

#### *Gradle
1. Declares the repository in the build.gradle file located at the top-level of the project.

```java
allprojects {
   repositories {
       ...
       google()
       jcenter()
       maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' } //Adift
       maven {
          url "s3://repo.cauly.net/releases"
          credentials(AwsCredentials) {
              accessKey "AKIAWRZUK5MFKYVSUOLB"
              secretKey "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"
          }
       } //Cauly
       maven { url "https://bidmad-sdk.s3.amazonaws.com/" } //bidmad
       maven { url "https://sdk.tapjoy.com/" } //Tapjoy

}
```
3. Declares SDK in dependencies of build.gradle file located in project App-Level.

```java
dependencies {
    ...
    implementation 'com.adop.sdk:bidmad-androidx:1.13.2'
}
```
4. Declare the options below in the android tag of the build.gradle file located in the project App-Level.

```java
android {
    ...
    defaultConfig {
        ...
        multiDexEnabled true
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

*Bidmad uses the AndroidX library. If it is not an AndroidX project, please migrate to AndroidX.

#### *AndroidManifest.xml

1. Declare the following code in the application tag of AndroidManifest.xml in the project.<br>
 *Please check the value of com.google.android.gms.ads.APPLICATION_ID on the Admob dashboard.

```xml
<application
   android:usesCleartextTraffic="true"
   ...
>
   ...
   <uses-library android:name="org.apache.http.legacy" android:required="false" />
   <activity android:name="com.google.android.gms.ads.AdActivity" android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"/>
   <meta-data android:name="android.webkit.WebView.EnableSafeBrowsing" android:value="false" />
   <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="APPLICATION_ID"/>
   ...
</application>
```
2. Declare permission in AndroidManifest.xml in project.

```xml
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

### 2. How to use

#### *Banner Ads

1. Add a layout to expose the banner.
```java
...
<androidx.constraintlayout.widget.ConstraintLayout
    android:id="@+id/bannerLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
...
```

2. To request a banner ad, create a BaseAdView, set the ZoneId, and call the load function.
3. Add a BaseAdView to the view created above to expose a banner ad.
```java
ConstraintLayout layout;
BaseAdView mAdView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    //Require
    mAdView = new BaseAdView(this);
    mAdView.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
    mAdView.setAdViewListener(new AdViewListener() {
        @Override
        public void onLoadAd() {
            //onLoad Callback
        }

        @Override
        public void onFailedAd() {
            //onFailedAd Callback
        }

        @Override
        public void onClickAd() {
            //onClickAd Callback
        }
    });

    // mAdView.setInterval(120); //Refresh Interval 60~120s
    mAdView.load(); //Banner Ad Load

    layout = findViewById(R.id.bannerLayout);
    layout.addView(mAdView); //attach Banner
}
```

#### *Interstitial Ads

1. To request interstitial ad, create BaseInterstitial, set ZoneId and call load function.
2. Call show to expose the interstitial ad. At this time, you need to check whether you have received an advertisement through isLoaded.
```java
BaseInterstitial mInterstitial;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    //Require
    mInterstitial = new BaseInterstitial(this);
    mInterstitial.setAdInfo("YOUR ZONE ID");//ADOP ZONE ID Setting
    mInterstitial.setInterstitialListener(new InterstitialListener() {
        @Override
        public void onLoadAd() {
           //onLoad Callback
        }

        @Override
        public void onShowAd() {
            //onShowAd Callback
            mInterstitial.load(); //Ad Reload
        }

        @Override
        public void onFailedAd() {
           //onFailedAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
        }
    });

    mInterstitial.load();

    //Ad Show Event Setting
    findViewById(R.id.showIntersitial).setOnClickListener(v -> {
        if(mInterstitial.isLoaded()){
            mInterstitial.show();
        }
    });
}
```

#### *Reward Ads

1. To request a rewarded advertisement, create a BaseReward, set the ZoneId, and call the load function.
2. Call show to expose rewarded advertisement. At this time, you need to check whether you have received an advertisement through isLoaded.
```java
BaseReward mReward;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reward);

    //Require
    mReward = new BaseReward(this);
    mReward.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
    mReward.setRewardListener(new RewardListener() {
        public void onLoadAd(String zoneId) {
            //onLoad Callback
        }

        @Override
        public void onShowAd(String zoneId) {
            //onShowAd Callback
            mReward.load(); //Ad Reload
        }

        @Override
        public void onFailedAd(String zoneId) {
            //onFailedAd Callback
        }

        @Override
        public void onCompleteAd(String zoneId) {
            //onCompleteAd Callback
        }

        @Override
        public void onOpenAd(String zoneId) {
            //onOpenAd Callback
        }

        @Override
        public void onCloseAd(String zoneId) {
            //onCloseAd Callback
        }

        @Override
        public void onClickAd(String zoneId) {
            //onClickAd Callback
        }

        @Override
        public void onSkippedAd(String zoneId) {
            //onSkippedAd Callback
        }
    });

    mReward.load();

    //Ad Show Event Setting
    findViewById(R.id.showReward).setOnClickListener(v -> {
        if(mReward.isLoaded()){
            mReward.show();
        }
    });
}
```

#### *Native Ads

1. Configure the layout according to the [Native Ad Layout Setting Guide] (https://github.com/bidmad/Bidmad-Android/wiki/Native-Ad-Layout-Setting-Guide%5BENG%5D).
2. To request a native ad, we create a BaseNativeAd, set the layout configured through setNativeAdContainer and registerViewForInteraction, and call the load function.
3. Add BaseNativeAd to the View created above to expose native ads.
```java
    CustomNativeAdLayout layoutNative;
    BaseNativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        //Require
        nativeAd = new BaseNativeAd(this);
        nativeAd.setAdInfo("YOUR ZONE ID"); //ADOP ZONE ID Setting
        layoutNative = findViewById(R.id.native_ad_container);
        nativeAd.setNativeAdContainer(layoutNative, R.layout.newslist_native_item_ad);
        nativeAd.setNativeAdListener(new NativeListener() {
            @Override
            public void onSuccessHouseAd() {
                //onSuccessHouseAd Callback
            }

            @Override
            public void onSuccessAd() {
                //onSuccessAd Callback
            }

            @Override
            public void onFailedAd() {
                //onFailedAd Callback
            }

            @Override
            public void onClickedAd(){
                //onClickedAd Callback
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

        nativeAd.load();
        layoutNative.addView(nativeAd);
```

#### *Offerwall Ads

1. In order to request offerwall advertisement, mOfferwall constructor is called, and if a response is received with onInitSuccess, then load is called.
2. Call show to provide a list of offer wall advertisements. At this time, you need to check whether you have received an advertisement through isLoaded.
3. In the case of offer wall advertisement, Currency are received according to whether the conditions for receive of Currency are met for advertisements provided in the list. The received Currency can be consumed through spendCurrency Function.
(*The received Currency can be checked through getCurrencyBalance Function.)
```java
BaseOfferwall mOfferwall;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offerwall);

    //Require
    mOfferwall = new BaseOfferwall(this, "YOUR ZONE ID", new OfferwallInitListener() {
        @Override
        public void onInitSuccess() {
            //onInitSuccess Callback
        }

        @Override
        public void onInitFail(String error) {
            //onInitFail Callback
        }
    });

    mOfferwall.setOfferwallAdListener(new OfferwallAdListener() {
        @Override
        public void onLoadAd() {
            //onLoadAd Callback
        }

        @Override
        public void onShowAd() {
            //onShowAd Callback
        }

        @Override
        public void onFailedAd() {
            //onFailedAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
        }
    });

    mOfferwall.setOfferwallCurrencyListener(new OfferwallCurrencyListener() {
        @Override
        public void onGetCurrencyBalanceSuccess(String currencyName, int balance) {
            //onGetCurrencyBalanceSuccess Callback
        }

        @Override
        public void onGetCurrencyBalanceFail(String error) {
            //onGetCurrencyBalanceFail Callback
        }

        @Override
        public void onSpendCurrencySuccess(String currencyName, int balance) {
            //onSpendCurrencySuccess Callback
        }

        @Override
        public void onSpendCurrencyFail(String error) {
             //onSpendCurrencyFail Callback
        }
    });

    findViewById(R.id.loadOfferwall).setOnClickListener(v -> {
        if(mOfferwall.isSDKInit()){
            mOfferwall.load();
        }
    });

    findViewById(R.id.showOfferwall).setOnClickListener(v -> {
        if(mOfferwall.isLoaded()){
            mOfferwall.show();
        }else{
            Log.d("", "Ad Not Load");
        }
    });

    findViewById(R.id.getCurrencyBalance).setOnClickListener(v -> {
        mOfferwall.getCurrencyBalance();
    });

    findViewById(R.id.spendCurrency).setOnClickListener(v -> {
        ...
        if(amount > 0){
            mOfferwall.spendCurrency(amount);
        }else{
            Log.d("","Please enter an amount greater than 0.");
        }
    });
}
```

### 3. Class Reference
#### *Banner Class Reference

- BaseAdView

Function|Description
---|---
BaseAdView(Activity)|BaseAdView constructor.
void setAdInfo(String)|Set the Banner Ad ZoneId.
void setAdViewListener(AdViewListener)|Set listener to receive event callbacks for Banner ad.
void setInterval(int)| Set the banner ad refresh interval.(60s~120s)
void load()|Request a Banner ad. Re-requests the advertisement every set interval.
void onceLoad()|Request a Banner ad. Regardless of the set interval, only one request.
void setChildDirected(boolean)|If AD Network (ex:Admob) supports an interface for COPPA, it passes a value to the interface.
void onPause()|Pause Banner ads.
void onResume()|Resume Banner ads.

- AdViewListener

Function|Description
---|---
void onLoadAd()|An event occurs when a banner ad is loaded. 
void onFailedAd()|An event occurs when a banner ad loading fails. 
void onClickedAd()|An event occurs when a banner ad is clicked.
---
#### *Interstitial Class Reference

- BaseInterstitial

Function|Description
---|---
BaseInterstitial(Activity)|BaseInterstitial constructor.
void setAdInfo(String)|Set the Interstitial Ad ZoneId.
void setInterstitialListener(InterstitialListener)|Set listener to receive event callbacks for interstitial ad.
void load()|Request an interstitial ad.
void show()|The loaded interstitial ad is displayed on the screen. 
boolean isLoaded()|Check whether interstitial ad is loaded or not.
void setMute()|Mute setup for video interstitial ads. This option only works on some Ad Networks.
void setChildDirected(boolean)|If AD Network (ex:Admob) supports an interface for COPPA, it passes a value to the interface.

- InterstitialListener

Function|Description
---|---
void onLoadAd()|An event occurs when an interstitial ad is loaded. 
void onShowAd()|An event occurs when an interstitial ad is shown. 
void onFailedAd()|An event occurs when interstitial ad loading fails. 
void onClickedAd()|An event occurs when a interstitial ad is clicked.
---
#### *Reward Class Reference

- BaseReward

Function|Description
---|---
BaseReward(Activity)|BaseReward constructor.
void setAdInfo(String)|Set Reward Ad ZoneId.
void setRewardListener(RewardListener)|Set listener to receive event callbacks for reward ad.
void load()|Request a reward ad.
void show()|The loaded reward ad is displayed on the screen. 
boolean isLoaded()|Checks whether the reward ad is loaded or not.
void setMute()|Perform Mute setting for reward ad. This option only works on some Ad Networks.
void setChildDirected(boolean)|If AD Network (ex:Admob) supports an interface for COPPA, it passes a value to the interface.

- RewardListener

Function|Description
---|---
void onLoadAd(String)|An event occurs when a reward ad is loaded, and the ZoneId is returned.
void onShowAd(String)|An event occurs when a reward ad is shown, an event occurs, and the ZoneId is 반환합니다.
void onFailedAd(String)|An event occurs when reward ad loading fails, and the ZoneId is returned.
void onCompleteAd(String)|In the reward ad, when the reward condition is satisfied, an event occurs and the ZoneId is returned.
void onSkippedAd(String)|In the reward ad, an event occurs when the ad ends when the reward condition is not satisfied and the ZoneId is returned.
void onOpenAd(String)|An event occurs when the video of the reward ad starts, and the ZoneId is returned.
void onCloseAd(String)|An event occurs when the reward ad ends, and the ZoneId is returned..
void onClickedAd(String)|An event occurs when a reward ad is clicked, and the ZoneId is returned.
---
#### *NativeAd Class Reference

- BaseNativeAd

Function|Description
---|---
BaseNativeAd(Activity)|BaseNativeAd constructor.
void setAdInfo(String)|Set the Native Ad ZoneId.
void setNativeListener(NativeListener)|Set listener to receive event callbacks for native ad.
void setNativeAdContainer(CustomNativeAdLayout, Int)| Set the layout (CustomNativeAdLayout) to expose native ad and the layout that composes ad image/text/button, etc.
void registerViewForInteraction(Int, Int, Int, Int, Int, Int, Int, Int, Int)|Register detailed elements for Layout that composes Native ad registered in setNativeAdContainer.
void load()|Request Native Ad.
void setMute()|Perform Mute setting for native advertisement. This option only works on some Ad Networks.
void setChildDirected(boolean)|If AD Network (ex:Admob) supports an interface for COPPA, it passes a value to the interface.

- NativeListener

Function|Description
---|---
void onSuccessHouseAd()|Deprecate event.
void onSuccessAd()|An event occurs when native ad is loaded. 
void onFailedAd()|An event occurs when loading of native ads fails.
void onClickedAd()|An event occurs when a native ad is clicked. 
---
#### *Offerwall Class Reference

- BaseOfferwall

Function|Description
---|---
BaseOfferwall(Activity activity, String zoneId, OfferwallInitListener listener)|BaseOfferwall constructor. Set the offerwall ad ZoneId and Listener to receive events for Init together.
void isSDKInit()|Check whether BaseOfferwall ad is initialize or not
void setOfferwallAdListener(OfferwallAdListener)|Set listener to receive event callbacks for Offerwall ad.
void setOfferwallCurrencyListener(OfferwallCurrencyListener)|Set listener so that you can receive event callbacks for received Currency as offerwall ad.
void load()|Request Offerwall ad.
void show()|The loaded Offerwall ad is displayed on the screen. 
boolean isLoaded()|Checks whether Offerwall ad is loaded or not.
int getCurrencyBalance()|Check the Currency received by the Offerwall ads.
void spendCurrency(int)|Spends the Currency received by the Offerwall ads.

- OfferwallInitListener

Function|Description
---|---
void onInitSuccess()|An event occurs when the Initialize operation performed when the BaseOfferwall constructor is called is successful. 
void onInitFail(String)|An event occurs when the Initialize operation performed when the BaseOfferwall constructor is called fails. Return Error Message.

- OfferwallAdListener

Function|Description
---|---
void onLoadAd()|An event occurs when Offerwall ad is loaded. 
void onShowAd()|When an Offerwall ad is shown, an event occurs and the ZoneId is returned.
void onFailedAd()|An event occurs when Offerwall ad load fails. 
void onCloseAd()|An event occurs when Offerwall ad ends. 

- OfferwallCurrencyListener

Function|Description
---|---
void onGetCurrencyBalanceSuccess(String, int)|An event occurs when the search for currency received through the Offerwall advertisement is successful. 
void onGetCurrencyBalanceFail(String)|An event occurs when the inquiry of the Currency received by the Offerwall advertisement fails. Return Error Message.
void onSpendCurrencySuccess(String, int)|An event occurs when the consumption of the Currency received by the Offerwall advertisement is successful. 
void onSpendCurrencyFail(String)|An event occurs when the consumption of the Currency received by the Offerwall advertisement fails. Return Error Message.
---
#### *Common Class Reference

- Common

Function|Description
---|---
String getSDKVersion()|Get SDK version information.
void setDebugging(boolean)|When called with a true value, the log of the SDK is output.
void setGgTestDeviceid()|Register with Google TEST device to receive test ads for Google ads.

---
### 4. Note

- [GDPR Guide](https://github.com/bidmad/Bidmad-Android/wiki/Android-GDPR-Guide-%5BENG%5D)

