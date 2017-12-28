package com.example.csl.myappfor70;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //7.0前的设置拍照
        findViewById(R.id.btnOpenCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                Uri imageUri = Uri.fromFile(file);
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                startActivityForResult(intent,1006);
            }
        });
        //7.0后的拍照
        findViewById(R.id.btnOpenCameraNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android7.0私有权限设置
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.jph.takephoto.fileprovider", file);
                //通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                startActivityForResult(intent,1006);
            }
        });
        //7.0前的拍照裁剪
        findViewById(R.id.btnOpenCameraCut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                Uri outputUri = Uri.fromFile(file);
                Uri imageUri=Uri.fromFile(new File("/storage/emulated/0/temp/1474960080319.jpg"));
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent,1008);
            }
        });
        findViewById(R.id.btnOpenCameraCutNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                Uri outputUri = FileProvider.getUriForFile(MainActivity.this, "com.jph.takephoto.fileprovider",file);
                Uri imageUri=FileProvider.getUriForFile(MainActivity.this, "com.jph.takephoto.fileprovider", new File("/storage/emulated/0/temp/1474960080319.jpg"));
                //通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent,1008);
            }
        });
        findViewById(R.id.btnToSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityForGetPermissions.startAction(MainActivity.this);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1008) {
            Uri uri = data.getData();
            Log.e("图片地址裁剪",data.toURI());
            //to do find the path of pic

        } else if (requestCode == 1006 ) {
            Uri uri = data.getData();
            Log.e("图片地址",data.toURI());
            //to do find the path of pic
        } }
}
