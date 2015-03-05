package com.edwardlol.autoanswertest;

import android.app.Service;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import utils.MyApplication;

/**
 * Created by edwardlol on 15/3/4.
 */
public class Controller {
    private boolean started;
    private RecordTask recordTask;
    private RecognizerTask recognizerTask;
    private int audioSource;
    BlockingQueue<DataBlock> blockingQueue;

    private Character lastValue;

    public Controller(int AudioSource) {
        this.audioSource = AudioSource;
    }

    public void start() {
        lastValue = ' ';
        blockingQueue = new LinkedBlockingQueue<DataBlock>();
        recordTask = new RecordTask(this, blockingQueue);
        recognizerTask = new RecognizerTask(this, blockingQueue);
        recordTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        recognizerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        started = true;
    }

    public void stop() {
        recognizerTask.cancel(true);
        recordTask.cancel(true);
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public int getAudioSource() {
        return audioSource;
    }

    public void keyReady(char key) {
        if (key != ' ') {
            if (lastValue != key) {
                Log.e("edward", "recognized: " + key);
            }
        }
        lastValue = key;
    }
}
