## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.#

-optimizationpasses 5
-dontusemixedcaseclassnames
-libraryjars libs/DataVaultLib-2.3.3.13.jar
-classobfuscationdictionary 'obfuscate.txt'
-packageobfuscationdictionary 'obfuscate.txt'
-obfuscationdictionary 'obfuscate.txt'
-repackageclasses 'com.sub.stuffer'
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification
# Keep all Fragments in this package, which are used by reflection.
-keep,allowobfuscation,allowoptimization public class com.stuffer.stuffers.fragments.bottom.* extends androidx.fragment.app.Fragment
-keep,allowobfuscation,allowoptimization public class com.stuffer.stuffers.fragments.bottom_fragment.* extends com.google.android.material.bottomsheet.BottomSheetDialogFragment

    #-keep class com.keepe.** { *; }
    #-keepclassmembers class io.card.** { *;}
    #-dontwarn io.card.payment.CardIOActivity

-dontwarn javax.annotation.**

-dontwarn android.app.**
-dontwarn android.support.**
-dontwarn android.view.**
-dontwarn android.widget.**
-dontwarn androidx.core.app.**

-dontwarn com.google.common.primitives.**

-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2

-keepclasseswithmembernames class * {
 native <methods>;
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

#-keepclassmembers class * extends android.app.Activity {
 #public void *(android.view.View);
#}
-keep public class com.stuffer.stuffers.asyntask.MyJobIntentService

-keepclasseswithmembernames class * extends androidx.core.app.JobIntentService




-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keep public class net.sqlcipher.*.* {
 *;
}

-keep public class net.sqlcipher.database.*.* {
 *;
}
-keepclassmembers class com.stuffer.stuffers.models.*.* { *; }
-keepclassmembers class com.stuffer.stuffers.fragments.bottom.chatmodel.*.* { *; }
-keepclassmembers class com.stuffer.stuffers.fragments.bottom.chatnotification.*.* { *; }
-keepclassmembers class com.stuffer.stuffers.activity.contact.*.* { *; }
-keep class com.emv.qrcode.model.*.*{*;}
-keep class com.emv.qrcode.core.*.*{*;}
-keep class com.emv.qrcode.decoder.*.*{*;}
-keep class com.emv.qrcode.validators*.*{*;}
#-dontoptimize


-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.javax.net.ssl.**
-dontwarn org.openjsse.net.ssl.**
-dontwarn javax.net.ssl.**
-dontwarn com.squareup.okhttp.Cache
-dontwarn com.squareup.okhttp.OkHttpClient
-dontwarn com.squareup.okhttp.OkUrlFactory
-dontwarn lombok.Generated
-dontwarn org.aspectj.lang.NoAspectBoundException
-dontwarn org.aspectj.lang.annotation.AfterThrowing
-dontwarn org.aspectj.lang.annotation.Aspect
-keep class com.google.gson.** { *; }

-keepclassmembers class * extends android.os.AsyncTask {
	protected void onPreExecute();
	protected *** doInBackground(...);
	protected void onPostExecute(...);
}
-keep public class * implements java.lang.reflect.Type

#-keep class java.**,javax.**,com.sun.**,android.** {
 #  static final %                *;
  # static final java.lang.String *;
  #*;
#}
#keep java.lang.String.**
-keep class * {
    java.lang.String *;
}

-keep class com.google.gson.** { *; }

-keepclassmembers class * extends android.os.AsyncTask {
	protected void onPreExecute();
	protected *** doInBackground(...);
	protected void onPostExecute(...);
}
-keep public class * implements java.lang.reflect.Type


-keep class * {
    java.lang.String *;
}

-dontwarn com.onesignal.**

# These 2 methods are called with reflection.
-keep class com.google.android.gms.common.api.GoogleApiClient {
    void connect();
    void disconnect();
}

# Need to keep as these 2 methods are called with reflection from com.onesignal.PushRegistratorFCM
-keep class com.google.firebase.iid.FirebaseInstanceId.* {
    static com.google.firebase.iid.FirebaseInstanceId.* getInstance(com.google.firebase.FirebaseApp);
    java.lang.String getToken(java.lang.String, java.lang.String);
}

-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}

# Observer backcall methods are called with reflection
-keep class com.onesignal.OSSubscriptionState {
    void changed(com.onesignal.OSPermissionState);
}

-keep class com.onesignal.OSPermissionChangedInternalObserver {
    void changed(com.onesignal.OSPermissionState);
}

-keep class com.onesignal.OSSubscriptionChangedInternalObserver {
    void changed(com.onesignal.OSSubscriptionState);
}

-keep class com.onesignal.OSEmailSubscriptionChangedInternalObserver {
    void changed(com.onesignal.OSEmailSubscriptionState);
}

-keep class com.onesignal.OSSMSSubscriptionChangedInternalObserver {
    void changed(com.onesignal.OSSMSSubscriptionState);
}

-keep class ** implements com.onesignal.OSPermissionObserver {
    void onOSPermissionChanged(com.onesignal.OSPermissionStateChanges);
}

-keep class ** implements com.onesignal.OSSubscriptionObserver {
    void onOSSubscriptionChanged(com.onesignal.OSSubscriptionStateChanges);
}

-keep class ** implements com.onesignal.OSEmailSubscriptionObserver {
    void onOSEmailSubscriptionChanged(com.onesignal.OSEmailSubscriptionStateChanges);
}

-keep class ** implements com.onesignal.OSSMSSubscriptionObserver {
    void onOSEmailSubscriptionChanged(com.onesignal.OSSMSSubscriptionStateChanges);
}

-keep class com.onesignal.shortcutbadger.impl.AdwHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ApexHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.AsusHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.DefaultBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.EverythingMeHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.HuaweiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.LGHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NewHtcHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NovaHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.OPPOHomeBader { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SamsungHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SonyHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.VivoHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.XiaomiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ZukHomeBadger { <init>(...); }

-dontwarn com.coremedia.iso.boxes.SampleToChunkBox$Entry
-dontwarn com.coremedia.iso.boxes.TimeToSampleBox$Entry


-dontwarn com.amazon.**

-dontwarn com.huawei.**

-dontwarn com.coremedia.iso.IsoFile
-dontwarn com.coremedia.iso.IsoTypeWriter
-dontwarn com.coremedia.iso.boxes.AbstractMediaHeaderBox
-dontwarn com.coremedia.iso.boxes.Box
-dontwarn com.coremedia.iso.boxes.DataEntryUrlBox
-dontwarn com.coremedia.iso.boxes.DataInformationBox
-dontwarn com.coremedia.iso.boxes.DataReferenceBox
-dontwarn com.coremedia.iso.boxes.FileTypeBox
-dontwarn com.coremedia.iso.boxes.HandlerBox
-dontwarn com.coremedia.iso.boxes.MediaBox
-dontwarn com.coremedia.iso.boxes.MediaHeaderBox
-dontwarn com.coremedia.iso.boxes.MediaInformationBox
-dontwarn com.coremedia.iso.boxes.MovieBox
-dontwarn com.coremedia.iso.boxes.MovieHeaderBox
-dontwarn com.coremedia.iso.boxes.SampleDescriptionBox
-dontwarn com.coremedia.iso.boxes.SampleSizeBox
-dontwarn com.coremedia.iso.boxes.SampleTableBox
#-dontwarn com.coremedia.iso.boxes.SampleToChunkBox$Entry
-dontwarn com.coremedia.iso.boxes.SampleToChunkBox
-dontwarn com.coremedia.iso.boxes.SoundMediaHeaderBox
-dontwarn com.coremedia.iso.boxes.StaticChunkOffsetBox
-dontwarn com.coremedia.iso.boxes.SyncSampleBox
#-dontwarn com.coremedia.iso.boxes.TimeToSampleBox$Entry
-dontwarn com.coremedia.iso.boxes.TimeToSampleBox
-dontwarn com.coremedia.iso.boxes.TrackBox
-dontwarn com.coremedia.iso.boxes.TrackHeaderBox
-dontwarn com.coremedia.iso.boxes.VideoMediaHeaderBox
-dontwarn com.coremedia.iso.boxes.sampleentry.AudioSampleEntry
-dontwarn com.coremedia.iso.boxes.sampleentry.VisualSampleEntry
-dontwarn com.fasterxml.jackson.annotation.JsonFormat
-dontwarn com.fasterxml.jackson.databind.JsonDeserializer
-dontwarn com.fasterxml.jackson.databind.JsonSerializer
-dontwarn com.fasterxml.jackson.databind.module.SimpleModule
-dontwarn com.fasterxml.jackson.databind.ser.ContextualSerializer
-dontwarn com.fasterxml.jackson.databind.ser.std.StdSerializer
-dontwarn com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox
-dontwarn com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig
-dontwarn com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor
-dontwarn com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor
-dontwarn com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor
-dontwarn com.googlecode.mp4parser.util.Matrix
-dontwarn com.mp4parser.iso14496.part15.AvcConfigurationBox
-dontwarn freemarker.template.Configuration
-dontwarn freemarker.template.Template
-dontwarn freemarker.template.Version
-dontwarn okhttp3.internal.Version
-dontwarn okhttp3.internal.http.HttpDate
-dontwarn okhttp3.internal.http.UnrepeatableRequestBody

# Proguard ends up removing this class even if it is used in AndroidManifest.xml so force keeping it.
-keep public class com.onesignal.ADMMessageHandler {*;}

-keep public class com.onesignal.ADMMessageHandlerJob {*;}

# OSRemoteNotificationReceivedHandler is an interface designed to be extend then referenced in the
#    app's AndroidManifest.xml as a meta-data tag.
# This doesn't count as a hard reference so this entry is required.
-keep class ** implements com.onesignal.OneSignal$OSRemoteNotificationReceivedHandler {
   void remoteNotificationReceived(android.content.Context, com.onesignal.OSNotificationReceivedEvent);
}

-keep class com.onesignal.JobIntentService$* {*;}

-keep class com.onesignal.OneSignalUnityProxy.* {*;}
-keep class com.stuffer.stuffers.commonChat.chatModel.** { *; }
-keep class com.stuffer.stuffers.widget.** { *; }






