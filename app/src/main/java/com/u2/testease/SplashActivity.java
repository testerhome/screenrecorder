package com.u2.testease;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.u2.testease.R;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity {

    String mPackName = "com.huawei.liwenzhi.weixinasr";
    AlertDialog mPermissionDialog;
    List<String> mPermissionList = new ArrayList();

    String overlayPermission = "android.permission.SYSTEM_ALERT_WINDOW";
    String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int ALERT_PERMISSION = 756232212;
    private View textView;

    private void cancelPermissionDialog() {
        this.mPermissionDialog.cancel();
    }

    @TargetApi(23)
    private void initPermission() {
        this.mPermissionList.clear();
        int i = 0;
        while (i < this.permissions.length) {
            if (ActivityCompat.checkSelfPermission(this, this.permissions[i]) != 0)
                this.mPermissionList.add(this.permissions[i]);
            i += 1;
        }
        if (this.mPermissionList.size() > 0) {
            requestPermissions(this.permissions, 100);
            return;
        }
        if (Settings.canDrawOverlays(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        requestAlertWindowPermission();
    }

    @TargetApi(23)
    private void requestAlertWindowPermission() {
        Intent localIntent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        if (Build.VERSION.SDK_INT < 26)
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(localIntent, ALERT_PERMISSION);
    }

    private void showPermissionDialog() {
        if (this.mPermissionDialog == null)
            this.mPermissionDialog = new AlertDialog.Builder(this).setMessage("已禁用权限，请手动授予").setPositiveButton("设置", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    SplashActivity.this.cancelPermissionDialog();
                    paramDialogInterface = (DialogInterface) new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + SplashActivity.this.mPackName));
                    SplashActivity.this.startActivity((Intent) paramDialogInterface);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    SplashActivity.this.cancelPermissionDialog();
                }
            }).create();
        this.mPermissionDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALERT_PERMISSION) {
            if ((Settings.canDrawOverlays(this))) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                exitPermissionDeny();
            }
        }
        System.out.println("ffffff");
    }

    private void exitPermissionDeny(){
        Toast.makeText(this, "权限获取失败，应用即将退出", Toast.LENGTH_SHORT).show();
        this.textView.postDelayed(() -> finish(), 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.textView = findViewById(R.id.img_launch);
        initPermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean getPermission = true;
        if (100 == requestCode) {
            for (int grant : grantResults){
                if(grant == -1){
                    getPermission = false;
                }
            }
            if(!getPermission){
                exitPermissionDeny();
            }
            if ((Build.VERSION.SDK_INT >= 23) && (!(Settings.canDrawOverlays(this)))) {
                requestAlertWindowPermission();
                return;
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
