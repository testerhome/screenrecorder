package com.netease.testease.http;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.testease.util.AppUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class CoordinateServer extends NanoHTTPD {

    private Handler mHandler;
    private Activity mContext;

    public CoordinateServer(Activity context, int port, Handler mHandler) {
        super(port);
        this.mContext = context;
        this.mHandler = mHandler;
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> headers, Map<String, String> params,
                          Map<String, String> datas) {
        System.out.print("data:" + datas.get("postData"));
        String requestStr = datas.get("postData");
        return doResponse(requestStr);
    }

    private Response doResponse(String requestStr) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retMap = new HashMap<>();
        retMap.put("ret", "0");
        try {
            Map<String, String> reqMap = mapper.readValue(requestStr, Map.class);

            Message message = new Message();
            message.what = 0;
            Bundle bundle = new Bundle();

            switch (reqMap.get("action")){
                case "start_coor":
                    bundle.putString("action", "start_coor");
                    break;
                case "start_app":
                    bundle.putString("action", "start_app");
                    break;
                case "coor":
                    bundle.putString("action", "coor");
                    bundle.putString("data", reqMap.get("data"));
                    bundle.putString("type", reqMap.get("type"));
                    break;
                case "stop_coor":
                    bundle.putString("action", "stop_coor");
                    break;
                case "device_info":
                    return newFixedLengthResponse(AppUtils.getDeviceInfo());
                case "app_info":
                    return newFixedLengthResponse(AppUtils.getAppInfo(mContext, reqMap.get("package_name")));
                case "app_icon":
                    Drawable icon = AppUtils.getAppIcon(mContext, reqMap.get("package_name"));
                    InputStream data = AppUtils.drawable2InputStream(icon);
                    return newFixedLengthResponse(Response.Status.OK, "image/jpeg", data, data.available());
                default:
                    retMap.put("ret", "-1");
            }
            if("0".equals(retMap.get("ret"))){
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
            return newFixedLengthResponse(mapper.writeValueAsString(retMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
