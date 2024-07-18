# BidmadSDK(v3.15.0)
### 바로가기
1. [SDK 세팅](#1-SDK-세팅)
   - [Gradle](#Gradle)
   - [AndroidManifest.xml](#AndroidManifest.xml)
2. [광고 추가하기](#2-SDK-사용하기)
   - [배너광고 추가하기](#배너광고-추가하기)
   - [전면광고 추가하기](#전면광고-추가하기)
   - [보상형광고 추가하기](#보상형광고-추가하기)
   - [네이티브광고 추가하기](#네이티브광고-추가하기)
   - [앱오픈광고 추가하기](#앱오픈광고-추가하기)
3. [Class Reference](#3-Class-Reference)
   - [배너광고 Class Reference](#배너광고-Class-Reference)
   - [전면광고 Class Reference](#전면광고-Class-Reference)
   - [보상형광고 Class Reference](#보상형광고-Class-Reference)
   - [네이티브광고 Class Reference](#네이티브광고-Class-Reference)
   - [앱오픈광고 Class Reference](#앱오픈광고-Class-Reference)
4. [참고사항](#4-참고사항)
5. [최신 샘플 프로젝트 다운로드](https://github.com/bidmad/Bidmad-Android/archive/master.zip)
---
### 1. SDK 세팅
#### *SDK 사용을 위한 기본 요건
- Gradle Plugin 3.6.0 이상
- minSdkVersion 21 이상

#### *Gradle
1. 프로젝트 Top-Level에 위치한 build.gradle 파일 내에 저장소 선언합니다.<br>
   *SDK 버전 3.10.0 이상 부터 각 광고 네트워크 별 의존성을 추가 할 수 있습니다. 추가 미디에이션 광고 세팅은 테크랩스 플랫폼 사업부 운영팀에 문의하세요.
```java
allprojects {
   repositories {
        ...
        google()
        mavenCentral()
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' } //Adift
        maven { url "https://bidmad-sdk.s3.amazonaws.com/" } //Bidmad
        maven { url 'https://android-sdk.is.com/' } // IronSource
        maven { url "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea" } //Mintegral
        maven { url 'https://artifact.bytedance.com/repository/pangle/' } //Pangle
        maven { url 'https://repo.pubmatic.com/artifactory/public-repos' } //PubMatic
        maven { url "https://sdk.tapjoy.com/" } //Tapjoy
}
```
2. 프로젝트 App-Level에 위치한 build.gradle 파일의 dependencies에 SDK 선언합니다.

```java
dependencies {
    ...
    implementation 'ad.helper.openbidding:admob-obh:3.15.0'
    implementation 'com.adop.sdk:bidmad-androidx:3.15.0'
    implementation 'com.adop.sdk.adapter:adfit:3.12.15.2'
    implementation 'com.adop.sdk.adapter:admob:22.0.0.5'
    implementation 'com.adop.sdk.adapter:applovin:11.9.0.3'
    implementation 'com.adop.sdk.adapter:coupang:1.0.0.2'
    implementation 'com.adop.sdk.adapter:criteo:6.0.0.1'
    implementation 'com.adop.sdk.adapter:fyber:8.2.3.3'
    implementation 'com.adop.sdk.adapter:ironsource:7.3.0.0'
    implementation 'com.adop.sdk.adapter:pangle:5.2.1.1.2'
    implementation 'com.adop.sdk.adapter:pubmatic:2.7.1.3'
    implementation 'com.adop.sdk.adapter:unityads:4.6.1.4'
    implementation 'com.adop.sdk.adapter:vungle:6.12.1.2'
    implementation 'com.adop.sdk.partners:admobbidding:1.0.2'
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
-keep class com.adop.sdk.adapter.**{ *; }
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
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.bykv.vk.openvk.component.video.api.** { *; }

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

1. 프로젝트 내 AndroidManifest.xml의 application 태그 안에 아래 코드를 선언합니다([가이드](https://github.com/bidmad/SDK/wiki/Find-your-app-key%5BKR%5D))<br>
   *com.google.android.gms.ads.APPLICATION_ID의 value는 Admob 대시보드에서 확인 바랍니다.
   *com.adop.sdk.APP_KEY의 value는 Insight 로그인 후 계정관리 > 나의 정보 > 상세 정보 에서 확인 바랍니다.

```xml
<application
   android:usesCleartextTraffic="true">
   ...
   <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="APPLICATION_ID"/>
   <meta-data android:name="com.adop.sdk.APP_KEY" android:value="INSERT_YOUR_APPKEY"/>
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
- initializeSdk를 호출하지 않는 경우 광고를 로드할 수 없습니다.
- initiaize Callback을 사용하여 초기화 여부를 확인할 수 있습니다.
```
    BidmadCommon.initializeSdk(activity);
    /** with initialize Callback
      BidmadCommon.initializeSdk(this, new BidmadInitializeListener() {
         @Override
         public void onInitialized(boolean isComplete) {
         }
      });
   */

```
-  Insight 로그인 후 계정관리 > 나의 정보 > 상세 정보 에서 프리로드 사용여부를 체크 및 저장 하면 초기화 시 전면/보상형 광고를 Load 합니다.

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
        public void onLoadAd() {
            //onLoad Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
            //onLoadFailAd Callback
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
2. 전면광고를 노출하기 위해 show를 호출합니다. 로드가 되지 않은 상태에서 show를 호출하면 load 함수를 내부적으로 호출합니다.
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
        public void onLoadFailAd(BMAdError error) {
           //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error) {
           //onShowFailAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
        }
    });

    mInterstitial.load();

    //Ad Show Event Setting
    findViewById(R.id.showIntersitial).setOnClickListener(v -> {
          mInterstitial.show();
    });
}
```

#### *보상형광고 추가하기

1. 보상형광고를 요청하기 위해 BidmadRewardAd를 생성, ZoneId 세팅 후 load 함수를 호출합니다.
2. 보상형광고를 노출하기 위해 show를 호출합니다. 로드가 되지 않은 상태에서 show를 호출하면 load 함수를 내부적으로 호출합니다.
```java
BidmadRewardAd mReward;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reward);

    //Require
    mReward = new BidmadRewardAd(this,"YOUR ZONE ID");
    mReward.setRewardListener(new RewardListener() {
        public void onLoadAd() {
            //onLoad Callback
        }

        @Override
        public void onShowAd() {
            //onShowAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
            //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error) {
           //onShowFailAd Callback
        }

        @Override
        public void onCompleteAd() {
            //onCompleteAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
        }

        @Override
        public void onClickAd() {
            //onClickAd Callback
        }

        @Override
        public void onSkipAd() {
            //onSkipAd Callback
        }
    });

    mReward.load();

    //Ad Show Event Setting
    findViewById(R.id.showReward).setOnClickListener(v -> {
         mReward.show();
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
            //onLoadAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
            callbackStatus.append("onLoadFailAd() Called\n");
            //onLoadFailAd Callback
        }

        @Override
        public void onClickAd(){
            callbackStatus.append("onClickAd() Called\n");
            //onClickAd Callback
        }
    });

    nativeAd.load();
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

    mAppOpen = new BidmadAppOpenAd(this.getApplication(), "YOUR ZONE ID");
    mAppOpen.setAppOpenListener(new AppOpenListener() {
        @Override
        public void onLoadAd() {
            //onLoadAd Callback
            mAppOpen.adShow();
        }

        @Override
        public void onShowAd() {
	        //onShowAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
	        //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error) {
           //onShowFailAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
        }

        @Override
        public void onCloseAd() {
            //onCloseAd Callback
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
void onPause()|Banner 광고를 제거합니다.
void onResume()|Banner 광고를 재요청합니다.

- AdViewListener

Function|Description
---|---
void onLoadAd()|Banner 광고가 Load 될 떄 이벤트가 발생합니다.
void onLoadFailAd(BMAdError error)|Banner 광고 Load에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onClickAd()|Banner 광고 Click시 이벤트가 발생합니다.
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
static void setAutoReload(boolean)|광고 노출 시 자동으로 다음 광고를 요청합니다. 이 기능은 광고 Fail이 발생하면 중단됩니다. Default 값은 True입니다.

- InterstitialListener

Function|Description
---|---
void onLoadAd()|Interstitial 광고가 Load 될 떄 이벤트가 발생합니다.
void onShowAd()|Interstitial 광고가 Show 될 때 이벤트가 발생합니다.
void onLoadFailAd(BMAdError error)|Interstitial 광고 Load에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onShowFailAd(BMAdError error)|Interstitial 광고 Show에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onCloseAd()|Interstitial 광고 Close시 이벤트가 발생합니다.
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
static void setAutoReload(boolean)|광고 노출 시 자동으로 다음 광고를 요청합니다. 이 기능은 광고 Fail이 발생하면 중단됩니다. Default 값은 True입니다.

- RewardListener

Function|Description
---|---
void onLoadAd()|Reward 광고가 Load 될 떄 이벤트가 발생합니다.
void onShowAd()|Reward 광고가 Show 될 때 이벤트가 발생합니다.
void onLoadFailAd(BMAdError error)|Reward 광고 Load에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onShowFailAd(BMAdError error)|Reward 광고 Show에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onCompleteAd()|Reward 광고에서 Reward가 지급조건이 충족되면 이벤트가 발생하며, ZoneId를 반환합니다.
void onSkipAd()|Reward 광고에서 Reward가 지급조건이 충족되지 않은 상태로 광고 종료 시 이벤트가 발생합니다.
void onCloseAd()|Reward 광고가 종료될 때 이벤트가 발생합니다.
void onClickAd()|Reward 광고 Click시 이벤트가 발생합니다.
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

- NativeListener

Function|Description
---|---
void onLoadAd()|Native 광고가 Load 될 때 이벤트가 발생합니다.
void onLoadFailAd(BMAdError error)|Native 광고 Load에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onClickAd()|Native 광고 Click시 이벤트가 발생합니다.
---

#### *앱오픈광고 Class Reference

- BidmadAppOpenAd

Function|Description
---|---
BidmadAppOpenAd(Application, String)|BidmadAppOpenAd 생성자입니다. 앱오픈광고 ZoneId를 셋팅합니다.
void setAppOpenListener(AppOpenListener)|AppOpen 광고에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void setAppOpenLifecycleListener(AppOpenLifecycleListener)|Lifecycle에 대한 이벤트 콜백을 받을 수 있도록 listener를 설정합니다.
void start()|LifecycleObserver를 등록하여 Lifecycle에 따라 AppOpen 광고를 요청하고 노출시킵니다.
void end()|등록한 LifecycleObserver를 제거합니다.
void adLoad()|AppOpen 광고를 요청합니다.
boolean isAdLoaded()|AppOpen 광고의 Load 여부를 확인합니다.
void adShow()|Load된 AppOpen 광고를 화면에 노출합니다.

- AppOpenListener

Function|Description
---|---
void onLoadAd()|AppOpen 광고가 Load 될 떄 이벤트가 발생합니다.
void onShowAd()|AppOpen 광고가 Show 될 떄 이벤트가 발생합니다.
void onLoadFailAd(BMAdError error)|AppOpen 광고 Load에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onShowFailAd(BMAdError error)|AppOpen 광고 Show에 실패할 때 이벤트가 발생합니다. BMAError로 에러코드와 메시지를 확인 할 수 있습니다.
void onCloseAd()|AppOpen 광고가 종료될 때 이벤트가 발생합니다.
void onExpireAd()|AppOpen 광고 Load하고 3시간 이상 경과 후 Show를 하는 경우에 이벤트가 발생합니다.

- AppOpenLifecycleListener

Function|Description
---|---
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
void initializeSdk(Activity, String)|BidmadSDK 초기화 작업을 수행합니다. AppKey를 설정합니다.
void initializeSdk(Context, String)|BidmadSDK 초기화 작업을 수행합니다. AppKey를 설정합니다.
void initializeSdk(Activity)|BidmadSDK 초기화 작업을 수행합니다. AndroidManifest.xml의 AppKey를 설정합니다.
void initializeSdk(Context)|BidmadSDK 초기화 작업을 수행합니다. AndroidManifest.xml의 AppKey를 설정합니다.
void initializeSdk(Activity, String, BidmadInitializeListener)|BidmadSDK 초기화 작업을 수행합니다. BidmadInitializeListener을 통해 초기화 여부를 전달 받습니다.
void initializeSdk(Context, String, BidmadInitializeListener)|BidmadSDK 초기화 작업을 수행합니다. BidmadInitializeListener을 통해 초기화 여부를 전달 받습니다.
void initializeSdk(Activity, BidmadInitializeListener)|BidmadSDK 초기화 작업을 수행합니다. AndroidManifest.xml의 AppKey를 설정합니다. BidmadInitializeListener을 통해 초기화 여부를 전달 받습니다.
void initializeSdk(Context, BidmadInitializeListener)|BidmadSDK 초기화 작업을 수행합니다. AndroidManifest.xml의 AppKey를 설정합니다. BidmadInitializeListener을 통해 초기화 여부를 전달 받습니다.
---

#### *AdOption Class Reference

- AdOption

Function|Description
---|---
static AdOption getInstance()|공용의 AdOption 반환합니다.
boolean isUseMute()|광고 음소거 설정여부를 가져옵니다.
void setUseMute(boolean)|True 값으로 호출 시 광고 음소거가 설정됩니다.
boolean isChildDirected()|COPPA 적용 여부를 확인합니다.
void setChildDirected(boolean)|COPPA 적용 여부를 설정합니다.
String getCuid()|User를 식별하는 ID 값을 확인합니다.
void setCuid(String)|User를 식별하는 ID 값을 설정합니다.
boolean getUseServerSideCallback()|Server Side Callback 사용 여부를 확인합니다.
void setUseServerSideCallback(boolean)|Server Side Callback 사용 여부를 설정합니다.
----

#### *AdFreeInformation Class Reference

- AdFreeInformation

Function|Description
---|---
static AdFreeInformation getInstance()| 프리광고에 대한 정보를 얻습니다.
int getAdFreeStatus | 광고 상태를 확인합니다.
void setOnAdFreeListener(AdFreeEventListener) |AdFreeEventListener으로 광고 상태변경 정보를 받기위해 listener를 설정합니다.
interface AdFreeEventListener |AdFreeEventListener를 통해 광고 상태 정보를 전달 받습니다.
----

### 4. 참고사항
- [GDPR 가이드](https://github.com/bidmad/Bidmad-Android/wiki/Android-GDPR-Guide-%5BKOR%5D)
- [v3.0.0 API 변경 내역](https://github.com/bidmad/Bidmad-Android/wiki/BidmadSDK-3.0.0-API-Migration-Guide)
- [쿠팡 네트워크 광고 차단 가이드](https://github.com/bidmad/Bidmad-Android/wiki/Android-Coupang-Network-Ad-Block-Guide%5BKOR%5D)
