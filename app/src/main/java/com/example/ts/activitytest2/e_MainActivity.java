package com.example.ts.activitytest2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class e_MainActivity extends e_BaseActivity {

    private IntentFilter intentFilter;

    private NetworkChangeReceiver networkChangeReceiver;

    private LocalReceiver localReceiver;

    private LocalBroadcastManager localBroadcastManager;

    //动态注册广播
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e__main);

        //fixme  全局广播 *************************************************
        //动态接收数据连接的广播
        intentFilter = new IntentFilter();      //过滤器
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");  //添加行为
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        //发送自定义标准广播(包含数据)
        Button button1 = findViewById(R.id.main_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                intent.putExtra("integer_data", 1018);
                intent.putExtra("boolean_data", true);
                intent.putExtra("extra_data", "This is extra data");

                Log.d("Mylog", "intent.getFlages is " + intent.getFlags());
                //TODO  如果打印日志: Background execution not allowed: receiving Intent
                //TODO 则需要设置指定接收包名(只能指定一个包)
//                intent.setPackage("com.example.ts.activitytest1");
//                intent.setPackage("com.example.ts.activitytest2");

                //TODO 发送标准广播
//                sendBroadcast(intent);
                //TODO 发送有序广播(此时可以设置接收器的优先级)
                sendOrderedBroadcast(intent, null);
            }
        });

        //TODO 此处使用动态注册广播接收,可以解决静态注册只能指定接收.此处可以在其他app同时接收到广播.
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.example.broadcasttest.MY_BROADCAST");
        intentFilter2.setPriority(100);   //设置优先级,此时可以在第二个广播接收前接收到
        e_MyBroadcastReceiver myBroadcastReceiver = new e_MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter2);

        //fixme  本地广播 *************************************************
        localBroadcastManager = LocalBroadcastManager.getInstance(this);  //获取实例
        Button button2 = findViewById(R.id.main_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.broadcasttest.LOACL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        //本地接收
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.example.broadcasttest.LOACL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter3);

        //fixme  跳转到login活动 *************************************************
        Button button3 = findViewById(R.id.main_button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(e_MainActivity.this, e_LoginActivity.class);
                startActivity(intent);
            }
        });

        //fixme  强制下线 *************************************************
        Button button4 = findViewById(R.id.main_button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(e_MainActivity.this, "Broadcast: send offline broadcast", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.example.broadcasttest.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });

        //fixme  打电话,并且申请运行时权限 *************************************************
        Button button5 = findViewById(R.id.main_button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(e_MainActivity.this, "Permission: make call", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(e_MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(e_MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }
        });

        //fixme  跳转到f_contacts_view活动 *************************************************
        Button button6 = findViewById(R.id.main_button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(e_MainActivity.this, "Jump to f_contacts_view Activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(e_MainActivity.this, f_ContactsViewActivity.class);
                startActivity(intent);
            }
        });

        //fixme  发出一条通知 *************************************************
        Button button7 = findViewById(R.id.main_button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 如果版本是O或者Q
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //TODO 添加通知的点击功能
                    Intent intent = new Intent(e_MainActivity.this, g_NotificationActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(e_MainActivity.this, 0, intent, 0);

                    //TODO 使用channel进行通知
                    String id = "channel1";  //channel的ID
                    CharSequence name = "channel1"; //channel的名字
                    String desc = "The description of channel1";   //描述
                    int importance = NotificationManager.IMPORTANCE_LOW;   //重要性
                    NotificationChannel channel = new NotificationChannel(id, name, importance);
                    channel.setDescription(desc);


                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);   //添加channel
                    //TODO 第一种方法：点击通知事件后，通知会在状态栏消失
//                    manager.cancel(1);

                    //TODO 使用当前的ACTIVITY代替this上下文,并却加上channel ID
                    Notification notification = new NotificationCompat.Builder(e_MainActivity.this, id)
                            .setContentTitle("This is a content title")    //通知的标题
//                            .setContentText("This is content text. Learn how to build notifications, send and sync data, and use voice actions. Get the" +
//                                    " official Android IDE and developer tools to build app for Android.")     //通知的内容
                            //TODO  显示长的内容
//                            .setStyle(new NotificationCompat.BigTextStyle().bigText("This is content text. Learn how to build notifications, send and sync data," +
//                                    " and use voice actions. Get the official Android IDE and developer tools to build app for Android."))
                            //TODO 状态栏通知显示图片
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(
                                    getResources(), R.drawable.test1
                            )))
                            //TODO 设置优先级
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(System.currentTimeMillis())    //通知悲怆件的时间，毫秒为单位
                            .setSmallIcon(R.mipmap.ic_launcher)     //设置通知的小图标
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))  //设置通知的大图标
                            .setContentIntent(pendingIntent)    //TODO 添加intent事件
                            .setAutoCancel(true)     //TODO 第二种方法：点击通知事件后，通知会在状态栏消失
//                            .setSound(Uri.fromFile(new File("...")))   //发出通知的时候播放音频
//                            .setVibrate(new long[] {0, 1000, 1000, 1000})   //TODO （没有用）发出震动，设置手机静止和震动的时长，以毫秒为单位（静止时长、震动时长、静止时长、震动时长），需要权限
//                            .setLights(Color.BLUE, 1000, 1000)   //TODO （没有用）闪烁LED灯
                            .setDefaults(NotificationCompat.DEFAULT_ALL)    //TODO 全部使用默认
                            .build();
                    manager.notify(1, notification);   //显示一个通知
                }

            }
        });

        //fixme  跳转到调用摄像头活动 *************************************************
        Button button8 = findViewById(R.id.main_button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(e_MainActivity.this, "进入图片显示活动", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(e_MainActivity.this, g_CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);     //打电话,需要申请权限
//                    Intent intent = new Intent(Intent.ACTION_DIAL);   //打电话,不需要申请权限

            intent.setData(Uri.parse("tel:10086"));  //访问call,拨号
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {   //判断是否授权权限
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
            系统广播接收
         */
    class NetworkChangeReceiver extends BroadcastReceiver {

        //重写广播的接收方法
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Broadcast: network changes", Toast.LENGTH_SHORT).show();   //广播内容创建一个提示，如果切换数据会提示

            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "Broadcast: network is available", Toast.LENGTH_SHORT).show();    //连接数据连接
            } else {
                Toast.makeText(context, "Broadcast: network is unavailable", Toast.LENGTH_SHORT).show();   //如果断开数据连接
            }
        }
    }

    /*
        本地广播接收(只能通过动态注册接收)
     */
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Broadcast: received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除本地广播接收器
        localBroadcastManager.unregisterReceiver(localReceiver);
        Toast.makeText(e_MainActivity.this, "Destory e_MainActivity", Toast.LENGTH_SHORT).show();
    }
}
