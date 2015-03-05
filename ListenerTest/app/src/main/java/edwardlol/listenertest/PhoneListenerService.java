package edwardlol.listenertest;

/**
 * Created by edwardlol on 15/3/4.
 */

import java.lang.reflect.Method;

//import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class PhoneListenerService extends Service {

    private MediaRecorder recorder;
    private boolean recording = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.e("TAG", "service onCreate()");
        super.onCreate();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void onDestroy() {
        Log.e("TAG", "service onDestroy()");
        super.onDestroy();
        //电话服务管理
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话状态
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    private PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            //打印电话状态改变信息
            Log.e("TAG", "state = " + state + "num = " + incomingNumber);
//            autoAnswercall();
            /*
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 没有来电 或者 挂断
                    Log.e("TAG", "onCallStateChanged state = " + state);
                    //stopRecord();
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃时
                    Log.e("TAG", "onCallStateChanged state = " + state);
                    //stop(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 接起电话
                    Log.e("TAG", "onCallStateChanged state = " + state);
                    //recordCalling();
                    break;
                default:
                    break;
            }*/
            super.onCallStateChanged(state, incomingNumber);
        }

    };

    //电话拦截
    /*
    public void stop(String s) {
        try {
            if (s.equals("110")) {
                Toast.makeText(this, "拦截成功", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "此来电为黑名单号码，已被拦截！");
                //调用ITelephony.endCall()结束通话
                Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                IBinder binder = (IBinder) method.invoke(null,new Object[] { TELEPHONY_SERVICE });
                ITelephony telephony = ITelephony.Stub.asInterface(binder);
                telephony.endCall();
            } else
                Toast.makeText(this, "不需拦截", Toast.LENGTH_SHORT).show();
            recording=false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/

/*
    private void autoAnswercall(){
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.answerRingingCall();

        } catch (NoSuchMethodException e) {
            Log.d("Sandy", "", e);
        } catch (ClassNotFoundException e) {
            Log.d("Sandy", "", e);
        }catch (Exception e) {
            Log.d("Sandy", "", e);
            try{
                Log.e("Sandy", "for version 4.1 or larger");
                Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");
            } catch (Exception e2) {
                Log.d("Sandy", "", e2);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                mediaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                sendOrderedBroadcast(mediaButtonIntent, null);
            }
        }
    }
    */
/*
    private void autoAnswercall2(){
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
        mediaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
        TApplication.nowApplication.sendOrderedBroadcast(mediaButtonIntent, null);
    }
*/


    //进行录音
    private void recordCalling() {
        try {
            Log.v("TAG", "recordCalling");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 读麦克风的声音
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 输出格式.3gp
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 编码方式
            recorder.setOutputFile(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/"
                    + System.currentTimeMillis()
                    + ".3gp");// 存放的位置是放在sdcard目录下
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
            recording=false;
        }
    }
}