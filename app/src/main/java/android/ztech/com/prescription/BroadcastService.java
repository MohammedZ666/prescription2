package android.ztech.com.prescription;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class BroadcastService extends Service {
    Thread mThread;
    boolean shouldthreadRun;
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "android.ztech.com.prescription.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    @Override
    public void onCreate() {
        super.onCreate();


        String FILENAME_STARTTIME = "android.ztech.com.prescription.STARTTIME";
        long time =30*24*60*60;
        //long time = 5 * 60;
        time*=1000;
//        Log.i(TAG, "the time-"+Long.toString(time));

        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_STARTTIME,this.MODE_PRIVATE);
        long startTime=sharedPreferences.getLong(FILENAME_STARTTIME,0);
        final long endTime = startTime+time;


        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                shouldthreadRun=true;


                while (shouldthreadRun) {

                    long milisRealUntilFininshed = endTime - System.currentTimeMillis();

//                Log.e(TAG, "infinitely looping "+Long.toString(milisRealUntilFininshed));

//                Log.e(TAG, "Countdown seconds remaining: " + milisRealUntilFininshed / 1000);

                    bi.putExtra("countdown", milisRealUntilFininshed);
                    sendBroadcast(bi);

                    if (milisRealUntilFininshed < 1) {

                        break;
                    }

                    SystemClock.sleep(1000);
                }

                if(!shouldthreadRun)bi.putExtra("countdown",endTime - System.currentTimeMillis());

            }
        });
        mThread.start();

    }

    @Override
    public void onDestroy() {

        shouldthreadRun=false;

        try {
            mThread.join();
            Log.e(TAG,"Worker(mThread) thread terminated properly");

        }

        catch (InterruptedException ie){

            ie.printStackTrace();
            Log.e(TAG,"Worker(mThread) thread could not be joined");

        }

        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}