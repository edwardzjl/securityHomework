package wpam.recognizer;

import java.util.concurrent.BlockingQueue;
import android.os.AsyncTask;
import android.util.Log;

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
        StatelessRecognizer statelessRecognizer;
        Character key;

        while (controller.isStarted()) {
            try {
                dataBlock = blockingQueue.take();
                spectrum = dataBlock.FFT();
                spectrum.normalize();
                statelessRecognizer = new StatelessRecognizer(spectrum);
                key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                publishProgress(spectrum, key);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Log.e("edwardlol", Log.getStackTraceString(e));
            }
        }

        return null;
    }

    protected void onProgressUpdate(Object... progress) {
        Character key = (Character) progress[1];
        controller.keyReady(key);
    }
}
