package com.edwardlol.autoanswertest;

/**
 * Created by edwardlol on 15/2/27.
 */

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import com.android.internal.telephony.ITelephony;

public class PhoneListenerService extends Service {
    private String TAG = "edwardlol.PhoneListenerService";
    private static int CAPTCHA_LENGTH = 4;
    private boolean filterStarted;
    private Character lastValue;
    private RecognizerTask recognizerTask;
    private RecordingThread recordingThread;
    private int matching_Index;

    private String CAPTCHA;
    public BlockingQueue<DataBlock> DataStream;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.d(TAG, "service onCreate()");
        super.onCreate();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void onDestroy() {
        Log.d(TAG, "service onDestroy()");
        super.onDestroy();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    private PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d(TAG, "phone state: " + state + ", incoming number: " + incomingNumber);
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
        CAPTCHA = "";
        matching_Index = 0;
        CustomContactsHandler handler = new CustomContactsHandler();

        try {
            if (handler.inContacts(Number, getApplicationContext())) {
                Log.d(TAG, "in contacts");
                //do nothing
            } else {
                Log.d(TAG, "not in contacts, filtering...");
                //发送验证信息
                String message = "your captcha num: ";
                CAPTCHA = getCAPTCHA(CAPTCHA_LENGTH);
                Log.d(TAG, "CAPTHA number: " + CAPTCHA);
                message += CAPTCHA;
                sendSMS(Number, message);
                //自动接听电话
                answerRingingCall(getApplicationContext());

                startFiltering();
            }
        } catch (Exception e) {
            Log.d(TAG, "callFilter went wrong: ", e);
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

    public void startFiltering() {
        lastValue = ' ';
        DataStream = new LinkedBlockingQueue<>(4);

        recordingThread = new RecordingThread(this, DataStream);
        recordingThread.start();

        recognizerTask = new RecognizerTask(this, DataStream);
        recognizerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        filterStarted = true;
    }

    public void stopFiltering() {
        recognizerTask.cancel(true);
        filterStarted = false;
    }

    public boolean isStarted() {
        return filterStarted;
    }

    public void keyReady(char key) {

        if (key != ' ') {
            if (lastValue != key) {
                Log.d(TAG, "recognized: " + key);
                if (CAPTCHA != null && CAPTCHA.length() > 0) {
                    if (key == '#') {
                        if (matching_Index == CAPTCHA_LENGTH) {
                            //ring again to notify the user
                            Log.d(TAG,"call verified!");
                        } else {
                            endCall();
                        }
                        stopFiltering();
                    } else {
                        if (key == CAPTCHA.charAt(matching_Index)) {
                            matching_Index++;
                        } else {
                            matching_Index = 0;
                        }
                    }
                }
            }
        }
        lastValue = key;

    }

    private String getCAPTCHA(int n) {
        String CAPTCHA = "";
        Random random;
        Integer num;

        for (int i = 0; i < n; i++) {
            random = new Random();
            num = Math.abs(random.nextInt() % 10);
            CAPTCHA += num.toString();
        }
        return CAPTCHA;
    }

    //适用各种版本的自动接听，5.0模拟器试了没用
    public synchronized void answerRingingCall(Context context) {
        try {
            Log.d(TAG, "try to answer below 2.3");
            //ITelephony itelephony = getITelephony(mTelephonyManager);
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
            ITelephony itelephony = ITelephony.Stub.asInterface(binder);
            itelephony.silenceRinger();
            itelephony.answerRingingCall();
        } catch (Exception e1) {
            Log.d(TAG, "try to answer 2.3 ~ 4.1", e1);
            try {
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");

                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra("android.intent.extra.KEY_EVENT",    localKeyEvent1);
                context.sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");

                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra("android.intent.extra.KEY_EVENT",    localKeyEvent2);
                context.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");

                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
            } catch (Exception e2) {
                try{
                    Log.d(TAG, "try to answer for 4.1 and above way 1", e2);
                    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                    intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                    context.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");
//                    sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");
                } catch (Exception e3) {
                    Log.d(TAG, "try to answer for 4.1 and above way2", e3);
                    Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                    mediaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                    context.sendOrderedBroadcast(mediaButtonIntent, null);
//                    sendOrderedBroadcast(mediaButtonIntent, null);
                }
            }
        }
    }

    public synchronized void endCall() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        Method mthEndCall;
        try {
            mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            mthEndCall.setAccessible(true);
            ITelephony iTelephony = (ITelephony) mthEndCall.invoke(tm, (Object[]) null);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "endCall test");
    }

    public int getAudioSource() {
        /* wtf
        TelephonyManager telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager.getCallState() != TelephonyManager.PHONE_TYPE_NONE) {
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;
        }
        */
        return MediaRecorder.AudioSource.MIC;
    }

}