package android.ztech.com.prescription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zishan on 23/Nov/2016.
 */

public class TrialExpiredLockerDialog extends DialogFragment {

    TrialExpiredLockerDialog.OnPositiveClickListener mCallback;

    BroadcastReceiver smsKeyReciever;
     AlertDialog alertDialog;
    EditText mEditText;
    View view;



    public interface OnPositiveClickListener {
        void entryCheckTrial();

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TrialExpiredLockerDialog.OnPositiveClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        // Use the Builder class for convenient dialog construction




        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view =inflater.inflate(R.layout.dialog_signin,null);
        mEditText=(EditText)view.findViewById(R.id.password);

        builder.setMessage("Your app is deactivated. Please contact your developer")
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {

                            Activity openingAct= getActivity();
                            openingAct.finish();


                        }
                        return false;
                    }
                });



        alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        smsKeyReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Get the data (SMS data) bound to intent

               SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                SmsMessage sms = msgs[0];


                 byte[]data= sms.getUserData();

                String message="";

                for (int index=0; index < data.length; index++) {
                    message += Character.toString((char) data[index]);
                }


                if(message.equals(".obaida.zisan.19678.%.1_*.*##")) {


                    Toast.makeText(getActivity(), "Congrats! You just bought the premium version. Enjoy....", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    mCallback.entryCheckTrial();



                }
//            for(int i=0;i<msgs.length;i++){
//
//                if(msgs[i]!=null){
//
//
//
//
//                }

//            }


            }
        };




        // Create the AlertDialog object and return it
        return alertDialog;
    }


    @Override
    public void onStart() {
        super.onStart();



        IntentFilter intentFilter = new IntentFilter("android.intent.action.DATA_SMS_RECEIVED");
        intentFilter.setPriority(9999);
        intentFilter.addDataScheme("sms");
        intentFilter.addDataAuthority("*", "8126");


        Log.e("registerd reciever","smskeyreceiver");
        getActivity().registerReceiver(smsKeyReciever, intentFilter);


    }



    @Override
    public void onStop() {
        super.onStop();
        Log.e("unregisterd reciever","smskeyreceiver");
        getActivity().unregisterReceiver(smsKeyReciever);
    }




}












