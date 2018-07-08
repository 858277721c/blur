# About
实现安卓高斯模糊，参考了[Blurry](https://github.com/wasabeef/Blurry)和[500px-android-blur](https://github.com/500px/500px-android-blur)，并做了扩展和优化，非常感谢以上两个开源库的开发者

# Gradle
[![](https://jitpack.io/v/zj565061763/blur.svg)](https://jitpack.io/#zj565061763/blur)

# 简单使用
```java
final Bitmap blurBitmap = new CompatBlur(this)
        // 设置模糊半径，默认10
        .setRadius(10)
        // 设置压缩倍数，默认8
        .setDownSampling(8)
        // 设置覆盖层颜色，默认透明
        .setColor(Color.parseColor("#66FFFFFF"))
        // 执行模糊操作，得到模糊的Bitmap
        .blur(bitmap);
```

# FBlurImageView
用法和普通的ImageView一样，只不过会把设置的图片进行模糊后展示，模糊的操作是在子线程进行的，所以这边不会阻塞主线程
```xml
<com.fanwe.lib.blur.view.FBlurImageView
        android:id="@+id/imageview"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center" />
```
```java
ImageView imageView = findViewById(R.id.imageview);
imageView.setImageResource(R.drawable.fj5);
```
