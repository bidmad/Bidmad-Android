> [!IMPORTANT]
> Starting with version 3.18.0, the previously used Appkey has been changed to AppDomain.<br>
> **AppDomain is not compatible with existing Appkeys, so a new AppDomain must be issued to initiaize.**<br>
> If you are updating to version 3.18.0, please contact **Techlabs Platform Operations Team.**<br>
> For AppDomain changes, please check  [AndroidManifest Settings](#AndroidManifest-Settings).

# BidmadSDK(v3.23.0)
### Shortcuts

1. [SDK Settings](#1-SDK-Settings)
   - [Gradle](#Gradle)
   - [AndroidManifest](#AndroidManifest-Settings)
2. [How to use](#2-How-to-use)
   - [Banner Ads](#Banner-Ads)
   - [Interstitial Ads](#Interstitial-Ads)
   - [Reward Ads](#Reward-Ads)
   - [Native Ads](#Native-Ads)
   - [AppOpen Ads](#AppOpen-Ads)
3. [Class Reference](#3-Class-Reference)
   - [Banner Class Reference](#Banner-Class-Reference)
   - [Interstitial Class Reference](#Interstitial-Class-Reference)
   - [Reward Class Reference](#Reward-Class-Reference)
   - [NativeAd Class Reference](#NativeAd-Class-Reference)
   - [AppOpen Class Reference](#AppOpen-Class-Reference)
4. [Note](#4-Note)
5. [Download the latest sample project](https://github.com/bidmad/Bidmad-Android/archive/master.zip)
---
### 1. SDK Settings
#### *Minimum requirements for using the SDK
- minSdkVersion 23 or higher

#### *Gradle
1. Declares the repository in the build.gradle file located at the top-level of the project.
   Starting from SDK version 3.10.0 or higher, you can add dependencies for each ad network. For additional mediation ad settings, please contact the TechLabs Platform Division operations team.

```java
allprojects {
   repositories {
        ...
        google()
        mavenCentral()
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' } //Adift
        maven { url "https://bidmad-sdk.s3.amazonaws.com/" } //Bidmad
        maven { url "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea" } //Mintegral
        maven { url 'https://artifact.bytedance.com/repository/pangle/' } //Pangle
        maven { url "https://teads.jfrog.io/artifactory/SDKAndroid-maven-prod" } //Teads
        maven { url 'https://taboolapublic.jfrog.io/artifactory/mobile-release'} //Taboola
}
```
2. Declares SDK in the build.gradle file located in the project App-Level.

```java
dependencies {
    ...
    implementation 'ad.helper.openbidding:admob-obh:3.25.0'
    implementation 'com.adop.sdk:bidmad-androidx:3.25.0'
    implementation 'com.adop.sdk.adapter:adfit:3.19.5.0'
    implementation 'com.adop.sdk.adapter:admixer:1.0.9.0'
    implementation 'com.adop.sdk.adapter:admob:24.4.0.1'
    implementation 'com.adop.sdk.adapter:adpopcorn:3.8.2.0'
    implementation 'com.adop.sdk.adapter:applovin:13.3.1.0'
    implementation 'com.adop.sdk.adapter:coupang:1.0.0.5'
    implementation 'com.adop.sdk.adapter:fyber:8.3.7.0'
    implementation 'com.adop.sdk.adapter:ortb:1.0.1'
    implementation 'com.adop.sdk.adapter:mobwith:1.1.3'
    implementation 'com.adop.sdk.adapter:pangle:7.2.0.6.0'
    implementation 'com.adop.sdk.adapter:taboola:4.0.8.0'
    implementation 'com.adop.sdk.adapter:unityads:4.15.0.0'
    implementation 'com.adop.sdk.adapter:vungle:7.5.0.0'
    implementation 'com.adop.sdk.partners:admobbidding:1.1.1'
}
```
3. Declare the option below in the android tag of the build.gradle file located in the project App-Level.

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

4. If you are applying Proguard in your project, add the rules below.
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

*Bidmad uses the AndroidX library. If it's not an AndroidX project, please migrate to AndroidX.

#### *AndroidManifest Settings

1. Declare the code below in the application tag of AndroidManifest.xml in the project.<br>
   *Check the value of com.google.android.gms.ads.APPLICATION_ID on the admob dashboard.<br>
   *For the value of com.adop.sdk.APP_DOMAIN, please contact the Techlabs Platform Operations Team.

```xml
<application
   android:usesCleartextTraffic="true">
   ...
   <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="APPLICATION_ID"/>
   <meta-data android:name="com.adop.sdk.APP_DOMAIN" android:value="INSERT_YOUR_APPDOMAIN"/>
   ...
</application>
```
2. Declare permission on AndroidManifest.xml in the project.

```xml
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```
3. If targeting Android 12 version, please check [AD_ID Declaration Guide] at https://github.com/bidmad/Bidmad-Android/wiki/AD_ID-Permission-Guide%5BKOR%5D).

### 2. How to use

#### *Initializing BidmadSDK

- Call initializeSdk at app startup.
- If initializeSdk is not called, You can't ad load.
- The initialization status can be checked using the initialize callback.
```
    BidmadCommon.initializeSdk(activity)
        /** with initialize Callback
      BidmadCommon.initializeSdk(this, new BidmadInitializeListener() {
         @Override
         public void onInitialized(boolean isComplete) {
         }
      });
   */

```
- When you login to Insight and  save the Preload check, Account > My Info > DETAIL INFORMATION interstitial or rewarded ads  is preloaded at initialization.

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

2. To request a banner ad, create a BidmadBannerAd, set the ZoneId, and call the load function.
3. Add a BidmadBannerAd to the view created above to expose a banner ad.
4. Call onResuem / onPause of banner advertisement according to the life cycle.
```java
ConstraintLayout layout;
BidmadBannerAd mAdView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    //Require
    mAdView = new BidmadBannerAd(this,"YOUR ZONE ID");
    mAdView.setListener(new AdViewListener() {
        @Override
        public void onLoadAd(@NonNull BMAdInfo) {
            //onLoad Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
            //onLoadFailAd Callback
        }

        @Override
        public void onClickAd(@NonNull BMAdInfo) {
            //onClickAd Callback
        }
    });

    // mAdView.setInterval(120); //Refresh Interval 60~120s
    mAdView.load(); //Banner Ad Load

    layout = findViewById(R.id.bannerLayout);
    layout.addView(mAdView.getView()); //attach Banner
}

@Override
protected void onResume() {
    super.onResume();
    if(mAdView != null)
        mAdView.onResume();
}

@Override
protected void onPause() {
    super.onPause();
    if(mAdView != null)
        mAdView.onPause();
}
```

#### *Interstitial Ads

1. To request interstitial ad, create BidmadInterstitialAd, set ZoneId and call load function.
2. Call show to expose Interstitial advertisement. At this time, you need to check whether you have received an advertisement through isLoaded.
```java
BidmadInterstitialAd mInterstitial;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    //Require
    mInterstitial = new BidmadInterstitialAd(this,"YOUR ZONE ID");
    mInterstitial.setListener(new InterstitialListener() {
        @Override
        public void onLoadAd(@NonNull BMAdInfo) {
           //onLoad Callback
        }

        @Override
        public void onShowAd(@NonNull BMAdInfo) {
            //onShowAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
           //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error, @NonNull BMAdInfo) {
           //onShowFailAd Callback
        }

        @Override
        public void onClickAd(@NonNull BMAdInfo info) {
            //onClickAd Callback
        }

        @Override
        public void onCloseAd(@NonNull BMAdInfo) {
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

#### *Reward Ads

1. To request a rewarded advertisement, create a BidmadRewardAd, set the ZoneId, and call the load function.
2. Call show to expose rewarded advertisement. At this time, you need to check whether you have received an advertisement through isLoaded.
```java
BidmadRewardAd mReward;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reward);

    //Require
    mReward = new BidmadRewardAd(this,"YOUR ZONE ID");
    mReward.setListener(new RewardListener() {
        public void onLoadAd(@NonNull BMAdInfo) {
            //onLoad Callback
        }

        @Override
        public void onShowAd(@NonNull BMAdInfo) {
            //onShowAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
            //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error, @NonNull BMAdInfo) {
           //onShowFailAd Callback
        }

        @Override
        public void onCompleteAd(@NonNull BMAdInfo) {
            //onCompleteAd Callback
        }

        @Override
        public void onCloseAd(@NonNull BMAdInfo) {
            //onCloseAd Callback
        }

        @Override
        public void onClickAd(@NonNull BMAdInfo) {
            //onClickAd Callback
        }

        @Override
        public void onSkipAd(@NonNull BMAdInfo) {
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

#### *Native Ads

1. Configure the layout according to the [Native Ad Layout Setting Guide] (https://github.com/bidmad/Bidmad-Android/wiki/Native-Ad-Layout-Setting-Guide%5BENG%5D).
2. To request a native advertisement, create BidmadNativeAd, set the layout configured through registerViewForInteraction, and call the load function.
3. Add a View to the Layout created through BidmadNativeAd getNativeLayout() to expose native ads.
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

    nativeAd.setListener(new NativeListener() {
        @Override
        public void onLoadAd(@NonNull BMAdInfo) {
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
        public void onClickAd(@NonNull BMAdInfo){
            callbackStatus.append("onClickAd() Called\n");
            //onClickAd Callback
        }
    });

    nativeAd.load();
}
```

#### *AppOpen Ads

1. Call BidmadAppOpenAd constructor to request AppOpenAd.
2. When BidmadAppOpenAd detects that the Application is entering the Foreground state, it requests an ad.<br>
3. If you don't want to request any more ads, call BidmadAppOpenAd's Destroy.

*App open ads expose ads when the app state changes from background to foreground.<br>
```java
BidmadAppOpenAd mAppOpen;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_appopen);

    mAppOpen = new BidmadAppOpenAd(this, "YOUR ZONE ID");
    mAppOpen.setListener(new AppOpenListener() {
        @Override
        public void onLoadAd(@NonNull BMAdInfo) {
            //onLoadAd Callback
            mAppOpen.adShow();
        }

        @Override
        public void onShowAd(@NonNull BMAdInfo) {
	        //onShowAd Callback
        }

        @Override
        public void onLoadFailAd(BMAdError error) {
	        //onLoadFailAd Callback
        }

        @Override
        public void onShowFailAd(BMAdError error, @NonNull BMAdInfo) {
           //onShowFailAd Callback
        }

        @Override
        public void onCloseAd(@NonNull BMAdInfo) {
            //onCloseAd Callback
        }

        @Override
        public void onCloseAd(@NonNull BMAdInfo) {
            //onCloseAd Callback
            mAppOpen.adLoad();
        }
    });
}

@Override
protected void onDestroy() {
    if(mAppOpen != null) {
        // Continues depending on app activation/deactivation, and executes the code below when the app terminates
        // Or call below code if you no longer use app open ads.
        mAppOpen.destory();
        mAppOpen = null;
    }
}
```

### 3. Class Reference
#### *Banner Class Reference

- BidmadBannerAd

Function|Description
---|---
BidmadBannerAd(Activity, String)|BidmadBannerAd constructor. Set ZoneId together.
void setListener(AdViewListener)|Set listener to receive event callbacks for Banner ad.
void setInterval(int)| Set the banner ad refresh interval.(60s~120s)
void load()|Request a Banner ad. Re-requests the advertisement every set interval.
void onceLoad()|Request a Banner ad. Regardless of the set interval, only one request.
void onPause()|Pause Banner ads.
void onResume()|Resume Banner ads.

- AdViewListener

Function|Description
---|---
void onLoadAd(@NonNull BMAdInfo)|An event occurs when a banner ad is loaded.
void onLoadFailAd(BMAdError error)|An event occurs when a banner ad loading fails. You can check the error code and message with BMAError.
void onClickAd(@NonNull BMAdInfo)|An event occurs when a banner ad is clicked.
---

#### *Interstitial Class Reference

- BidmadInterstitialAd

Function|Description
---|---
BidmadInterstitialAd(Activity, String)|BidmadInterstitialAd constructor. Set ZoneId together.
void setListener(InterstitialListener)|Set listener to receive event callbacks for interstitial ad.
void load()|Request an interstitial ad.
void show()|The loaded interstitial ad is displayed on the screen.
boolean isLoaded()|Check whether interstitial ad is loaded or not.
static void setAutoReload(boolean)|Automatically request the next ad when an ad is Show. This feature will be discontinued when an ad Fail occurs. Default value is True.

- InterstitialListener

Function|Description
---|---
void onLoadAd(@NonNull BMAdInfo)|An event occurs when an interstitial ad is loaded.
void onShowAd(@NonNull BMAdInfo)|An event occurs when an interstitial ad is shown.
void onLoadFailAd(BMAdError error)|An event occurs when interstitial ad load fails. You can check the error code and message with BMAError.
void onShowFailAd(BMAdError error, @NonNull BMAdInfo)|An event occurs when interstitial ad show fails. You can check the error code and message with BMAError.
void onClickAd(@NonNull BMAdInfo)|An event occurs when a interstitial ad is Click.
void onCloseAd(@NonNull BMAdInfo)|An event occurs when a interstitial ad is Closed.
---

#### *Reward Class Reference

- BidmadRewardAd

Function|Description
---|---
BidmadRewardAd(Activity, String)|BidmadRewardAd constructor. Set ZoneId together.
void setListener(RewardListener)|Set listener to receive event callbacks for reward ad.
void load()|Request a reward ad.
void show()|The loaded reward ad is displayed on the screen.
boolean isLoaded()|Checks whether the reward ad is loaded or not.
static void setAutoReload(boolean)|Automatically request the next ad when an ad is Show. This feature will be discontinued when an ad Fail occurs. Default value is True.

- RewardListener

Function|Description
---|---
void onLoadAd(@NonNull BMAdInfo)|An event occurs when a reward ad is loaded.
void onShowAd(@NonNull BMAdInfo)|An event occurs when a reward ad is shown.
void onLoadFailAd(BMAdError error)|An event occurs when reward ad load fails, You can check the error code and message with BMAError.
void onShowFailAd(BMAdError error, @NonNull BMAdInfo)|An event occurs when reward ad show fails, You can check the error code and message with BMAError.
void onCompleteAd(@NonNull BMAdInfo)|In the reward ad, when the reward condition is satisfied.
void onSkipAd(@NonNull BMAdInfo)|In the reward ad, an event occurs when the ad ends when the reward condition is not satisfied.
void onCloseAd(@NonNull BMAdInfo)|An event occurs when the reward ad ends.
void onClickAd(@NonNull BMAdInfo)|An event occurs when a reward ad is clicked
---

#### *NativeAd Class Reference

- BidmadNativeAd

Function|Description
---|---
BidmadNativeAd(Activity, String)|BidmadNativeAd constructor. Set ZoneId together.
void setListener(NativeListener)|Set listener to receive event callbacks for native ad.
void registerViewForInteraction(Int, Int, Int, Int, Int, Int)|Register detailed elements for Layout composing native advertisement.
void load()|Request Native Ad.
FrameLayout getNativeLayout()|Get the NativeAd layout.

- NativeListener

Function|Description
---|---
void onLoadAd()|An event occurs when native ad is loaded.
void onLoadFailAd(BMAdError error)|NAn event occurs when loading of native ads fails. You can check the error code and message with BMAError.
void onClickAd()|An event occurs when a native ad is clicked.
---

#### *앱오픈광고 Class Reference

- BidmadAppOpenAd

Function|Description
---|---
BidmadAppOpenAd(Activity, String)|BidmadAppOpenAd constructor. Set the ZoneId and load the advertisement.
void setListener(AppOpenListener)|Set up a listener to receive event callbacks for AppOpen ads.
void setLifecycleListener(AppOpenLifecycleListener)|Set up a listener to receive event callbacks for the Lifecycle.
void destory()|Destroys the app open object so that it no longer requests ads.
void adLoad()|Request an AppOpen ad.
boolean isAdLoaded()|Check whether AppOpen ads are loaded.
void adShow()|Display the loaded AppOpen advertisement on the screen.
- AppOpenListener

Function|Description
---|---
void onLoadAd(@NonNull BMAdInfo)|Event occurs when the AppOpen ad is loaded.
void onShowAd(@NonNull BMAdInfo)|Event occurs when the AppOpen ad is shown.
void onLoadFailAd(BMAdError error)|Event occurs when the AppOpen ad is load fails. You can check the error code and message with BMAError.
void onShowFailAd(BMAdError error, @NonNull BMAdInfo)|Event occurs when the AppOpen ad is show fails. You can check the error code and message with BMAError.
void onCloseAd(@NonNull BMAdInfo)|Event occurs when the AppOpen ad is fails.
void onExpireAd(@NonNull BMAdInfo)|Event occurs when a show is call after 3 hours or more have elapsed after loading the AppOpen ad.

- AppOpenLifecycleListener

Function|Description
---|---
void onActivityCreated(Activity, Bundle)|The event is fired when onActivityCreated of Application.ActivityLifecycleCallbacks is called.
void onActivityStarted(Activity)|The event is fired when onActivityStarted of Application.ActivityLifecycleCallbacks is called.
void onActivityResumed(Activity)|The event is fired when onActivityResumed of Application.ActivityLifecycleCallbacks is called.
void onActivityPaused(Activity)|The event is fired when onActivityPaused of Application.ActivityLifecycleCallbacks is called.
void onActivityStopped(Activity)|The event is fired when onActivityStopped of Application.ActivityLifecycleCallbacks is called.
void onActivitySaveInstanceState(Activity, Bundle)|The event is fired when onActivitySaveInstanceState of Application.ActivityLifecycleCallbacks is called.
void onActivityDestroyed(Activity)|The event is fired when onActivityDestroyed of Application.ActivityLifecycleCallbacks is called.
---

#### *BidmadCommon Class Reference

- BidmadCommon

Function|Description
---|---
String getSDKVersion()|Get SDK version information.
void setDebugging(boolean)|When called with a true value, the log of the SDK is output.
void setGgTestDeviceid()|Register with Google TEST device to receive test ads for Google ads.
String getGgTestDeviceid()|Get the device ID registered with setGgTestDeviceid.
void initializeSdk(Activity, String)|Perform BidmadSDK initialization. Set the AppDomain.
void initializeSdk(Context, String)|Perform BidmadSDK initialization. Set the AppDomain.
void initializeSdk(Activity)|Perform BidmadSDK initialization. Set by the AppDomain in AndroidManifest.xml.
void initializeSdk(Context)|Perform BidmadSDK initialization. Set by the AppDomain in AndroidManifest.xml.
void initializeSdk(Activity, String, BidmadInitializeListener)|Perform BidmadSDK initialization. Set the AppDomain. BidmadInitializeListener conveys the initialization status.
void initializeSdk(Context, String, BidmadInitializeListener)|Perform BidmadSDK initialization. Set the AppDomain. BidmadInitializeListener conveys the initialization status.
void initializeSdk(Activity, BidmadInitializeListener)|Perform BidmadSDK initialization. Set by the AppDomain in AndroidManifest.xml. BidmadInitializeListener conveys the initialization status.
void initializeSdk(Context, BidmadInitializeListener)|Perform BidmadSDK initialization. Set by the AppDomain in AndroidManifest.xml. BidmadInitializeListener conveys the initialization status.
---

#### *AdOption Class Reference

- AdOption

Function|Description
---|---
static AdOption getInstance()|Returns a common AdOption.
boolean isUseMute()|Gets whether to mute ads.
void setUseMute(boolean)|Gets whether advertisements are muted.
boolean isChildDirected()|Return whether COPPA is applied.
void setChildDirected(boolean)|Sets whether COPPA is applied.
String getCuid()|Return the value that identifies the User.
void setCuid(String)|Sets the value that identifies the User.
boolean getUseServerSideCallback()|Verify that Server Side Callback is enabled.
void setUseServerSideCallback(boolean)|Sets whether Server Side Callback is enabled.
----

#### *AdFreeInformation Class Reference

- AdFreeInformation

Function|Description
---|---
static AdFreeInformation getInstance()| Get information about Free Ad
int getAdFreeStatus | The Ad status check.
void setOnAdFreeListener(AdFreeEventListener) |AdFreeEventListener sets a listener to receive information about ad status changes.
interface AdFreeEventListener |AdFreeEventListener receives advertisement status information.
---

### 4. Note
- [GDPR Guide](https://github.com/bidmad/Bidmad-Android/wiki/Android-GDPR-Guide-%5BENG%5D)
- [v3.0.0 API Changes](https://github.com/bidmad/Bidmad-Android/wiki/BidmadSDK-3.0.0-API-Migration-Guide-(ENG))
- [Coupang Network Ad Block Guide] (https://github.com/bidmad/Bidmad-Android/wiki/Android-Coupang-Network-Ad-Block-Guide%5BENG%5D)
