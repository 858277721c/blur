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
