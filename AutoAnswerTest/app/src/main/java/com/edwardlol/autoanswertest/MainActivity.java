package com.edwardlol.autoanswertest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Button btnTogglePhoneListening, btnAddContact;
    public EditText etContactName, etContactNumber, etContactEmail;
    private String ContactName, ContactNumber, ContactEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTogglePhoneListening = (Button)findViewById(R.id.btnTogglePhoneListening);
        btnAddContact = (Button) this.findViewById(R.id.btnAddContact);
        etContactName = (EditText)findViewById(R.id.etContactName);
        etContactNumber = (EditText)findViewById(R.id.etContactNumber);
        etContactEmail = (EditText)findViewById(R.id.etContactEmail);

        if (isServiceRunning(getApplicationContext(),"com.edwardlol.autoanswertest.PhoneListenerService")) {
            btnTogglePhoneListening.setText("stop");
        } else {
            btnTogglePhoneListening.setText("start");
        }

        btnTogglePhoneListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContactName = etContactName.getText().toString();
                ContactNumber = etContactNumber.getText().toString();
                ContactEmail = etContactEmail.getText().toString();
                if (ContactName == null || ContactName.length() <= 0) {
                    Toast.makeText(MainActivity.this, "must input content name", Toast.LENGTH_SHORT).show();
                } else if (ContactNumber == null || ContactNumber.length() <= 0) {
                    Toast.makeText(MainActivity.this, "must input content number", Toast.LENGTH_SHORT).show();
                } else if (ContactEmail == null || ContactEmail.length() <= 0) {
                    Toast.makeText(MainActivity.this, "must input content email", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        CustomContactsHandler contactsHandler = new CustomContactsHandler();
                        contactsHandler.AddContacts(getApplicationContext(), ContactName, ContactNumber, ContactEmail);
                    } catch(Exception e) {
                        Log.e("edwardlol", "Something went wrong with foo!", e);
                    }

                }
            }
        });

    }

    public void changeState() {
        if (isServiceRunning(getApplicationContext(),"com.edwardlol.autoanswertest.PhoneListenerService")) {
            Log.e("TAG","is running, now stop");
            Intent i = new Intent(getApplicationContext(), PhoneListenerService.class);
            stopService(i);
            btnTogglePhoneListening.setText("start");
        } else {
            Log.e("TAG","is not running, now start");
            Intent i = new Intent(getApplicationContext(), PhoneListenerService.class);
            startService(i);
            btnTogglePhoneListening.setText("stop");
        }
    }

    private boolean isServiceRunning(Context context,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}

