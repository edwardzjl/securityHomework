package wpam.recognizer;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
            blockingQueue = new LinkedBlockingQueue<DataBlock>(10240000);
            mainActivity.start();
            recordTask = new RecordTask(this, blockingQueue);
            recognizerTask = new RecognizerTask(this, blockingQueue);
            recordTask.execute();
            recognizerTask.execute();
            started = true;
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
                Log.e("edwardlol.Controller","recognized: " + key);
            }
        }
        lastValue = key;
    }

}
