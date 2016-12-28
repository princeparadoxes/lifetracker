# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/naghtarr/dev/android/sdk/tools/proguard/proguard-android.txt
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

#--------------------------------
# Android support
#--------------------------------
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.** { *; }
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

#--------------------------------
# Android Support - v4
#--------------------------------
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }


#--------------------------------
# Android Support - v7
#--------------------------------
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

#--------------------------------
# Android Support Design
#--------------------------------
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#--------------------------------
# Android
#--------------------------------
#-dontwarn com.google.auto.**
#-dontwarn com.google.common.**

#--------------------------------
# License
#--------------------------------
#error : Note: the configuration refers to the unknown class 'com.google.vending.licensing.ILicensingService'
#solution : @link http://stackoverflow.com/a/14463528
-dontnote com.google.vending.licensing.ILicensingService
-dontnote **ILicensingService

#--------------------------------
# Social Auth
#--------------------------------
-keep class org.brickred.** { *; }
-dontwarn org.brickred.*

#--------------------------------
# Retrofit, OkHttp, Gson, Picasso
#--------------------------------
#-keepattributes *Annotation*
#-keepattributes Signature
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#-dontwarn com.squareup.okhttp.**
#-dontwarn rx.**
#-dontwarn okio.**
#-dontwarn retrofit.**
#-keep class retrofit.** { *; }
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
#-keep class sun.misc.Unsafe { *; }
#-dontwarn java.nio.file.*
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#--------------------------------
# Gson
#--------------------------------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

#--------------------------------
# Retrofit2
#--------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature
# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

#--------------------------------
# OkHttp3
#--------------------------------
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keepattributes Signature
-keepattributes *Annotation*

#--------------------------------
# Okio
#--------------------------------
-dontwarn okio.**

#--------------------------------
# Moshi
#--------------------------------
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep class com.yotask.data.api.moshi.** { *; }
-keep interface com.yotask.data.api.moshi.** { *; }
-dontwarn com.squareup.moshi.**
-keepclassmembers class ** {
    @com.squareup.moshi.FromJson *;
    @com.squareup.moshi.ToJson *;
}

#--------------------------------
# Retrolambda
#--------------------------------
-dontwarn java.lang.invoke.*

#--------------------------------
# Glide
#--------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#--------------------------------
# Picasso
#--------------------------------
-dontwarn com.squareup.okhttp.**

#--------------------------------
# Google Play Services
#--------------------------------
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

-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.gcm.**{ *; }

#--------------------------------
# ButterKnife 7
#--------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#--------------------------------
# Timber
#--------------------------------
-dontwarn org.jetbrains.annotations.**

#--------------------------------
# Flurry
#--------------------------------
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**

#--------------------------------
# Pushwoosh
#--------------------------------
-dontwarn com.arellomobile.android.push.**

#--------------------------------
# ButterKnife
#--------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#--------------------------------
# Guava:
#--------------------------------
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

#--------------------------------
# RxJava:
#--------------------------------
-dontwarn sun.misc.**
-dontwarn rx.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#--------------------------------
# Joda Time
#--------------------------------
#https://stackoverflow.com/questions/14025487/proguard-didnt-compile-with-joda-time-used-in-windows
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.tz.ZoneInfoCompiler

#--------------------------------
# TestFairy
#--------------------------------
-keep class com.testfairy.** { *; }
-dontwarn com.testfairy.**
-keepattributes Exceptions, Signature, LineNumberTable

#--------------------------------
# Api classes
#--------------------------------
-keep class ru.ltst.yotask.data.api.** { *; }
-keep interface ru.ltst.yotask.data.api.** { *; }

#--------------------------------
# Testing
#--------------------------------
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

#--------------------------------
# Crashlitics
#--------------------------------
# https://docs.fabric.io/android/crashlytics/dex-and-proguard.html
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#--------------------------------
# IcePick
#--------------------------------
-dontwarn icepick.**
-keep class icepick.** { *; }
-keep class **$$Icepick { *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}
-keepnames class * { @icepick.State *;}

#--------------------------------
# Auto Value
#--------------------------------

-dontwarn javax.lang.**
-dontwarn javax.tools.**
-dontwarn javax.annotation.**
-dontwarn autovalue.shaded.com.**
#-dontwarn com.google.auto.value.**
-dontwarn com.google.auto.**

#--------------------------------
# Auto Value Gson Extension
#--------------------------------

-keep class **.AutoParcelGson_*
-keepnames @auto.parcelgson.AutoParcelGson class *

#--------------------------------
# Squareup Javapoet
#--------------------------------

-dontwarn java.nio.file.**

#--------------------------------
# Spullara Mustache
#--------------------------------
-dontwarn org.jruby.**

#--------------------------------
# Dart
#--------------------------------
-dontwarn com.f2prateek.dart.internal.**
-keep class **$$ExtraInjector { *; }
-keepclasseswithmembernames class * {
    @com.f2prateek.dart.* <fields>;
}

#--------------------------------
# Dart 2.0 & Henson
#--------------------------------
-keep class **Henson { *; }
-keep class **$$IntentBuilder { *; }
-dontwarn javax.**
-dontwarn java.**
-dontwarn com.f2prateek.dart.common.*

#--------------------------------
# Others
#--------------------------------
-dontwarn java.beans.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.model.**
-dontwarn javax.tools.**
-dontwarn com.sun.tools.javac.code.**
-dontwarn sun.misc.Unsafe
-dontnote libcore.icu.ICU

#--------------------------------
# Otto
#--------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

#--------------------------------
# InstaBug
#--------------------------------
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.instabug.**

#--------------------------------
# Lombok
#--------------------------------
-dontwarn lombok.**

#--------------------------------
# Appach
#--------------------------------
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

#--------------------------------
# Yandex Metrica
#--------------------------------
-keep class com.yandex.metrica.impl.* { *; }
-dontwarn com.yandex.metrica.impl.*
-keep class com.yandex.metrica.* { *; }
-dontwarn com.yandex.metrica.*

#--------------------------------
# Stripe
#--------------------------------
-keep class com.stripe.** { *; }

