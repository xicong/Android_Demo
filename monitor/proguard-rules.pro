# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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


 -keep class com.wyl.monitor.analysis.**
 -keep class com.wyl.monitor.base.**
 -keep class com.wyl.monitor.database.**
 -keep class com.wyl.monitor.entity.**
 -keep class com.wyl.monitor.manage.**
 -keep class com.wyl.monitor.util.**

 -keep class pageName {#保留所有的public的构造方法不会被混淆
      public <init>;
  }
 -keepclassmembers enum * {# 保留枚举类不被混淆
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

