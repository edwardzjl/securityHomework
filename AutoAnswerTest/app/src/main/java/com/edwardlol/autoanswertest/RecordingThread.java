package com.edwardlol.autoanswertest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by edwardlol on 15/3/6.
 */
public class RecordingThread extends Thread {
    private int frequency = 16000;
    private int blockSize = 1024;
    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
    private int bufferReadSize;
    private String TAG = "edwardlol.RecordingThreaad";

    public PhoneListenerService phoneListenerService;
    public BlockingQueue<DataBlock> DataStream;

    public RecordingThread(PhoneListenerService phoneListenerService, BlockingQueue<DataBlock> blockingQueue) {
        this.phoneListenerService = phoneListenerService;
        this.DataStream = blockingQueue;
        Log.d(TAG, "RecordingThread running");
    }

    public void run() {
        AudioRecord audioRecord = new AudioRecord(phoneListenerService.getAudioSource(), frequency, channelConfiguration, audioEncoding, bufferSize);
        DataBlock dataBlock;
        short[] buffer = new short[blockSize];

        try {
            audioRecord.startRecording();
            while (phoneListenerService.isStarted()) {
                bufferReadSize = audioRecord.read(buffer, 0, blockSize);
                dataBlock = new DataBlock(buffer, blockSize, bufferReadSize);
                DataStream.put(dataBlock);
            }
            audioRecord.stop();
            audioRecord.release();
        } catch (Throwable t) {
            Log.d(TAG, "Recording Failed", t);
            audioRecord.stop();
            audioRecord.release();
        }
    }

}
