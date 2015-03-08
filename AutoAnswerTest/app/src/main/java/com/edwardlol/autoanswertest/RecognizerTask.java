package com.edwardlol.autoanswertest;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by edwardlol on 15/3/4.
 */
public class RecognizerTask extends AsyncTask<Void, Object, Void> {
    private BlockingQueue<DataBlock> blockingQueue;
    private Recognizer recognizer;
    private String TAG = "edwardlol.RecognizerTask";

    PhoneListenerService phoneListenerService;

    public RecognizerTask(PhoneListenerService phoneListenerService, BlockingQueue<DataBlock> blockingQueue) {
        this.phoneListenerService = phoneListenerService;
        this.blockingQueue = blockingQueue;
        recognizer = new Recognizer();
        Log.d(TAG, "RecognizerTask running");
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataBlock dataBlock;
        Spectrum spectrum;
        Character key;
        StatelessRecognizer statelessRecognizer;

/*
        while (phoneListenerService.isStarted()) {
            if (isCancelled()) {
                break;
                //return null;
            } else {
                try {
                    dataBlock = blockingQueue.take(); //取出时域信号
                    spectrum = dataBlock.FFT(); //进行FFT变换，转变成频域信号
                    spectrum.normalize();
                    statelessRecognizer = new StatelessRecognizer(spectrum);
                    key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                    //publishProgress(spectrum, key);
                    publishProgress(key);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.d(TAG, "sth went wrong", e);
                }
            }
        }
*/

        while (!isCancelled()) {
            try {
                dataBlock = blockingQueue.take(); //取出时域信号
                spectrum = dataBlock.FFT(); //进行FFT变换，转变成频域信号
                spectrum.normalize();
                statelessRecognizer = new StatelessRecognizer(spectrum);
                key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                //publishProgress(spectrum, key);
                publishProgress(key);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Log.d(TAG, "sth went wrong", e);
            }

        }



        return null;
    }

    protected void onProgressUpdate(Object... progress) {

            //Character key = (Character) progress[1];
            Character key = (Character) progress[0];
            phoneListenerService.keyReady(key);

    }
}