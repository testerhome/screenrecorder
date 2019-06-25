package com.netease.testease;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.netease.testease.handler.CrashHandler;

public class UApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler handler = new CrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }

    /**
     * 创建全局变量
     * 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
     *
     * 这里使用了在Application中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     *
     */
    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

}