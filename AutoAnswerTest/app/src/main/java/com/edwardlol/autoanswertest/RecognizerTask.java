package com.edwardlol.autoanswertest;

import android.os.AsyncTask;

import java.util.concurrent.BlockingQueue;

/**
 * Created by edwardlol on 15/3/4.
 */
public class RecognizerTask extends AsyncTask<Void, Object, Void> {
    private Controller controller;
    private BlockingQueue<DataBlock> blockingQueue;
    private Recognizer recognizer;

    public RecognizerTask(Controller controller, BlockingQueue<DataBlock> blockingQueue) {
        this.controller = controller;
        this.blockingQueue = blockingQueue;

        recognizer = new Recognizer();
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataBlock dataBlock;
        Spectrum spectrum;
        Character key;
        StatelessRecognizer statelessRecognizer;

        while (controller.isStarted()) {
            if (isCancelled()) {
                return null;
            } else {
                try {
                    dataBlock = blockingQueue.take();
                    spectrum = dataBlock.FFT();
                    spectrum.normalize();
                    statelessRecognizer = new StatelessRecognizer(spectrum);
                    key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                    publishProgress(spectrum, key);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
        return null;
    }

    protected void onProgressUpdate(Object... progress) {
        Character key = (Character) progress[1];
        controller.keyReady(key);
    }
}