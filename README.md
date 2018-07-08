# About
安卓高斯模糊库，参考了[Blurry](https://github.com/wasabeef/Blurry)和[500px-android-blur](https://github.com/500px/500px-android-blur)，并做了扩展和优化，非常感谢以上两个开源库的开发者

# Gradle
[![](https://jitpack.io/v/zj565061763/blur.svg)](https://jitpack.io/#zj565061763/blur)

# [Blur](https://github.com/zj565061763/blur/blob/master/lib/src/main/java/com/fanwe/lib/blur/core/Blur.java)接口
```java
// 创建一个Blur对象
final Blur blur = BlurFactory.create(getApplicationContext());
final Bitmap bitmapBlurred = blur
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

# FBlurLayout
如果需要对某个view进行模糊后展示，只要用这个layout包裹一下目标view即可
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="onClick"
        android:text="button" />

    <com.fanwe.lib.blur.view.FBlurLayout
        android:id="@+id/view_blur"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <ImageView
            android:id="@+id/imageview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center" />

    </com.fanwe.lib.blur.view.FBlurLayout>

</FrameLayout>
```
```java
public class BlurLayoutActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private FBlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur_layout);
        mImageView = findViewById(R.id.imageview);
        mBlurLayout = findViewById(R.id.view_blur);
    }

    @Override
    public void onClick(View v)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(getApplicationContext());
        mImageView.setImageBitmap(bitmap);

        // 执行模糊操作
        mBlurLayout.blur();
    }
}
```
# 模糊View参数设置
以上介绍的模糊view都实现了以下接口，可以进行模糊参数设置
<br>
```java
public interface BlurView
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     */
    void setBlurRadius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     */
    void setBlurDownSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     */
    void setBlurColor(int color);

    /**
     * 模糊
     */
    void blur();
}
```