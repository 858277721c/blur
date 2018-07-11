package com.fanwe.lib.blur.core.source;

abstract class BaseSource<T> implements BlurSource
{
    private T mSource;

    public BaseSource(T source)
    {
        if (source == null)
            throw new NullPointerException("source is null");
        mSource = source;
    }

    protected final T getSource()
    {
        return mSource;
    }
}
