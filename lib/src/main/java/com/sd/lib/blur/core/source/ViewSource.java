package com.sd.lib.blur.core.source;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.lang.ref.WeakReference;

class ViewSource implements BlurSource
{
    private final WeakReference<View> mView;
    private Handler mHandler;

    public ViewSource(View source)
    {
        mView = new WeakReference<>(source);
    }

    private View getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public int getWidth()
    {
        final View view = getView();
        return view == null ? 0 : view.getWidth();
    }

    @Override
    public int getHeight()
    {
        final View view = getView();
        return view == null ? 0 : view.getHeight();
    }

    @Override
    public void draw(final Canvas canvas)
    {
        View view = getView();
        if (view == null)
            return;

        if (Looper.myLooper() == Looper.getMainLooper())
        {
            view.draw(canvas);
        } else
        {
            synchronized (ViewSource.this)
            {
                if (mHandler == null)
                    mHandler = new Handler(Looper.getMainLooper());

                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        draw(canvas);

                        synchronized (ViewSource.this)
                        {
                            ViewSource.this.notifyAll();
                        }
                    }
                });

                try
                {
                    ViewSource.this.wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
