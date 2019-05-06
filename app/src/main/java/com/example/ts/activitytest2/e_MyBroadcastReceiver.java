package com.example.ts.activitytest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class e_MyBroadcastReceiver extends BroadcastReceiver {

    //接收标准广播以及传输的数据
    @Override
    public void onReceive(Context context, Intent intent) {

        String data1 = intent.getStringExtra("extra_data");
        int data2 = intent.getIntExtra("integer_data", 0);
        boolean data3 = intent.getBooleanExtra("boolean_data", false);

        Log.d("Mylog: ", "data1=" + data1);
        Log.d("Mylog: ", "data2=" + data2);
        Log.d("Mylog: ", "data3=" + data3);

        Toast.makeText(context, "Broadcast: received in MyBroadcastReceiver", Toast.LENGTH_SHORT).show();

        // TODO 可以截断之后的广播接收
//        abortBroadcast();
    }
}
