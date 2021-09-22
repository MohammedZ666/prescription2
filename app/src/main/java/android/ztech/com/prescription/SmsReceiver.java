package android.ztech.com.prescription;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Zishan on 29/Dec/2016.
 */

public class SmsReceiver extends BroadcastReceiver {

    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent


//        Intent intentImplicit = new Intent();
//        intent.setClass(context,com.ztech.android.diagincome.MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intentImplicit);
//


        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);


        String message = "";


        for (int i = 0; i < msgs.length; i++) {

            SmsMessage sms = msgs[i];
            message += sms.getMessageBody();


        }

        if (msgs[0].getOriginatingAddress().equals("dummy")) {

            if (message.contains("01837322980") &&
                    message.contains("successful") &&
                    message.contains("accredited") &&
                    !message.contains("not successful") &&
                    !message.contains("unsuccessful") &&
                    !message.contains("failed")) {

                List<String> words = Arrays.asList(message.split(" "));
                double amount;
             amount = checkAmount(words);

            }


        }

    }






    private double checkAmount(List <String> words){
        double amount = 0.0f;
        String takaSign = "Tk";
        if (words.contains(takaSign)) {
            int i = words.indexOf(takaSign) + 1;
            try {
                amount = Double.parseDouble(words.get(i + 1));
            } catch (Exception e0) {
                i = words.indexOf(takaSign) - 1;
                try {
                    amount = Double.parseDouble(words.get(i + 1));
                } catch (Exception e1) {

                }
            }

        }

            else if (words.contains( takaSign = "Tk.")) {
            int i = words.indexOf(takaSign) + 1;
            try {
                amount = Double.parseDouble(words.get(i + 1));
            } catch (Exception e0) {
                i = words.indexOf(takaSign) - 1;
                try {
                    amount = Double.parseDouble(words.get(i + 1));
                } catch (Exception e1) {

                }
            }

        }


            else if (words.contains( takaSign = "৳")) {
            int i = words.indexOf(takaSign) + 1;
            try {
                amount = Double.parseDouble(words.get(i + 1));
            } catch (Exception e0) {
                i = words.indexOf(takaSign) - 1;
                try {
                    amount = Double.parseDouble(words.get(i + 1));
                } catch (Exception e1) {

                }
            }

        }



            else if (words.contains( takaSign = "BDT")) {
            int i = words.indexOf(takaSign) + 1;
            try {
                amount = Double.parseDouble(words.get(i + 1));
            } catch (Exception e0) {
                i = words.indexOf(takaSign) - 1;
                try {
                    amount = Double.parseDouble(words.get(i + 1));
                } catch (Exception e1) {

                }
            }
        }


        else   {

            int i = words.indexOf(takaSign) + 1;
            try {
                amount = Double.parseDouble(words.get(i + 1));
            } catch (Exception e0) {

            }

        }
    return amount;
    }

}



