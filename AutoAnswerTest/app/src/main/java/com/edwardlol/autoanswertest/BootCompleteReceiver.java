package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/2/27.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    //运用广播开启监听这个服务
        Log.v("TAG", "开机了!");
        Intent i = new Intent(context, PhoneListenerService.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }
}