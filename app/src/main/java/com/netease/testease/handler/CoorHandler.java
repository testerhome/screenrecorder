package com.netease.testease.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.netease.testease.MainActivity;
import com.netease.testease.view.FloatView;

public class CoorHandler extends Handler {
    private final String TAG = "Handler";
    private FloatView mLayout;
    private MainActivity mActivity;

    public CoorHandler(FloatView mLayout, MainActivity mActivity){
        this.mLayout = mLayout;
        this.mActivity = mActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle;
        String action;
        String type;
        String data;
        String[] coorData;
        if (this.mLayout == null || !this.mLayout.isShown()){
            this.mActivity.showFloatView();
        }
        switch (msg.what){
            case 0:
                bundle = msg.getData();
                action = bundle.getString("action", null);
                type = bundle.getString("type", null);
                switch (action){
                    case "start_record":
                        if (mActivity != null){
                            mActivity.startRecord();
                        }else {
                            Log.i(TAG, "service is down, start recording failed!");
                        }
                        break;
                    case "stop_record":
                        if (mActivity != null){
                            mActivity.stopRecorder();
                        }else {
                            Log.i(TAG, "service is down, start recording failed!");
                        }
                        break;
                    case "start_coor":
                        mActivity.moveTaskToBack(true);
                        break;
                    case "start_app":
                        mActivity.moveTaskToBack(true);
                        break;
                    case "coor":
                        data = bundle.getString("data");
                        assert data != null;
                        coorData = data.substring(1, data.length() - 1).split(",");
                        if ("click".equals(type)){
                            int x = (int)Float.parseFloat(coorData[0]);
                            int y = (int)Float.parseFloat(coorData[1]);
                            mLayout.setVisibility(View.VISIBLE);
                            mLayout.updateView(x, y, x, y);
                            Message message = new Message();
                            message.what = 1;
                            sendMessageDelayed(message, 600);
                        }else {
                            int fX = Integer.parseInt(coorData[0]);
                            int fY = Integer.parseInt(coorData[1]);
                            int tX = Integer.parseInt(coorData[2]);
                            int tY = Integer.parseInt(coorData[3]);
                            mLayout.setVisibility(View.VISIBLE);
                            mLayout.updateView(fX, fY, tX, tY);
                            Message message = new Message();
                            message.what = 1;
                            sendMessageDelayed(message, 600);
                        }
                    case "stop_coor":
                        bundle.putString("action", "stop_coor");
                        break;
                }
                break;
            case 1:
                mLayout.clearView();
                break;
        }
    }

    public void updateFloatView(FloatView mLayout){
        this.mLayout = mLayout;
    }
}
