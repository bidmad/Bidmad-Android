# BidmadSDK(v2.5.0.1)
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
    - [전면보상형광고 추가하기](#전면보상형광고-추가하기)
    - [앱오픈광고 추가하기](#앱오픈광고-추가하기)
3. [Class Reference](#3-Class-Reference)
    - [배너광고 Class Reference](#배너광고-Class-Reference)
    - [전면광고 Class Reference](#전면광고-Class-Reference)
    - [보상형광고 Class Reference](#보상형광고-Class-Reference)
    - [네이티브광고 Class Reference](#네이티브광고-Class-Reference)
    - [오퍼월광고 Class Reference](#오퍼월광고-Class-Reference)
    - [전면보상형광고 Class Reference](#전면보상형광고-Class-Reference)
    - [앱오픈광고 Class Reference](#앱오픈광고-Class-Reference)
4. [참고사항](#4-참고사항)
5. [최신 샘플 프로젝트 다운로드](https://github.com/bidmad/Bidmad-Android/archive/master.zip)
---
### 1. SDK 세팅
#### *SDK 사용을 위한 기본 요건
- Gradle Plugin 3.5.4 이상
- minSdkVersion 21 이상

#### *Gradle
1. 프로젝트 Top-Level에 위치한 build.gradle 파일 내에 저장소 선언합니다.

```java
allprojects {
   repositories {
       ...
       google()
       jcenter()
       mavenCentral()
       maven { url "https://devrepo.kakao.com/nexus/content/groups/public/" } //Adift
       maven {
          url "s3://repo.cauly.net/releases"
          credentials(AwsCredentials) {
              accessKey "AKIAWRZUK5MFKYVSUOLB"
              secretKey "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"
          }
       } //Cauly
       maven { url "https://bidmad-sdk.s3.amazonaws.com/" } //bidmad
       maven { url "https://sdk.tapjoy.com/" } //Tapjoy
       maven { url "https://artifact.bytedance.com/repository/pangle" } //Pangle
       maven { url "https://jitpack.io" } //Adpie
       maven { url "https://android-sdk.is.com/" } //ironsource
}
```
2. 프로젝트 App-Level에 위치한 build.gradle 파일의 dependencies에 SDK 선언합니다.

```java
dependencies {
    ...
    implementation 'com.adop.sdk:bidmad-androidx:2.5.0.1'
    implementation 'ad.helper.openbidding:admob-obh:2.5.0.1'
    implementation 'com.adop.adapter.fc:fcNetwork-adapter:2.5.0.1'
    implementation 'com.adop.adapter.fnc:fncNetwork-adapter:2.5.1.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
}
```
3. 프로젝트 App-Level에 위치한 build.gradle 파일의 android 태그에 아래 옵션을 선언합니다.

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

4. 프로젝트에서 Proguard를 적용하고 있다면 아래의 룰을 추가하세요.
```cpp
-keep class com.adop.sdk.** { *; }
-keep class ad.helper.openbidding.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Tapjoy
-keep class com.tapjoy.** { *; }
-keep class com.moat.** { *; }
-keepattributes JavascriptInterface
-keepattributes *Annotation*
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.tapjoy.**
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
3. Android 12버전을 Target하는 경우 [AD_ID 권한 추가 선언 가이드](https://github.com/bidmad/Bidmad-Android/wiki/AD_ID-Permission-Guide%5BKOR%5D)를 확인바랍니다.

### 2. 광고 추가하기

#### *BidmadSDK 초기화 하기

- 앱 시작 시 initializeSdk()를 호출합니다.
- initializeSdk를 호출하지 않는 경우, SDK 자체적으로 수행하기 때문에 초회 광고 로딩이 늦어질 수 있습니다.
```
    BidmadCommon.initializeSdk()
```
- 전면 또는 보상형 광고를 사용하시는 경우에는 원활한 광고 노출을 위해 initializeSdk() 호출 대신
아래 전면 / 보상형 광고 가이드에 따라 앱 시작 시점에서 광고를 Load 하시고 원하시는 시점에 Show하시기 바랍니다.

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

2. 배너 광고를 요청하기 위해 BidmadBannerAd를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
3. 배너 광고를 노출하기 위해 BidmadBannerAd를 위에서 생성한 View에 추가합니다.
```java
ConstraintLayout layout;
BidmadBannerAd mAdView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    //Require
    mAdView = new BidmadBannerAd(this,"YOUR ZONE ID");
    mAdView.setAdViewListener(new AdViewListener() {
        @Override
        public void onLoadAd(String NetworkName) {
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
    layout.addView(mAdView.getView()); //attach Banner
}
```

#### *전면광고 추가하기

1. 전면광고를 요청하기 위해 BidmadInterstitialAd를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
2. 전면광고를 노출하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
```java
BidmadInterstitialAd mInterstitial;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    //Require
    mInterstitial = new BidmadInterstitialAd(this,"YOUR ZONE ID");
    mInterstitial.setInterstitialListener(new InterstitialListener() {
        @Override
        public void onLoadAd() {
           //onLoad Callback
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

1. 보상형광고를 요청하기 위해 BidmadRewardAd를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
2. 보상형광고를 노출하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
```java
BidmadRewardAd mReward;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reward);

    //Require
    mReward = new BidmadRewardAd(this,"YOUR ZONE ID");
    mReward.setRewardListener(new RewardListener() {
        public void onLoadAd(String zoneId) {
            //onLoad Callback
        }

        @Override
        public void onShowAd(String zoneId) {
            //onShowAd Callback
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
2. 네이티브 광고를 요청하기 위해 BidmadNativeAd를 생성, registerViewForInteraction를 통해 구성한 레이아웃을 셋팅하고 load 함수를 호출합니다.
3. 네이티브 광고를 노출하기 위해 BidmadNativeAd getNativeLayout()를 통해 생성한 Layout에 View를 추가합니다.
```java
BidmadNativeAd nativeAd;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_native);

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

    nativeAd.load();
}
```

#### *오퍼월광고 추가하기

1. 오퍼월광고를 요청하기 위해 BidmadOfferwallAd 생성자를 호출하고 onInitSuccess로 응답을 받았다면, 이어서 load를 호출합니다.
2. 오퍼월광고를 목록을 제공하기 위해 show를 호출합니다. 이때, isLoaded를 통해 광고를 수신하였는지 체크해야 합니다.
3. 오퍼월광고의 경우 목록에서 제공되는 광고에 대해 재화 지급 조건 충족 여부에 따라 재화가 지급됩니다. 지급된 재화는 spendCurrency를 통해 소비할 수 있습니다.
(*지급된 재화는 getCurrencyBalance를 통해 확인할 수 있습니다.)
```java
BidmadOfferwallAd mOfferwall;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offerwall);

    //Require
    mOfferwall = new BidmadOfferwallAd(this, "YOUR ZONE ID", new OfferwallInitListener() {
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

#### *전면보상형광고 추가하기

1. 전면보상형광고 요청하기 위해 BidmadRewardInterstitialAd 생성자를 호출하고 ZoneId 세팅 후 load 함수를 호출합니다.
2. 전면보상형광고를 노출하기 위해 Popup Class를 생성, 호출합니다. 이때 Popup에는 사용자가 Popup에 표시된 안내문을 읽고 광고를 시청할 것인지 아닌지 결정할 충분한 시간이 주어져야 합니다.
3. 사용자가 광고를 시청하고자 하는 경우 show를 호출합니다.<br>

*전면보상형광고는 사용자에게 노출 되기 전 Popup 등을 통해 광고가 노출될 것임을 사전에 안내해야하며, 사용자가 원치 않을 경우 광고를 보지 않을 수 있도록 옵션을 제공해야 합니다.
```java
BidmadRewardInterstitialAd mRewardInterstitial;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rewardinterstitial);

    //Require
    mRewardInterstitial = new BidmadRewardInterstitialAd(this, "YOUR ZONE ID");
    mRewardInterstitial.setRewardInterstitialListener(new RewardInterstitialListener() {
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
        @Override
        public void onSkipAd() {
            //onSkipAd Callback

        }
        @Override
        public void onCompleteAd() {
            //onCompleteAd Callback

        }
    });

    mRewardInterstitial.load();

    findViewById(R.id.popupCall).setOnClickListener(v -> {
        alertMessage();
    });
}
public void alertMessage(){   
    AlertPopup ap = new AlertPopup(this, new AlertPopup.OnClickListener() {
        @Override
        public void OnNegativeButton() {

        }

        @Override
        public void OnPositiveButton() {
            if(mRewardInterstitial.isLoaded()){
                mRewardInterstitial.show();
            }
        }
    });
    ap.show();
}

```

#### *앱오픈광고 추가하기

1. 앱오픈광고 요청하기 위해 BidmadAppOpenAd 생성자를 호출합니다. 이때 ZoneId를 셋팅하고 광고 Orientation option을 설정합니다.
2. start를 호출하면 BidmadAppOpenAd가 Application의 Lifecycle에 따라 onStart 발생 시 광고를 요청하고 노출합니다.<br>

*앱 오픈 광고는 앱 상태가 백그라운드에서 포그라운드로 변경될 때 광고를 노출합니다.<br>
*Lifecycle에 따른 광고 호출을 변경하고자 하는 경우 BidmadAppOpenAd을 사용해 앱오픈 광고를 구현 바랍니다.
```java
BidmadAppOpenAd mAppOpen;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_appopen);

    mAppOpen = new BidmadAppOpenAd(this.getApplication(), "YOUR ZONE ID", BidmadAppOpenAd.ORIENTATION_PORTRAIT);
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
            mAppOpen.adLoad();
        }
    });

    mAppOpen.start();
}

@Override
public void onBackPressed() {
    super.onBackPressed();
    mAppOpen.end();
}
```

### 3. Class Reference
#### *배너광고 Class Reference

- BidmadBannerAd

Function|Description
---|---
BidmadBannerAd(Activity, String)|BidmadBannerAd 생성자입니다. ZoneId를 같이 세팅합니다.
void setAdViewListener(AdViewListener)|Banner 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setInterval(int)| Banner 광고 Refresh Interval을 설정합니다.(60s~120s)
void load()|Banner 광고를 요청합니다. 설정된 Interval마다 광고를 재요청 합니다.
void onceLoad()|Banner 광고를 요청합니다. 설정된 Interval에 관계없이 1번만 요청합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void onPause()|Banner 광고를 제거합니다.
void onResume()|Banner 광고를 재요청합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.

- AdViewListener

Function|Description
---|---
void onLoadAd(String networkName)|Banner 광고가 Load 될 떄 이벤트가 발생하며, Ad NetworkName을 반환합니다. 
void onFailedAd()|Banner 광고 Load에 실패할 때 이벤트가 발생합니다. 
void onClickedAd()|Banner 광고 Click시 이벤트가 발생합니다. 
---
#### *전면광고 Class Reference

- BidmadInterstitialAd

Function|Description
---|---
BidmadInterstitialAd(Activity, String)|BidmadInterstitialAd 생성자입니다. ZoneId를 같이 세팅합니다.
void setInterstitialListener(InterstitialListener)|Interstitial 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Interstitial 광고를 요청합니다.
void show()|Load된 Interstitial 광고를 화면에 노출합니다. 
boolean isLoaded()|Interstitial 광고의 Load 여부를 확인합니다.
void setMute()|동영상 Interstitial 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.
static void setAutoReload(boolean)|광고 노출 시 자동으로 다음 광고를 요청합니다. 이 기능은 광고 Fail이 발생하면 중단됩니다. Default 값은 True입니다.

- InterstitialListener

Function|Description
---|---
void onLoadAd(String NetworkName)|Interstitial 광고가 Load 될 떄 이벤트가 발생합니다. 로드된 Ad Network Name을 반환합니다. 
void onShowAd()|Interstitial 광고가 Show 될 때 이벤트가 발생합니다. 
void onFailedAd()|Interstitial 광고 Load에 실패할 때 이벤트가 발생합니다. 
void onClickedAd()|Interstitial 광고 Click시 이벤트가 발생합니다. 
---
#### *보상형광고 Class Reference

- BidmadRewardAd

Function|Description
---|---
BidmadRewardAd(Activity, String)|BidmadRewardAd 생성자입니다. ZoneId를 같이 세팅합니다.
void setRewardListener(RewardListener)|Reward 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Reward 광고를 요청합니다.
void show()|Load된 Reward 광고를 화면에 노출합니다. 
boolean isLoaded()|Reward 광고의 Load 여부를 확인합니다.
void setMute()|Reward 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.
static void setAutoReload(boolean)|광고 노출 시 자동으로 다음 광고를 요청합니다. 이 기능은 광고 Fail이 발생하면 중단됩니다. Default 값은 True입니다.

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
---
#### *네이티브광고 Class Reference

- BidmadNativeAd

Function|Description
---|---
BidmadNativeAd(Activity, String)|BidmadNativeAd 생성자입니다. ZoneId를 같이 세팅합니다.
void setNativeListener(NativeListener)|Native 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void registerViewForInteraction(Int, Int, Int, Int, Int, Int)|Native 광고를 구성하는 Layout에 대한 세부 요소를 등록합니다.
void load()|Native 광고를 요청합니다.
FrameLayout getNativeLayout()|NativeAd 레이아웃을 가져옵니다.
void setMute()|Native 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.

- NativeListener

Function|Description
---|---
void onSuccessHouseAd()|Deprecate된 이벤트입니다.
void onSuccessAd()|Native 광고가 Load 될 때 이벤트가 발생합니다. 
void onFailedAd()|Native 광고 Load에 실패할 때 이벤트가 발생합니다.
void onClickedAd()|Native 광고 Click시 이벤트가 발생합니다. 
---
#### *오퍼월광고 Class Reference

- BidmadOfferwallAd

Function|Description
---|---
BidmadOfferwallAd(Activity activity, String zoneId, OfferwallInitListener listener)|BidmadOfferwallAd 생성자입니다. 오퍼월광고 ZoneId와 Init에 대한 이벤트 수신을 위한 Listener를 같이 셋팅합니다.
void isSDKInit()|BidmadOfferwallAd에 대한 Initialize 여부를 확인합니다.
void setOfferwallAdListener(OfferwallAdListener)|Offerwall 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setOfferwallCurrencyListener(OfferwallCurrencyListener)|Offerwall광고로 지급된 재화에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|Offerwall 광고를 요청합니다.
void show()|Load된 Offerwall 광고를 화면에 노출합니다. 
boolean isLoaded()|Offerwall 광고의 Load 여부를 확인합니다.
int getCurrencyBalance()|Offerwall 광고로 지급된 재화를 확인합니다.
void spendCurrency(int)|Offerwall 광고로 지급된 재화를 소모합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.

- OfferwallInitListener

Function|Description
---|---
void onInitSuccess()|BidmadOfferwallAd 생성자 호출 시 수행하는 Initialize 작업이 성공 할 떄 이벤트가 발생합니다. 
void onInitFail(String)|BidmadOfferwallAd 생성자 호출 시 수행하는 Initialize 작업이 실패 할 떄 이벤트가 발생합니다. Error Message을 반환합니다.

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
---
#### *전면보상형광고 Class Reference

- BidmadRewardInterstitialAd

Function|Description
---|---
BidmadRewardInterstitialAd(Activity activity, String)|BidmadRewardInterstitialAd 생성자입니다. ZoneId를 같이 셋팅합니다.
void setRewardInterstitialListener(RewardInterstitialListener)|RewardInterstitial 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void load()|RewardInterstitial 광고를 요청합니다.
void show()|Load된 RewardInterstitial 광고를 화면에 노출합니다. 
boolean isLoaded()|RewardInterstitial 광고의 Load 여부를 확인합니다.
void setMute()|RewardInterstitial 광고에 대한 Mute 설정을 수행합니다. 이 옵션은 일부 Ad Network에서만 동작합니다.
void setChildDirected(boolean)|AD Network(ex:Admob)에서 COPPA에 대한 인터페이스를 지원한다면, 해당 인터페이스에 값을 전달 합니다.
void setCUID()|사용자 고유 식별자를 등록합니다.
static void setAutoReload(boolean)|광고 노출 시 자동으로 다음 광고를 요청합니다. 이 기능은 광고 Fail이 발생하면 중단됩니다. Default 값은 True입니다.

- RewardInterstitialListener

Function|Description
---|---
void onLoadAd()|RewardInterstitial 광고가 Load 될 떄 이벤트가 발생합니다.
void onShowAd()|RewardInterstitial 광고가 Show 될 때 이벤트가 발생합니다.
void onFailedAd()|RewardInterstitial 광고 Load에 실패할 때 이벤트가 발생합니다.
void onCompleteAd()|RewardInterstitial 광고에서 Reward 지급조건이 충족되면 이벤트가 발생합니다.
void onSkippedAd()|RewardInterstitial 광고에서 Reward 지급조건이 충족되지 않은 상태로 광고 종료 시 이벤트가 발생합니다.
void onCloseAd()|RewardInterstitial 광고가 종료될 때 이벤트가 발생합니다.
---
#### *앱오픈광고 Class Reference

- BidmadAppOpenAd

Function|Description
---|---
BidmadAppOpenAd(Application, String, int)|BidmadAppOpenAd 생성자입니다. 앱오픈광고 ZoneId와 Orientation을 같이 셋팅합니다.
void setAppOpenListener(AppOpenListener)|AppOpen 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setAppOpenLifecycleListener(AppOpenLifecycleListener)|Lifecycle에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void start()|LifecycleObserver를 등록하여 Lifecycle에 따라 AppOpen 광고를 요청하고 노출시킵니다.
void end()|등록한 LifecycleObserver를 제거합니다. 
void adLoad()|AppOpen 광고를 요청합니다.
boolean isAdLoaded()|AppOpen 광고의 Load 여부를 확인합니다.
void adShow(boolean)|Load된 AppOpen 광고를 화면에 노출합니다. 
void setCUID()|사용자 고유 식별자를 등록합니다.

- AppOpenListener

Function|Description
---|---
void onLoadAd()|AppOpen 광고가 Load 될 떄 이벤트가 발생합니다.
void onShowAd()|AppOpen 광고가 Show 될 떄 이벤트가 발생합니다.
void onFailedAd()|AppOpen 광고 Load에 실패할 때 이벤트가 발생합니다.
void onCloseAd()|AppOpen 광고가 종료될 때 이벤트가 발생합니다.
void onExpired()|AppOpen 광고 Load하고 3시간 이상 경과 후 Show를 하는 경우에 이벤트가 발생합니다.

- AppOpenLifecycleListener

Function|Description
---|---
void onActivityForGround()|Application.ActivityLifecycleCallbacks의 onActivityForGround가 호출되면 이벤트가 발생합니다. 
void onActivityCreated(Activity, Bundle)|Application.ActivityLifecycleCallbacks의 onActivityCreated가 호출되면 이벤트가 발생합니다.
void onActivityStarted(Activity)|Application.ActivityLifecycleCallbacks의 onActivityStarted가 호출되면 이벤트가 발생합니다. 
void onActivityResumed(Activity)|Application.ActivityLifecycleCallbacks의 onActivityResumed가 호출되면 이벤트가 발생합니다. 
void onActivityPaused(Activity)|Application.ActivityLifecycleCallbacks의 onActivityPaused가 호출되면 이벤트가 발생합니다. 
void onActivityStopped(Activity)|Application.ActivityLifecycleCallbacks의 onActivityStopped가 호출되면 이벤트가 발생합니다. 
void onActivitySaveInstanceState(Activity, Bundle)|Application.ActivityLifecycleCallbacks의 onActivitySaveInstanceState가 호출되면 이벤트가 발생합니다. 
void onActivityDestroyed(Activity)|Application.ActivityLifecycleCallbacks의 onActivityDestroyed가 호출되면 이벤트가 발생합니다. 
---
#### *BidmadCommon Class Reference

- BidmadCommon

Function|Description
---|---
String getSDKVersion()|SDK의 버전 정보를 얻습니다.
void setDebugging(boolean)|True값으로 호출 시 SDK의 로그를 출력합니다.
void setGgTestDeviceid()|Google TEST 기기로 등록하여 구글 광고에 대한 테스트 광고를 수신합니다.
String getGgTestDeviceid()|setGgTestDeviceid로 등록한 기기 ID를 가져옵니다.
void initializeSdk(Activity)|BidmadSDK 초기화 작업을 수행합니다.

----
### 4. 참고사항

- [GDPR 가이드](https://github.com/bidmad/Bidmad-Android/wiki/Android-GDPR-Guide-%5BKOR%5D)
- [v2.0.0.0 API 변경 내역](https://github.com/bidmad/Bidmad-Android/wiki/v2.0.0.0-API-Migration-Guide%5BKOR%5D)
