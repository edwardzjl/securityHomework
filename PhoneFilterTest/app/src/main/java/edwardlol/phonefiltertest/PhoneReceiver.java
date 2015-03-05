package edwardlol.phonefiltertest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by edwardlol on 15/3/5.
 */
public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        //设置一个监听器

    }
    PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e("TAG","idle");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e("TAG","offhook");

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("TAG","ringing: "+incomingNumber);
                    //输出来电号码
                    break;
            }
        }
    };
}

