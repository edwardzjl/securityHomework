package com.edwardlol.test;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.edwardlol.test.CustomContactsHandler;

/**
 * Created by edwardlol on 15/2/28.
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    private static final String TAG = "CustomPhoneStateListener";
    Context context = MyApplication.getInstance();
    CustomContactsHandler handler = new CustomContactsHandler();

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Log.i(TAG, "WE ARE INSIDE!!!!!!!!!!!");
        Log.i(TAG, incomingNumber);
        switch (state) {
            //响铃
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i("RINGING", incomingNumber);
                if (handler.queryByNumber(incomingNumber, context)) {
                    Log.i("edwardlol", "in contacts");
                } else {
                    Log.i("edwardlol", "not in contacts");
                }
                break;
            //挂断
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i("IDLE", incomingNumber);
                break;
            //接通
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i("OFFHOOK", incomingNumber);
                break;
        }
    }


}