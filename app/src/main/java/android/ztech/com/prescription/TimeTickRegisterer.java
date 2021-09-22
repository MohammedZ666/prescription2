package android.ztech.com.prescription;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by Zishan on 19/Dec/2016.
 */

public class TimeTickRegisterer extends Service {


    @Override
    public void onCreate() {
        super.onCreate();


//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
//        registerReceiver(new TimeBroadcastReceiver(),intentFilter);

        Context context = getBaseContext();


        String FILENAME_PREVIOUSMINUTE="com.ztech.android.cashmemo.FILENAME_PREVIOUSMINUTE";



        SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME_PREVIOUSMINUTE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(FILENAME_PREVIOUSMINUTE,System.currentTimeMillis());
        editor.commit();



//        else if((remainingMin=prevMin-nextMin-60000)>0)
//        {
//
//
//            sharedPreferences = context.getSharedPreferences(FILENAME_STARTTIME, context.MODE_PRIVATE);
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putLong(FILENAME_STARTTIME,sharedPreferences.getLong(FILENAME_STARTTIME,0)-remainingMin);
//            editor.commit();
//
//        }





    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
