# About
安卓高斯模糊库，参考了[Blurry](https://github.com/wasabeef/Blurry)和[500px-android-blur](https://github.com/500px/500px-android-blur)，并做了扩展和优化，非常感谢以上两个开源库的开发者

# Gradle
[![](https://jitpack.io/v/zj565061763/blur.svg)](https://jitpack.io/#zj565061763/blur)

# 使用API
```java
public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurInvoker mBlurInvoker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur);
        mImageView = findViewById(R.id.imageview);
    }

    @Override
    public void onClick(final View view)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(getApplicationContext());

        mBlurInvoker = BlurApiFactory.create(getApplicationContext())
                // 设置模糊半径，默认10
                .radius(10)
                // 设置压缩倍数，默认8
                .downSampling(8)
                // 设置覆盖层颜色，默认透明
                .color(Color.parseColor("#66FFFFFF"))
                .blur(bitmap)
                // 设置是否在子线程执行
                .async(true)
                .into(mImageView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // 如果有未完成的子线程任务，取消任务
        if (mBlurInvoker != null)
            mBlurInvoker.cancelAsync();
    }
}
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
```
```java
ImageView imageView = findViewById(R.id.imageview);
imageView.setImageResource(R.drawable.fj5);
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