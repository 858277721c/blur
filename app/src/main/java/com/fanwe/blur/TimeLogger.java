package com.fanwe.blur;

import android.util.Log;

public class TimeLogger
{
    private final String mLogTag;
    private long mStartTime;

    private boolean mIsDebug;

    public TimeLogger(String logTag)
    {
        mLogTag = logTag;
    }

    public void setDebug(boolean debug)
    {
        mIsDebug = debug;
    }

    public void start()
    {
        if (mIsDebug)
            mStartTime = System.currentTimeMillis();
    }

    public void print(String preffix)
    {
        if (mIsDebug)
        {
            if (preffix == null)
                preffix = "time";

            Log.i(mLogTag, preffix + ":" + (System.currentTimeMillis() - mStartTime));
        }
    }
}