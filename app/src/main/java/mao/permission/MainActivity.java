package mao.permission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import mao.runingpermission.R;

/**
 * Description:运行时权限：对于一些危险权限，在6.0以下的系统中，只要在AndroidManifest中注册了则可以正常运行；
 * 但针对Android6.0以上的版本，只在AndroidManifest中注册仍会崩溃，其只有在运行时进行权限处理。
 * author:jingmaolin
 * email:1271799407@qq.com.
 * phone:13342446520.
 * date: 2018/4/19.
 */

public class MainActivity extends PermissionActivity {
    private static final String TAG = "MainActivity";
    private String permissions[] = new String[]{"android.permission.ACCESS_FINE_LOCATION"
            , "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"
            ,"android.permission.READ_SMS"};

    private String dec[] = new String[]{"定位", "读写手机存储", "相机","读取短信"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void request(View v) {
        requestPermissions(permissions, dec);
    }

    @Override
    public void permissionGranted(String[] permissions, String[] dec) {
        int size = permissions.length;
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "permissionGranted: " + permissions[i]);
            if (dec != null) {
                Log.d(TAG, "permissionGranted: " + dec[i]);
            }
        }
    }

    @Override
    public void permissionDenied(String[] permissions, String[] dec) {
        int size = permissions.length;
        StringBuilder str = new StringBuilder("相关权限被被禁止");
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "permissionDenied: " + permissions[i]);
            if (dec != null) {
                Log.d(TAG, "permissionDenied: " + dec[i]);
                if (i == 0 && i == size - 1) {
                    str.append("(" + dec[i]+")");
                } else if (i == size - 1) {
                    str.append(dec[i] + ")");
                } else if (i == 0) {
                    str.append("("+dec[i]+"、");
                } else {
                    str.append(dec[i]+"、");
                }
            }
        }
        str.append("；是否去设置");
        new AlertDialog.Builder(this)
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
