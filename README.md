# About
安卓高斯模糊库，参考了[Blurry](https://github.com/wasabeef/Blurry)和[500px-android-blur](https://github.com/500px/500px-android-blur)，并做了扩展和优化，非常感谢以上两个开源库的开发者

# Gradle
[![](https://jitpack.io/v/zj565061763/blur.svg)](https://jitpack.io/#zj565061763/blur)

# 使用API
```java
public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurApi mBlurApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur);
        mImageView = findViewById(R.id.imageview);
    }

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
            mBlurApi = BlurApiFactory.create(this);
        return mBlurApi;
    }

    @Override
    public void onClick(final View view)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(this);

        getBlurApi()
                // 设置模糊半径
                .radius(15)
                // 设置压缩倍数
                .downSampling(8)
                // 设置覆盖层颜色
                .color(Color.parseColor("#66FFFFFF"))
                // 设置是否在子线程执行
                .async(true)
                .blur(bitmap)
                .into(mImageView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /**
         * 释放资源，并取消所有和该api对象关联的子线程任务
         */
        if (mBlurApi != null)
            mBlurApi.destroy();
    }
}
```

# FBlurImageView
用法和普通的ImageView一样，只不过会把设置的图片进行模糊后展示，可以设置是否在子线程进行模糊操作，默认在UI主线程
```xml
<com.fanwe.lib.blur.view.FBlurImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/fj5"
    app:blurAsync="true"
    app:blurColor="#99FFFFFF"
    app:blurDownSampling="8"
    app:blurRadius="10" />
```

# FBlurLayout
如果需要对某个view进行动态模糊，只要用这个layout包裹一下目标view，目标view的内容发生变化后会实时模糊
```xml
<com.fanwe.lib.blur.view.FBlurLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:blurColor="#99FFFFFF"
    app:blurDownSampling="8"
    app:blurRadius="10">

    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/fj5" />

</com.fanwe.lib.blur.view.FBlurLayout>
```

# FBlurView
FBlurLayout内部动态模糊的功能就是用FBlurView实现的
注意：FBlurView相对FBlurView父布局的x和y，必须跟目标view相对目标view父布局的x和y一致，否则会出现模糊view的位置不对
```java
FBlurView blurView = findViewById(R.id.view_blur);
blurView.setBlurTarget(findViewById(R.id.ll_content));
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
     * 是否在子线程进行模糊，默认false
     *
     * @param async
     */
    void setBlurAsync(boolean async);

    /**
     * 模糊
     */
    void blur();
}
```

# 覆盖默认参数
[default_blur_settings](https://github.com/zj565061763/blur/blob/master/lib/src/main/res/values/default_blur_settings.xml)