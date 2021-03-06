package edwardlol.dtmftest;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button stateButton;
    Controller controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        stateButton = (Button) this.findViewById(R.id.stateButton);
        stateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.changeState();
            }
        });
    }

    public void start() {
        stateButton.setText(R.string.stop);
    }

    public void stop() {
        stateButton.setText(R.string.start);
    }

    public int getAudioSource() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager.getCallState() != TelephonyManager.PHONE_TYPE_NONE) {
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;
        }
        return MediaRecorder.AudioSource.MIC;
    }

    @Override
    protected void onPause() {
        if (controller.isStarted())
            controller.changeState();
        super.onPause();
    }
}
