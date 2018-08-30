package com.sd.lib.blur.core.source;

abstract class BaseSource<T> implements BlurSource
{
    private final T mSource;

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
