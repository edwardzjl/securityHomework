package edwardlol.dtmftest;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by edwardlol on 15/3/4.
 */
public class Controller {
    private boolean started;

    private RecordTask recordTask;
    private RecognizerTask recognizerTask;
    private MainActivity mainActivity;
    BlockingQueue<DataBlock> blockingQueue;

    private Character lastValue;

    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void changeState() {
        if (!started) {
            lastValue = ' ';
            blockingQueue = new LinkedBlockingQueue<DataBlock>();
            mainActivity.start();
            recordTask = new RecordTask(this, blockingQueue);
            recognizerTask = new RecognizerTask(this, blockingQueue);
            //recordTask.execute();
            //recognizerTask.execute();
            recordTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            recognizerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            started = true;
            Log.e("ed","controller start!");
        } else {
            mainActivity.stop();
            recognizerTask.cancel(true);
            recordTask.cancel(true);
            started = false;
        }
    }

    public boolean isStarted() {
        return started;
    }

    public int getAudioSource() {
        return mainActivity.getAudioSource();
    }

    public void keyReady(char key) {
        if (key != ' ') {
            if (lastValue != key) {
                Log.e("edwardlol.Controller", "recognized: " + key);
            }
        }
        lastValue = key;
    }
}
