# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

  -dontwarn okio.**
  -dontwarn javax.annotation.**
  -dontwarn javax.annotation.Nullable
  -dontwarn javax.annotation.ParametersAreNonnullByDefault
  -keepattributes Signature
  -dontwarn retrofit2.Platform$Java8
  -dontwarn retrofit.**
  -keep class retrofit.** { *; }
#删除文件日志
-printusage unused.txt
#混淆前后映射
-printmapping mapping.txt
-dontwarn android.support.multidex.**
-keep class android.support.multidex.**{*;}
  -keep class android.support.v4.** { *; }
  -keep interface android.support.v4.** { *; }
  # Keep the support library
  -keep class android.support.v7.** { *; }
  -keep interface android.support.v7.** { *; }
  -keep interface android.support.design.** { *; }

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.view.View

  #【保护指定的类文件和类的成员】
#  -keep class * implements android.os.Parcelable {
#      public *;
#  }
  -keepclassmembers class * implements android.os.Parcelable {
       private void writeObject(java.io.ObjectOutputStream);
       private void readObject(java.io.ObjectInputStream);
       java.lang.Object writeReplace();
       java.lang.Object readResolve();
         public static final android.os.Parcelable$Creator *;
  }
  -keepclassmembers class **.R$* {
      public static <fields>;
  }
 -keepnames class com.yb.btcinfo.repository.entity.* implements java.io.Serializable{*;}
 -keep public class * implements java.io.Serializable {
    public *;
 }
 -dontwarn java.lang.invoke.*
 -keep class com.chad.library.adapter.** {
 *;
 }
 -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
 -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
 -keepclassmembers public class * extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(android.view.View);
 }
# 华为推送

-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.huawei.hms.**{*;}
#图片加载 Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep class io.agora.**{*;}
-dontwarn com.agora.**
-keep public class com.agora.**{*;}

-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}


-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-keep class java.net.**{*;}
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# RxJava RxAndroid
-dontwarn sun.misc.**
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

#WebSocket
-keep class org.java_websocket.** { *; }
-keep class com.huanmedia.videochat.common.service.socket.WMessage{*;}
# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class com.huanmedia.videochat.repository.entity.**{*;}


##支付宝支付
-libraryjars libs/alipaySdk-20170922.jar
-dontwarn com.alipay.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

##微信支付
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
-keep class com.huanmedia.videochat.common.pay.wechat.WechatPayModel{*;}

##数据库
-keep class org.litepal.** {
    *;
}
-keep class * extends org.litepal.crud.DataSupport {
    *;
}
#哎呀美颜特效
#-keep class com.aiyaapp.aiya.**{*;}
#-keep class com.aiyaapp.aavt.**{*;}

#-keep class com.aiyaapp.aiya.IEventListener{*;}
#-keep class com.aiyaapp.MRender{*;}
#-keep class io.agora.propeller.preprocessing.VideoPreProcessing{*;}
#-keep class com.aiyaapp.aiya.WeakAnimListener{*;}
#-keep class com.aiyaapp.aiya.render.AnimListener{*;}
#-keep class com.aiyaapp.aiya.AiyaGiftEffect{*;}
#-keep class com.aiyaapp.aiya.AiyaBeauty{*;}
#-keep class com.aiyaapp.aiya.AiyaTracker{*;}
#-keep class com.aiyaapp.aiya.AiyaShaderEffect{*;}
#-keep class com.aiyaapp.aiya.AiyaEffects{*;}

