package com.edwardlol.contactstest;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by edwardlol on 15/2/28.
 */
public class CustomPhoneStateListener extends PhoneStateListener {
    private static final String TAG = "CustomPhoneStateListener";
    @Override
    public void onCallStateChanged(int state, String incomingNumber){
        Log.v(TAG, "WE ARE INSIDE!!!!!!!!!!!");
        Log.v(TAG, incomingNumber);
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "RINGING");
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, "IDLE");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, "OFFHOOK");
                break;
        }
    }
}