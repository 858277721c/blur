package com.fanwe.lib.blur.utils;

import java.util.HashMap;
import java.util.Map;

public class LockHelper
{
    private static final Map<Object, LockInfo> MAP_OBJECT = new HashMap<>();
    private static StackTraceElement sElement;

    public static synchronized void lock(Object object)
    {
        sElement = new Throwable().getStackTrace()[1];
        if (object == null)
            throw new NullPointerException("object is null");
        throwIfNotLocked(object);

        final LockInfo info = MAP_OBJECT.get(object);
        if (info == null)
        {
            final LockInfo lockInfo = new LockInfo();
            lockInfo.mLockCount++;
            MAP_OBJECT.put(object, lockInfo);
        } else
        {
            final LockInfo current = new LockInfo();
            if (info.equals(current))
                info.mLockCount++;
            else
                throw new RuntimeException(object + " object is already locked:" + info + " buy try lock:" + current);
        }
    }

    public static synchronized void unLock(Object object)
    {
        sElement = new Throwable().getStackTrace()[1];
        if (object == null)
            throw new NullPointerException("object is null");
        throwIfNotLocked(object);

        final LockInfo info = MAP_OBJECT.get(object);
        if (info == null)
            throw new RuntimeException(object + " object is not locked before buy try unlock:" + new LockInfo());

        final LockInfo current = new LockInfo();
        if (info.equals(current))
        {
            info.mLockCount--;
            if (info.mLockCount == 0)
                MAP_OBJECT.remove(object);
        } else
            throw new RuntimeException(object + " object is locked by:" + info + " but try unlock:" + current);
    }

    public static synchronized void checkLocked(Object object)
    {
        sElement = new Throwable().getStackTrace()[1];
        throwIfNotLocked(object);
    }

    public static void synchronizedSession(Object object, Runnable runnable)
    {
        sElement = new Throwable().getStackTrace()[1];
        throwIfNotLocked(object);
        if (runnable == null)
            throw new NullPointerException("runnable is null");

        lock(object);
        runnable.run();
        unLock(object);
    }

    private static void throwIfNotLocked(Object object)
    {
        if (object == null)
            throw new NullPointerException("object is null");
        try
        {
            object.wait(1);
            object.notify();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e)
        {
            throw new RuntimeException(object + " object is not locked:" + new LockInfo());
        }
    }

    private static final class LockInfo
    {
        public final long mThreadId;
        public final String mThreadName;
        public final String mThreadState;

        public final String mClassName;
        public final String mMethodName;
        public final int mLineNamber;

        public int mLockCount;

        public LockInfo()
        {
            mThreadId = Thread.currentThread().getId();
            mThreadName = Thread.currentThread().getName();
            mThreadState = String.valueOf(Thread.currentThread().getState());

            mClassName = sElement.getFileName();
            mMethodName = sElement.getMethodName();
            mLineNamber = sElement.getLineNumber();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == this)
                return true;

            if (!(obj instanceof LockInfo))
                return false;

            final LockInfo other = (LockInfo) obj;
            return mThreadId == other.mThreadId;
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder();
            sb.append("\r\n");
            sb.append("ThreadId").append("=").append(mThreadId).append("\r\n");
            sb.append("ThreadName").append("=").append(mThreadName).append("\r\n");
            sb.append("ThreadState").append("=").append(mThreadState).append("\r\n");
            sb.append("\r\n");
            sb.append("ClassName").append("=").append(mClassName).append("\r\n");
            sb.append("MethodName").append("=").append(mMethodName).append("\r\n");
            sb.append("LineNamber").append("=").append(mLineNamber).append("\r\n");
            sb.append("\r\n");

            return sb.toString();
        }
    }
}
