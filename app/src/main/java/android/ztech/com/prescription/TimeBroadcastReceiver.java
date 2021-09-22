package android.ztech.com.prescription;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Zishan on 18/Dec/2016.
 */

public class TimeBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {



        Toast.makeText(context,"Entered",Toast.LENGTH_SHORT);

        String FILENAME_PREVIOUSMINUTE="com.ztech.android.cashmemo.FILENAME_PREVIOUSMINUTE";
        String FILENAME_STARTTIME = "com.ztech.android.cashmemo.STARTTIME";

        SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME_PREVIOUSMINUTE,context.MODE_PRIVATE);
        long prevMin = sharedPreferences.getLong(FILENAME_PREVIOUSMINUTE,System.currentTimeMillis()-60000);
        long nextMin=System.currentTimeMillis();
        long remainingMin;


        Log.e("!!!!","inside the broadcast reciever");


        if(prevMin>nextMin)
        {


            remainingMin=prevMin-nextMin;

            sharedPreferences = context.getSharedPreferences(FILENAME_STARTTIME, context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();



            editor.putLong(FILENAME_STARTTIME, sharedPreferences.getLong(FILENAME_STARTTIME, 0)-remainingMin);



            editor.commit();



//            Intent intentImplicit= new Intent(context,openingActivity.class);
//            intentImplicit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intentImplicit);
        }















        }

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

