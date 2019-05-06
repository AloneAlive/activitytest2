package com.example.ts.activitytest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class e_BootCompleteReceiver extends BroadcastReceiver {

    //静态注册广播
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Toast.makeText(context, "Broadcast: Boot Complete", Toast.LENGTH_LONG).show();

    }
}
