# BidmadSDK
### 바로가기
1. [SDK 세팅](#1-SDK-세팅)
    - [Gradle](#Gradle)
    - [AndroidManifest.xml](#AndroidManifest.xml)
2. [광고 추가하기](#2-SDK-사용하기)
    - [배너광고 추가하기](#배너광고-추가하기)
    - [전면광고 추가하기](#전면광고-추가하기)
    - [보상형광고 추가하기](#보상형광고-추가하기)
    - [네이티브광고 추가하기](#네이티브광고-추가하기)
    - [오퍼월광고 추가하기](#오퍼월광고-추가하기)
3. [Class Reference](#3-Class-Reference)
    - [배너광고 Class Reference](#배너광고-Class-Reference)
    - [전면광고 Class Reference](#전면광고-Class-Reference)
    - [보상형광고 Class Reference](#보상형광고-Class-Reference)
    - [네이티브광고 Class Reference](#네이티브광고-Class-Reference)
    - [오퍼월광고 Class Reference](#오퍼월광고-Class-Reference)
4. [참고사항](#4-참고사항)
5. [최신 샘플 프로젝트 다운로드](https://github.com/bidmad/Bidmad-Android/archive/master.zip)
---
### 1. SDK 세팅
#### *SDK 사용을 위한 기본 요건
- Gradle Plugin 3.5.4 이상
- minSdkVersion 19 이상

#### *Gradle
1. 프로젝트 Top-Level에 위치한 build.gradle 파일 내에 저장소 선언합니다.

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
3. 프로젝트 App-Level에 위치한 build.gradle 파일의 dependencies에 SDK 선언합니다.

```java
dependencies {
    ...
    implementation 'com.adop.sdk:bidmad-androidx:1.13.1'
}
```
4. 프로젝트 App-Level에 위치한 build.gradle 파일의 android 태그에 아래 옵션을 선언합니다.

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

*Bidmad는 AndroidX 라이브러리를 사용합니다. AndroidX 프로젝트가 아니라면 AndroidX로 마이그레이션 바랍니다.

#### *AndroidManifest.xml

1. 프로젝트 내 AndroidManifest.xml의 application 태그 안에 아래 코드를 선언합니다<br>
 *com.google.android.gms.ads.APPLICATION_ID의 value는 Admob 대시보드에서 확인 바랍니다.

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
2. 프로젝트 내 AndroidManifest.xml에 permission을 선언합니다.

```xml
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

### 2. 광고 추가하기

#### *배너광고 추가하기

1. 배너를 노출시킬 Layout을 추가합니다.
```java
...
<androidx.constraintlayout.widget.ConstraintLayout
    android:id="@+id/bannerLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
...
```

2. 배너 광고를 요청하기 위해 BaseAdView를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
3. 배너 광고를 노출하기 위해 BaseAdView를 위에서 생성한 View에 추가합니다.
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

#### *전면광고 추가하기

1. 전면광고를 요청하기 위해 BaseInterstitial를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
2. 전면광고를 노출하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
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

#### *보상형광고 추가하기

1. 보상형광고를 요청하기 위해 BaseReward를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
2. 보상형광고를 노출하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
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

#### *네이티브광고 추가하기

1. [네이티브 광고 레이아웃 구성 가이드](https://github.com/bidmad/Bidmad-Android/wiki/Native-Ad-Layout-Setting-Guide%5BKOR%5D)에 따라 레이아웃을 구성합니다. 
2. 네이티브 광고를 요청하기 위해 BaseNativeAd를 생성, setNativeAdContainer와 registerViewForInteraction를 통해 구성한 레이아웃을 셋팅하고 load 함수를 호출합니다.
3. 네이티브 광고를 노출하기 위해 BaseNativeAd를 위에서 생성한 View에 추가합니다.
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

#### *오퍼월광고 추가하기

1. 오퍼월광고를 요청하기 위해 mOfferwall 생성자를 호출하고 onInitSuccess로 응답을 받았다면, 이어서 load를 호출합니다.
2. 오퍼월광고를 목록을 제공하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
3. 오퍼월광고의 경우 목록에서 제공되는 광고에 대해 재화 지급 조건 충족 여부에 따라 재화가 지급됩니다. 지급된 재화는 spendCurrency를 통해 소비할 수 있습니다.
(*지급된 재화는 getCurrencyBalance를 통해 확인할 수 있습니다.)
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
#### *배너광고 Class Reference

- BaseAdView

Function|Description
---|---
BaseAdView(Activity)|BaseAdView 생성자입니다.
void setAdInfo(String)|Banner 광고 ZoneId를 설정합니다.
void setAdViewListener(AdViewListener)|Banner 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setInterval(int)| Banner 광고 Refresh Interval을 설정합니다.(60s~120s)
void load()|Banner 광고를 요청합니다. 설정된 Interval마다 광고를 재요청 합니다.
void onceLoad()|Banner 광고를 요청합니다. 설정된 Interval에 관계없이 1번만 요청합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void onPause()|Banner 광고를 제거합니다.
void onResume()|Banner 광고를 재요청합니다.

- AdViewListener

Function|Description
---|---
void onLoadAd()|Banner 광고가 Load 될 떄 이벤트가 발생합니다. 
void onFailedAd()|Banner 광고 Load에 실패할 때 이벤트가 발생합니다. 
void onClickedAd()|Banner 광고 Click시 이벤트가 발생합니다. 

#### *전면광고 Class Reference

- BaseInterstitial

Function|Description
---|---
BaseInterstitial(Activity)|BaseInterstitial 생성자입니다.
void setAdInfo(String)|Interstitial 광고 ZoneId를 설정합니다.
void setInterstitialListener(InterstitialListener)|Interstitial 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Interstitial 광고를 요청합니다.
void show()|Load된 Interstitial 광고를 화면에 노출합니다. 
boolean isLoaded()|Interstitial 광고의 Load 여부를 확인합니다.
void setMute()|동영상 Interstitial 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.

- InterstitialListener

Function|Description
---|---
void onLoadAd()|Interstitial 광고가 Load 될 떄 이벤트가 발생합니다. 
void onShowAd()|Interstitial 광고가 Show 될 때 이벤트가 발생합니다. 
void onFailedAd()|Interstitial 광고 Load에 실패할 때 이벤트가 발생합니다. 
void onClickedAd()|Interstitial 광고 Click시 이벤트가 발생합니다. 

#### *보상형광고 Class Reference

- BaseReward

Function|Description
---|---
BaseReward(Activity)|BaseReward 생성자입니다.
void setAdInfo(String)|Reward 광고 ZoneId를 설정합니다.
void setRewardListener(RewardListener)|Reward 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Reward 광고를 요청합니다.
void show()|Load된 Reward 광고를 화면에 노출합니다. 
boolean isLoaded()|Reward 광고의 Load 여부를 확인합니다.
void setMute()|Reward 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.

- RewardListener

Function|Description
---|---
void onLoadAd(String)|Reward 광고가 Load 될 떄 이벤트가 발생하며, ZoneId를 반환합니다.
void onShowAd(String)|Reward 광고가 Show 될 때 이벤트가 발생하며, ZoneId를 반환합니다.
void onFailedAd(String)|Reward 광고 Load에 실패할 때 이벤트가 발생하며, ZoneId를 반환합니다.
void onCompleteAd(String)|Reward 광고에서 Reward가 지급조건이 충족되면 이벤트가 발생하며, ZoneId를 반환합니다.
void onSkippedAd(String)|Reward 광고에서 Reward가 지급조건이 충족되지 않은 상태로 광고 종료 시 이벤트가 발생하며, ZoneId를 반환합니다.
void onOpenAd(String)|Reward 광고의 영상이 시작될 때 이벤트가 발생하며, ZoneId를 반환합니다.
void onCloseAd(String)|Reward 광고가 종료될 때 이벤트가 발생하며, ZoneId를 반환합니다.
void onClickedAd(String)|Reward 광고 Click시 이벤트가 발생하며, ZoneId를 반환합니다.

#### *네이티브광고 Class Reference

- BaseNativeAd

Function|Description
---|---
BaseNativeAd(Activity)|BaseNativeAd 생성자입니다.
void setAdInfo(String)|Native 광고 ZoneId를 설정합니다.
void setNativeListener(NativeListener)|Native 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setNativeAdContainer(CustomNativeAdLayout, Int)| Native 광고를 노출 시킬 Layout(CustomNativeAdLayout)과 광고 이미지/텍스트/버튼 등을 구성하는 Layout을 설정합니다.
void registerViewForInteraction(Int, Int, Int, Int, Int, Int, Int, Int, Int)|setNativeAdContainer에서 등록한 Native 광고를 구성하는 Layout에 대한 세부 요소를 등록합니다.
void load()|Native 광고를 요청합니다.
void setMute()|Native 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.

- NativeListener

Function|Description
---|---
void onSuccessHouseAd()|Deprecate된 이벤트입니다.
void onSuccessAd()|Native 광고가 Load 될 때 이벤트가 발생합니다. 
void onFailedAd()|Native 광고 Load에 실패할 때 이벤트가 발생합니다.
void onClickedAd()|Native 광고 Click시 이벤트가 발생합니다. 

#### *오퍼월광고 Class Reference

- BaseOfferwall

Function|Description
---|---
BaseOfferwall(Activity activity, String zoneId, OfferwallInitListener listener)|BaseOfferwall 생성자입니다. 오퍼월광고 ZoneId와 Init에 대한 이벤트 수신을 위한 Listener를 같이 셋팅합니다.
void isSDKInit()|BaseOfferwall에 대한 Initialize 여부를 확인합니다.
void setOfferwallAdListener(OfferwallAdListener)|Offerwall 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setOfferwallCurrencyListener(OfferwallCurrencyListener)|Offerwall광고로 지급된 재화에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Offerwall 광고를 요청합니다.
void show()|Load된 Offerwall 광고를 화면에 노출합니다. 
boolean isLoaded()|Offerwall 광고의 Load 여부를 확인합니다.
int getCurrencyBalance()|Offerwall 광고로 지급된 재화를 확인합니다.
void spendCurrency(int)|Offerwall 광고로 지급된 재화를 소모합니다.

- OfferwallInitListener

Function|Description
---|---
void onInitSuccess()|BaseOfferwall 생성자 호출 시 수행하는 Initialize 작업이 성공 할 떄 이벤트가 발생합니다. 
void onInitFail(String)|BaseOfferwall 생성자 호출 시 수행하는 Initialize 작업이 실패 할 떄 이벤트가 발생합니다. Error Message을 반환합니다.

- OfferwallAdListener

Function|Description
---|---
void onLoadAd()|Offerwall 광고가 Load 될 떄 이벤트가 발생합니다. 
void onShowAd()|Offerwall 광고가 Show 될 때 이벤트가 발생하며, ZoneId를 반환합니다.
void onFailedAd()|Offerwall 광고 Load에 실패할 때 이벤트가 발생합니다. 
void onCloseAd()|Offerwall 광고가 종료 될 때 이벤트가 발생합니다. 

- OfferwallCurrencyListener

Function|Description
---|---
void onGetCurrencyBalanceSuccess(String, int)|Offerwall 광고로 지급된 재화 조회 성공 시 이벤트가 발생합니다. 
void onGetCurrencyBalanceFail(String)|Offerwall 광고로 지급된 재화 조회 실패 시 이벤트가 발생합니다. Error Message을 반환합니다.
void onSpendCurrencySuccess(String, int)|Offerwall 광고로 지급된 재화 소모 성공 시 이벤트가 발생합니다. 
void onSpendCurrencyFail(String)|Offerwall 광고로 지급된 재화 소모 실패 시 이벤트가 발생합니다. Error Message을 반환합니다.

#### *Common Class Reference

- Common

Function|Description
---|---
String getSDKVersion()|SDK의 버전 정보를 얻습니다.
void setDebugging(boolean)|True값으로 호출 시 SDK의 로그를 출력합니다.
void setGgTestDeviceid()|Google TEST 기기로 등록하여 구글 광고에 대한 테스트 광고를 수신합니다.

----
### 4. 참고사항

- [GDPR 가이드](https://github.com/bidmad/Bidmad-Unity/wiki/Unity-GDPR-Guide-%5BKOR%5D)

