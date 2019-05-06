package com.example.ts.activitytest2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class g_CameraActivity extends AppCompatActivity {

    /**
     * 拍照
     */
    public static final int TAKE_PHOTO = 1;

    /**
     * 选择图片
     */
    public static final int CHOOSE_PHOTO = 2;

    /**
     * 拍好的图片
     */
    private ImageView picture;

    /**
     * 存储图片的Uri
     */
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g__camera);

        Button takePhoto = findViewById(R.id.g_take_photo);
        picture = findViewById(R.id.g_picture);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 创建File对象，用于存储拍照后的图片，存储到SD的应用关联缓存目录下，路径是sdcard/Android/data/<packageName>/cache
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();   //创建新的文件
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //TODO 保存Uri标识,判断是否版本低于7.0,使用两种转换Uri方法
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(g_CameraActivity.this,
                            "com.example.cameraalbumtest.fileprovider", outputImage);    //保存图片到Uri
                    //TODO content://com.example.cameraalbumtest.fileprovider/my_images/Android/data/com.example.ts.activitytest2/cache/output_image.jpg
                    Log.d("Mylog", "imageUri(>=24) is " + imageUri);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                    Log.d("Mylog", "imageUri(<24) is " + imageUri);
                }

                //启动摄像头
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE_SECURE");   //TODO 拍照的Intent
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);   //保存数据到imageUri，指定图片输出地址
                startActivityForResult(intent, TAKE_PHOTO);   //启动（请求code是1）
            }
        });

        Button button2 = findViewById(R.id.g_choose_from_album);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(g_CameraActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(g_CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case TAKE_PHOTO:   //匹配请求code 1
                if (resultCode == RESULT_OK) {   //成功code
                    try {
                        //将拍摄的图片显示出来,先解码
                        // TODO 没有权限，所以抛异常
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Log.d("Mylog", "Show bitmap(<24) is " + bitmap + ", imageUri is " + imageUri);
                        picture.setImageBitmap(bitmap);   //显示ImageView
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                //TODO
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本
                    if (Build.VERSION.SDK_INT >=19) {
                        //4.4以上版本
                        handleImageOnKitKat(data);
                    } else {
                        Toast.makeText(g_CameraActivity.this, "Mylog: Your phone version is lower than Android 4.4", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开Album相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);   //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(g_CameraActivity.this, "Mylog: You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(g_CameraActivity.this, uri)) {
            //如果是Document类型的Uri，则通过document id处理
            String docid = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docid.split(":")[1];   //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                //TODO 获取image路径
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docid));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径
            imagePath = uri.getPath();
        }
        //TODO 显示图片
        displayImage(imagePath);
    }

    /**
     * 获取图片真实路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(g_CameraActivity.this, "Mylog: Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
