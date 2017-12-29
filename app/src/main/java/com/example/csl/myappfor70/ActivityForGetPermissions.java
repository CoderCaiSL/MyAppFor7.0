package com.example.csl.myappfor70;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

/**
 * 动态获取权限
 * Created by csl on 2017/12/28.
 */

public class ActivityForGetPermissions extends AppCompatActivity{
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisssions);
        findViewById(R.id.btnGetWrite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(ActivityForGetPermissions.this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        showDialogTipUserRequestPermission();
                    }else {
                        Toast.makeText(ActivityForGetPermissions.this,"权限已经开通...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.btnOpenCameraNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //版本判断，系统大于23的时候就判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //检查权限是否获取
                    int i = ContextCompat.checkSelfPermission(ActivityForGetPermissions.this,Manifest.permission.CAMERA);
                    //权限是否已经授权 GRANTED--授权 DINED--拒绝
                    if (i != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ActivityForGetPermissions.this,new String[]{Manifest.permission.CAMERA},654);
                    }else {
                        //这个是应对360系统的权限配置
                        if(!isCameraCanUse()){
                            ActivityCompat.requestPermissions(ActivityForGetPermissions.this,new String[]{Manifest.permission.CAMERA},654);
                        }else {
                            File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                            Uri imageUri = FileProvider.getUriForFile(ActivityForGetPermissions.this, "com.jph.takephoto.fileprovider", file);
                            //通过FileProvider创建一个content类型的Uri
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                            startActivityForResult(intent,1006);
                        }
                    }
                }
            }
        });
    }
    /**
     * 启动界面
     * @param context
     */
    public static void startAction(Context context){
        Intent intent = new Intent(context,ActivityForGetPermissions.class);
        context.startActivity(intent);
    }
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("由于支付宝需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用支付宝")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    private void startRequestPermission() {
        //内存卡写入权限申请
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting(int type) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(123);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

                }
            }
        }else if (requestCode == 456){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting(456);
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    private void showDialogTipUserGoToAppSettting(final int type){
        new AlertDialog.Builder(this)
                .setTitle("是否进入设置")
                .setMessage("需要进入设置设置开启权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAppSetting(type);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
    }
    // 用户权限申请的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            for (int i = 0;i <grantResults.length;i++){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting(123);
                        } else
                            finish();
                    } else {
                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else if (requestCode == 654){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting(456);
                    } else
                        finish();
                } else {
                   if (!isCameraCanUse()){
                       showDialogTipUserGoToAppSettting(456);
                   }else {
                       Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                       File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                       if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                       Uri imageUri = FileProvider.getUriForFile(ActivityForGetPermissions.this, "com.jph.takephoto.fileprovider", file);
                       //通过FileProvider创建一个content类型的Uri
                       Intent intent = new Intent();
                       intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                       intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                       intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                       startActivityForResult(intent,1006);
                   }
                }
            }
        }
    }
    /**
     * 判断摄像头是否可用
     * 主要针对6.0 之前的版本，现在主要是依靠try...catch... 报错信息，感觉不太好，
     * 以后有更好的方法的话可适当替换
     *
     * @return
     */
    public boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }
}
