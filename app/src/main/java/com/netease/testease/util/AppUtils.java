package com.netease.testease.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {

    /**
     * 获取app基本信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppInfo(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        Map<String, String> params = new HashMap<>();
        for (PackageInfo info : packages) {
            if (packageName.equals(info.packageName)) {
                params.put("app_name", info.applicationInfo.loadLabel(context.getPackageManager()).toString());
                params.put("version_name", info.versionName);
                params.put("version_code", String.valueOf(info.versionCode));
                JSONObject jsonObject = new JSONObject(params);
                return jsonObject.toString();
            }
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getAppIcon(Context context, String packageName){
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for(PackageInfo info : packages){
            if (packageName.equals(info.packageName)) {
                return info.applicationInfo.loadIcon(context.getPackageManager());
            }
        }
        return null;
    }

    // 将Bitmap转换成InputStream
    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    // Drawable转换成InputStream
    public static InputStream drawable2InputStream(Drawable d) {
        Bitmap bitmap = drawable2Bitmap(d);
        return bitmap2InputStream(bitmap);
    }

    // Drawable转换成Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String getDeviceInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("brand", getSystemModel());
        params.put("os", getSystemVersion());
        //java对象变成json对象
        JSONObject jsonObject = new JSONObject(params);
        return jsonObject.toString();
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
