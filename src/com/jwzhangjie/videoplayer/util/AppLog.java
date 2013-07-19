package com.jwzhangjie.videoplayer.util;

import android.util.Log;

/**
 *
 * 便于调试的类
 *
 */
public class AppLog
{
    private static boolean showLog = false;

    /**
     * 是否显示日志
     * @param enable
     */
    public static void enableLogging(boolean enable)
    {
        showLog = enable;
    }

    /**
     * 一般信息
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg)
    {
        if (showLog)
            Log.i(tag, msg);
    }

    /**
     * 错误信息
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg)
    {
        if (showLog)
            Log.e(tag , msg);
    }
    
    public static void e(String msg)
    {
    	  if (showLog)
              Log.e("jwzhangjie" , msg);
    }
    
    /**
     * 错误信息
     * @param tag
     * @param msg
     * @param tr
     */
    public static void e(String tag, String msg, Throwable tr)
    {
        if (showLog)
            Log.e(tag , msg, tr);
    }

    /**
     * 警告信息.
     * @param tag
     * @param msg
     */
    public static void w(String tag , String msg)
    {
        if (showLog)
            Log.w(tag, msg);
    }
    
    /**
     * 警告信息.
     * @param tag
     * @param msg
     */
    public static void w(String tag , String msg, Throwable tr)
    {
        if (showLog)
            Log.w(tag, msg, tr);
    }

    /**
     * debug信息.
     * @param tag
     * @param msg
     */
    public static void d(String tag , String msg)
    {
        if (showLog)
            Log.d(tag, msg);
    }

    /**
     * 详细信息
     * @param tag
     * @param msg
     */
    public static void v(String tag , String msg)
    {
        if (showLog)
            Log.v(tag, msg);
    }
}
