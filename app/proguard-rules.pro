# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/rick/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#adop
-dontwarn android.webkit.**

-dontoptimize
-dontshrink
-keepattributes InnerClasse

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

-dontwarn com.mobfox.sdk.**
-dontwarn com.skplanet.tad.**
-dontwarn com.adop.sdk.**

-dontwarn com.unity3d.player.**
-keep class com.unity3d.player.** { *; }

-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**
-dontwarn com.facebook.ads.internal.**
-keeppackagenames com.facebook.*

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-dontwarn com.fsn.cauly.**
-dontwarn com.trid.tridad.**

-dontwarn com.mapps.android.**
-dontwarn com.mz.common.**
-dontwarn com.mobfox.sdk.**

-keep class javax.** { *; }
-keep class org.** { *; }

-keep class com.criteo.Criteo.** { *; }
-dontwarn com.criteo.Crite.**

#InterstitialCauly
-keep public class com.fsn.cauly.** {
public protected *;
}

-keep class com.fsn.cauly.** {
	 public *; protected *;
}
-keep class com.trid.tridad.** {
  	  public *; protected *;
}

-keep class com.criteo.sync.sdk.** {
  	  public *; protected *;
}

-keep public class com.trid.tridad.** {
public protected *;
}

#AdViewFacebook
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

#AdViewGoogle
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

#MobFox
-keep class com.mobfox.** { *; } 
-keep class com.mobfox.adapter.** {*;} 
-keep class com.mobfox.sdk.** {*;}

-dontobfuscate
-dontwarn com.squareup.okhttp.**

#DawinClick
-keep class com.skplanet.tad.** { *; }
-dontwarn com.skplanet.tad.**
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

-keepattributes Signature

-keep class com.google.gson.**{ *;}
-keep class com.google.android.gms.**{*;}
-keep class com.youappi.sdk.**{*;}
-keep interface com.youappi.sdk.**{*;}
-keep enum com.youappi.sdk.**{*;}
-keepclassmembers class * {
   @android.webkit.JavascriptInterface <methods>;
}

############### Unity Ads #################
# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}

# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}

# Keep all classes in Unity Services package
-keep class com.unity3d.services.** {
   *;
}
-dontwarn com.google.ar.core.**

############### AdColony #################
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity

############### MobPower #################
 -keep public class com.**.core.api.AdReceiver
 -keep public class com.**.core.api.AdRequestService

 ############### MoPub#################
 # MoPub Proguard Config
 # NOTE: You should also include the Android Proguard config found with the build tools:
 # $ANDROID_HOME/tools/proguard/proguard-android.txt

 # Keep public classes and methods.
 -keepclassmembers class com.mopub.** { public *; }
 -keep public class com.mopub.**
 -keep public class android.webkit.JavascriptInterface {}

 # Explicitly keep any custom event classes in any package.
 -keep class * extends com.mopub.mobileads.CustomEventBanner {}
 -keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
 -keep class * extends com.mopub.mobileads.CustomEventRewardedAd {}
 -keep class * extends com.mopub.nativeads.CustomEventNative {}

 # Keep methods that are accessed via reflection
 -keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }

 # Viewability support
 -keepclassmembers class com.integralads.avid.library.mopub.** { public *; }
 -keep public class com.integralads.avid.library.mopub.**
 -keepclassmembers class com.moat.analytics.mobile.mpub.** { public *; }
 -keep public class com.moat.analytics.mobile.mpub.**

 # Support for Android Advertiser ID.
 -keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

 # Support for Google Play Services
 # http://developer.android.com/google/play-services/setup.html
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
##########  AppLovin ##############################

-dontwarn com.AppLovin.**
-keep class com.AppLovin.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }

######################################################

##########  AdFit ##############################
-keep class com.kakao.adfit.** { *; }


##########  Vungle ##############################
-keep class com.vungle.warren.** { *; }
# Evernote
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.GcmAvailableHelper
-dontwarn com.google.android.gms.ads.identifier.**
-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver
-keep public class com.evernote.android.job.JobRescheduleService
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class com.google.android.gms.internal.** { *; }
# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

###########APP LOG REMOVE ########################
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}