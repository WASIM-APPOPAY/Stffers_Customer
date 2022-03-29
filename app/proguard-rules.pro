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
-keepclassmembers class com.emv.qrcode.model.*.*{*;}
-keepclassmembers class com.emv.qrcode.core.*.*{*;}
-keepclassmembers class com.emv.qrcode.decoder.*.*{*;}
-keepclassmembers class com.emv.qrcode.validators*.*{*;}

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
#-addconfigurationdebugging

#-keepclasseswithmembers ,allowobfuscation class com.stuffer.stuffers.AppoPayApplication { <init>(); }
#-keepclasseswithmembers ,allowobfuscation class com.stuffer.stuffers.activity.FianceTab.ClientsActivity { <init>(); }
#-keepclasseswithmembers ,allowobfuscation class com.stuffer.stuffers.activity.FianceTab.FinanceActivity { <init>(); }




