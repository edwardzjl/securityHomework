package com.edwardlol.autoanswertest;


import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.app.AppOpsManager;
import android.provider.ContactsContract;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Button btnDial, btnFilter;
    private EditText etTelNo;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDial = (Button) findViewById(R.id.btnDial);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        etTelNo = (EditText) findViewById(R.id.etTelNo);

        //final EditText phoneNoText = (EditText)findViewById(R.id.telNo);
        btnDial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String telNo = etTelNo.getText().toString();
                if ((telNo != null) && (!"".equals(telNo.trim()))) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNo));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "不能输入为空", Toast.LENGTH_LONG).show();
                }
            }
        });

//        btnFilter.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 Context context = getApplicationContext();
//                 Intent i = new Intent(context, PhoneListenerService.class);
//                 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                 if (isServiceRunning(context, i)) {
//                     context.startService(i);
//                 } else {
//                     context.stopService(i);
//
//                 }
//            }
//        });
//    }
//    public static boolean isServiceRunning(Context mContext,String className) {
//        boolean isRunning = false;
//        ActivityManager activityManager = (ActivityManager)
//                mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> serviceList
//                = activityManager.getRunningServices(30);
//        if (!(serviceList.size()>0)) {
//            return false;
//        }
//        for (int i=0; i<serviceList.size(); i++) {
//            if (serviceList.get(i).service.getClassName().equals(className)) {
//                isRunning = true;
//                break;
//            }
//        }
//        return isRunning;
//    }
    }

}

