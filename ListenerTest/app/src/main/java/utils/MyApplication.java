package utils;

/**
 * Created by edwardlol on 15/3/5.
 */
import android.app.Application;

/**
 * Created by edwardlol on 15/2/28.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
