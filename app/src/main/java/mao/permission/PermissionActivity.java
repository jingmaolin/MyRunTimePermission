package mao.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

/**
 * Description:动态权限获取基类
 * author:jingmaolin
 * email:1271799407@qq.com.
 * phone:13342446520.
 * date: 2018/4/19.
 */

public abstract class PermissionActivity extends AppCompatActivity {
    private String[] mGranted;
    private String[] mDenied;
    private String[] mGrantedDec;
    private String[] mDeniedDec;
    private final static int PERMISSION_REQUEST_CODE = 100;

    /**
     * 动态请求权限
     */
    public void requestPermissions(String[] permissions, String[] dec) {
        if (permissions == null || permissions.length == 0) {
            return;
        }

        if (hasAllPermissions(permissions, dec)) {
            permissionGranted(permissions, mGranted);
        } else {
            if (mGranted != null) {
                permissionGranted(mGranted, mGrantedDec);
            }
            applyPermissions();
        }
    }

    /**
     * 判断申请的权限是否都已经有
     */
    private boolean hasAllPermissions(String[] permissions, String[] dec) {
        boolean has = true;
        boolean isHaveDec = false;

        int success = 0;
        int fail = 0;

        String[] granted;
        String[] denied;
        String[] grantedDec = null;
        String[] deniedDec = null;

        granted = new String[permissions.length];
        denied = new String[permissions.length];

        if (dec != null && dec.length == permissions.length) {
            grantedDec = new String[permissions.length];
            deniedDec = new String[permissions.length];
            isHaveDec = true;
        }

        for (String per : permissions) {
            if (!(ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, per)) {
                    Log.d("hahahaha", "hasAllPermissions: " + "shouldShowRequestPermissionRationale 执行了");
                }
                has = false;
                denied[fail] = per;
                if (isHaveDec) {
                    deniedDec[fail] = dec[success + fail];
                }
                ++fail;

            } else {
                granted[success] = per;
                if (isHaveDec) {
                    grantedDec[success] = dec[success + fail];
                }
                ++success;
            }
        }

        removeNullValue(granted, grantedDec, success, denied, deniedDec, fail, isHaveDec);
        return has;
    }

    /**
     * 去除字符串为空的项
     */
    private void removeNullValue(String[] granted, String[] grantedDec, int success, String[] denied, String[] deniedDec, int fail, boolean isHaveDec) {
        this.mGranted = success > 0 ? Arrays.copyOf(granted, success) : null;
        this.mGrantedDec = success > 0 && isHaveDec ? Arrays.copyOf(grantedDec, success) : null;
        this.mDenied = fail > 0 ? Arrays.copyOf(denied, fail) : null;
        this.mDeniedDec = fail > 0 && isHaveDec ? Arrays.copyOf(deniedDec, fail) : null;
    }

    /**
     * 申请权限
     */
    private void applyPermissions() {
        //注意：第二个参数中，字符串数组的所有子项都不能为null（字符传的长度=其有效子项的个数），否则系统弹框不会显示
        ActivityCompat.requestPermissions(this, this.mDenied, PERMISSION_REQUEST_CODE);
    }

    /**
     * 申请权限结果回调
     * permission  申请的权限
     * grantResults 申请权限的结果 0 允许 -1拒绝
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && permissions.length > 0) {
            hasAllPermissions(this.mDenied, this.mDeniedDec);
            if (this.mGranted != null) {
                permissionGranted(this.mGranted, this.mGrantedDec);
            }

            if (this.mDenied != null) {
                permissionDenied(this.mDenied, this.mDeniedDec);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGranted = null;
        mDenied = null;
        mGrantedDec = null;
        mDeniedDec = null;
    }

    /**
     * 权限通过
     */
    public abstract void permissionGranted(String[] permissions, String[] permissionDec);

    /**
     * 权限拒绝
     */
    public abstract void permissionDenied(String[] permissions, String[] deniedDec);
}
