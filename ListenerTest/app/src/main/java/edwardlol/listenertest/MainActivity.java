package edwardlol.listenertest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private Button btnTogglePhoneListening, btnAddContact;
    public EditText etContactName, etContactNumber, etContactEmail;
    public String ContactName, ContactNumber, ContactEmail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTogglePhoneListening = (Button)findViewById(R.id.btnTogglePhoneListening);
        btnAddContact = (Button)findViewById(R.id.btnAddContact);
        etContactName = (EditText)findViewById(R.id.etContactName);
        etContactNumber = (EditText)findViewById(R.id.etContactNumber);
        etContactEmail = (EditText)findViewById(R.id.etContactEmail);

        if (isServiceRunning(getApplicationContext(),"edwardlol.listenertest.PhoneListenerService")) {
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
            @Override
            public void onClick(View v) {
                ContactName = etContactName.getText().toString();
                ContactNumber = etContactNumber.getText().toString();
                ContactEmail = etContactEmail.getText().toString();

                try {
                    CustomContactsHandler contactsHandler = new CustomContactsHandler();
                    contactsHandler.AddContacts(getApplicationContext(), ContactName, ContactNumber, ContactEmail);
                } catch(Exception e) {
                    Log.e("edwardlol", "Something went wrong with foo!", e);
                }
            }
        });

    }

    public void changeState() {
        if (isServiceRunning(getApplicationContext(),"edwardlol.listenertest.PhoneListenerService")) {
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
/* can do
    private boolean isWorking() {
        ActivityManager myManager=(ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for(int i = 0 ; i<runningService.size();i++) {
            if(runningService.get(i).service.getClassName().toString().equals("edwardlol.listenertest.PhoneListenerService")) {
                return true;
            }
        }
        return false;
    }
*/

/*
    private void answerRingCall() {
        Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
        localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        localIntent1.putExtra("state", 1);
        localIntent1.putExtra("microphone", 1);
        localIntent1.putExtra("name", "Headset");
        MainActivity.this.sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");

        Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
        localIntent2.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent1);
        MainActivity.this.sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");

        Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
        localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
        MainActivity.this.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");

        Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
        localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        localIntent4.putExtra("state", 0);
        localIntent4.putExtra("microphone", 1);
        localIntent4.putExtra("name", "Headset");
        MainActivity.this.sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
    }
    */

    /*
    public void autoAnswerPhone() {
        try {
            ITelephony itelephony = getITelephony(mTelephonyManager);

            //itelephony.silenceRinger();
            itelephony.answerRingingCall();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                TApplication.nowApplication.sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");

                intent = new Intent("android.intent.action.MEDIA_BUTTON");
                keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                TApplication.nowApplication.sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");

                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                TApplication.nowApplication.sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");

                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra("android.intent.extra.KEY_EVENT",    localKeyEvent1);
                TApplication.nowApplication.sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");

                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra("android.intent.extra.KEY_EVENT",    localKeyEvent2);
                TApplication.nowApplication.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");

                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                TApplication.nowApplication.sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
            } catch (Exception e2) {
                e2.printStackTrace();
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                TApplication.nowApplication.sendOrderedBroadcast(meidaButtonIntent, null);
            }
        }
    }
    */

}
