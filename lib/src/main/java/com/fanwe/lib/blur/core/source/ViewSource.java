package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.lang.ref.WeakReference;

class ViewSource implements BlurSource
{
    private final WeakReference<View> mView;

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
    public void draw(final Canvas canvas, final Handler handler)
    {
        final View view = getView();
        if (view == null)
            return;

        if (Looper.myLooper() == Looper.getMainLooper())
        {
            view.draw(canvas);
        } else
        {
            synchronized (ViewSource.this)
            {
                if (handler.getLooper() != Looper.getMainLooper())
                    throw new RuntimeException("Illegal Handler:" + handler);

                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        draw(canvas, handler);

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
