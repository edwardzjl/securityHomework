package com.edwardlol.autoanswertest;

import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    private Button stateButton;
    private Button clearButton;
    private EditText recognizedEditText;
    private String recognizedText;

    Controller controller;
    History history;

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

        clearButton = (Button) this.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.clear();
            }
        });

        recognizedEditText = (EditText) this.findViewById(R.id.recognizedText);
        recognizedEditText.setFocusable(false);
        recognizedEditText.setEnabled(false);

        recognizedText = "";

        history = new History(this);
    }

    public void start() {
        stateButton.setText(R.string.stop);
        recognizedEditText.setEnabled(true);
    }

    public void stop() {
        history.add(recognizedText);
        stateButton.setText(R.string.start);
        recognizedEditText.setEnabled(false);
    }

    public int getAudioSource() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager.getCallState() != TelephonyManager.PHONE_TYPE_NONE) {
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;
        }
        return MediaRecorder.AudioSource.MIC;
    }

    public void clearText() {
        history.add(recognizedText);

        recognizedText = "";
        recognizedEditText.setText("");
    }

    public void addText(Character c) {
        recognizedText += c;
        recognizedEditText.setText(recognizedText);
    }

    @Override
    protected void onPause() {
        if (controller.isStarted())
            controller.changeState();
        super.onPause();
    }

}

