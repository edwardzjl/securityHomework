package com.edwardlol.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by edwardlol on 15/2/28.
 */
public class CustomBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CustomBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "WE ARE INSIDE!!!!!!!!!!!");
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Bundle bundle = intent.getExtras();
        String phoneNumber = bundle.getString("incoming_number");
        Log.i(TAG, "phoneNumber: " + phoneNumber);
    }

    public static String queryNameByNum(String num,Context context) {
        Cursor cursorOriginal =
                context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                        ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + num + "'",null,null);
        if(cursorOriginal != null) {
            if(cursorOriginal.getCount() > 1) {
                return null;
            } else {
                if(cursorOriginal.moveToFirst()) {
                    return cursorOriginal.getString(cursorOriginal.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                } else {
                    return null;
                }
            }
        }else {
            return null;
        }
    }
}
