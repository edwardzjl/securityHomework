package edwardlol.tonetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnAsterisk,btn0,btnHash;

    private ToneGenerator mToneGenerator;
    private Object mToneGeneratorLock = new Object(); // 监视器对象锁
    private boolean mDTMFToneEnabled; // 按键操作音
    private static final int TONE_LENGTH_MS = 150; // 延迟时间
    private AudioManager mAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btnAsterisk = (Button)findViewById(R.id.btnAsterisk);
        btn0 = (Button)findViewById(R.id.btn0);
        btnHash = (Button)findViewById(R.id.btnHash);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(3);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(4);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(5);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(6);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(7);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(8);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(9);
            }
        });
        btnAsterisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(10);
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(0);
            }
        });
        btnHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(11);
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mDTMFToneEnabled = Settings.System.getInt(getContentResolver(), Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                } catch (Exception e) {
                    e.printStackTrace();
                    mToneGenerator = null;
                }
            }
        }

    }

    // 播放按键声音
    void playTone(int tone) {
        if (!mDTMFToneEnabled) {
            return;
        }

        int ringerMode = mAudioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT) || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            // 静音或者震动时不发出按键声音
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.startTone(tone, TONE_LENGTH_MS); // 发声
        }
    }


}
