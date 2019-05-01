package sse309.bupt.fence.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import sse309.bupt.fence.R;

public class LoginActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_CAMERA_AND_WIFI = 123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        checkPermission();
        initToolrbar();
        Date date = new Date();
        Log.i("sun",date.getTime()+"");
    }

    private void initToolrbar() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            //权限全部具有，则延时2s退出


        } else {
            EasyPermissions.requestPermissions(this, "需要权限",
                    RC_CAMERA_AND_WIFI, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
