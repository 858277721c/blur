package com.sd.blur;

import android.util.Log;

public class TimeLogger
{
    private final String mLogTag;
    private long mStartTime;

    public TimeLogger(String logTag)
    {
        mLogTag = logTag;
    }


    public void start()
    {
        mStartTime = System.currentTimeMillis();
    }

    public void print(String preffix)
    {
        if (preffix == null)
            preffix = "time";

        Log.i(mLogTag, preffix + ":" + (System.currentTimeMillis() - mStartTime));
    }
}