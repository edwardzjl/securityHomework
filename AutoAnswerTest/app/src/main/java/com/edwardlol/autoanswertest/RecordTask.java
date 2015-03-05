package com.edwardlol.autoanswertest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by edwardlol on 15/3/4.
 */
public class RecordTask extends AsyncTask<Void, Object, Void> {
    int frequency = 16000;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    int blockSize = 1024;

    Controller controller;
    BlockingQueue<DataBlock> blockingQueue;

    public RecordTask(Controller controller, BlockingQueue<DataBlock> blockingQueue) {
        this.controller = controller;
        this.blockingQueue = blockingQueue;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        int bufferReadSize;
        AudioRecord audioRecord = new AudioRecord(controller.getAudioSource(), frequency, channelConfiguration, audioEncoding, bufferSize);

        try {
            short[] buffer = new short[blockSize];
            audioRecord.startRecording();

            while (controller.isStarted()) {
                if (isCancelled()) {
                    audioRecord.stop();
                    audioRecord.release();
                    return null;
                } else {
                    bufferReadSize = audioRecord.read(buffer, 0, blockSize);
                    DataBlock dataBlock = new DataBlock(buffer, blockSize, bufferReadSize);
                    blockingQueue.put(dataBlock);
                }
            }
            audioRecord.stop();
            audioRecord.release();
        } catch (Throwable t) {
            Log.e("edwardlol", "Recording Failed");
            audioRecord.stop();
            audioRecord.release();
        }

        return null;
    }
}