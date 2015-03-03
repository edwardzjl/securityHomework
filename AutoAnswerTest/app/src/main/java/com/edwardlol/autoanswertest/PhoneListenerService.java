package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/2/27.
 */

import java.lang.reflect.Method;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import utils.MyApplication;

public class PhoneListenerService extends Service {
    private MediaRecorder recorder;
    private boolean recording = false;
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
                    stopRecord();
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃时
                    callFilter(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 接起电话
                    recordCalling();
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


    //进行录音
    private void recordCalling() {
        try {
            Log.v("TAG", "recordCalling");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //读麦克风的声音
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 输出格式.3gp
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 编码方式
            recorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".3gp"); //存放的位置是放在sdcard目录下
            recorder.prepare();
            recorder.start();
            recording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //停止录音
    private void stopRecord() {
        Log.v("TAG", "stopRecord");
        if (recording) {
            recorder.stop();
            recorder.release();
            recording = false;
        }
    }


}