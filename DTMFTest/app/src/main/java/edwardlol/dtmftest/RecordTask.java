package edwardlol.dtmftest;

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
    int blockSize = 1024;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    Controller controller;
    BlockingQueue<DataBlock> blockingQueue;

    public RecordTask(Controller controller, BlockingQueue<DataBlock> blockingQueue) {
        this.controller = controller;
        this.blockingQueue = blockingQueue;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) + 100;
        AudioRecord audioRecord = new AudioRecord(controller.getAudioSource(), frequency, channelConfiguration, audioEncoding, bufferSize);

        try {
            short[] buffer = new short[blockSize];
            audioRecord.startRecording();

            while (controller.isStarted()) {
                int bufferReadSize = audioRecord.read(buffer, 0, blockSize);
                DataBlock dataBlock = new DataBlock(buffer, blockSize, bufferReadSize);
                blockingQueue.put(dataBlock);
            }
            //audioRecord.stop();
            Log.e("edwardlol.RecordTask", "am i doing things?");
            audioRecord.release();

        } catch (Throwable t) {
            audioRecord.release();
            Log.e("edwardlol.RecordTask",Log.getStackTraceString(t));
        }

        return null;
    }
}