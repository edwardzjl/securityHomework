package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/2/27.
 */

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.os.Environment;
import utils.MyApplication;

public class PhoneListenerService extends Service {
    //private MediaRecorder recorder;
    //private boolean recording = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.v("TAG", "service onCreate()");
        super.onCreate();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    private PhoneStateListener listener = new PhoneStateListener() {
    /*
    * @see TelephonyManager#CALL_STATE_IDLE 值为0
    * @see TelephonyManager#CALL_STATE_RINGING 值为1
    * @see TelephonyManager#CALL_STATE_OFFHOOK 值为2
    */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //打印电话状态改变信息
            Log.v("TAG", "onCallStateChanged state=" + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 没有来电 或者 挂断
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃时
                    callFilter(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 接起电话
                    break;
                default:
                    break;
            }
        }
    };

    //电话拦截
    public void callFilter(String Number) {
        CustomContactsHandler handler = new CustomContactsHandler();
        Context context = MyApplication.getInstance();
        try {
            if (handler.inContacts(Number, context)) {
                Toast.makeText(this, "不需拦截", Toast.LENGTH_SHORT).show();
                //do nothing
            } else {
                Toast.makeText(this, "正在过滤", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "正在进行来电过滤！");

                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                ITelephony iTelephony;
                iTelephony = (ITelephony) m.invoke(tm);
                iTelephony.silenceRinger();//不响铃
                iTelephony.answerRingingCall();//接听
                //发送验证信息
                String message = "your captcha num: ";
                Random random = new Random();
                Integer captcha = random.nextInt(10000)+1;
                //Integer captcha = (int)(Math.random() * 10000);
                message += captcha.toString();
                sendSMS(Number,message);
                //接收对方键盘输入
                /*
                if (未通过验证) {
                    iTelephony.endCall();//结束通话
                } else {
                    //响铃

                    //接听

                }
                */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 直接调用短信接口发短信，不含发送报告和接受报告
    public void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
/*
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony iTelephony;
            iTelephony= (ITelephony)m.invoke(tm);
            iTelephony.silenceRinger();//不响铃
            //发送验证信息
            //接收对方键盘输入
            //iTelephony.answerRingingCall();//接听
            //iTelephony.endCall();//结束通话
            if (num.equals("110")) {
                Toast.makeText(this, "拦截成功", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "此来电为黑名单号码，已被拦截！");
                //调用ITelephony.endCall()结束通话
                Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                IBinder binder = (IBinder) method.invoke(null, new Object[] { TELEPHONY_SERVICE });
                ITelephony telephony = ITelephony.Stub.asInterface(binder);
                telephony.endCall();
            } else {
                Toast.makeText(this, "不需拦截", Toast.LENGTH_SHORT).show();
            }
            recording = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

}