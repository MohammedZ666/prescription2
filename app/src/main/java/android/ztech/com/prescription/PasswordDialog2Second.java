package android.ztech.com.prescription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zishan on 8/18/2016.
 */
public class PasswordDialog2Second extends DialogFragment {


    OnPositiveClickListener mCallback;

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
            mCallback = (OnPositiveClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {




        final String passwordOriginal=".abusafwan.";


        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view =inflater.inflate(R.layout.dialog_signin,null);
        mEditText=(EditText)view.findViewById(R.id.password);

        builder.setMessage("Password")
                .setView(view)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {

                            Activity openingAct= getActivity();
                            openingAct.finish();


                        }
                        return false;
                    }
                })


                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                    }



                });



        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Write your Logic. It will never dismiss the dialog unless your condition satisifies


                        if (passwordOriginal.equals(mEditText.getText().toString())) {
                            mCallback.entryCheckTrial();
                            alertDialog.dismiss();

                        }
                        else {

//                            mCallback.entryCheckMain(false);

                            Toast.makeText(getActivity(), "WRONG PASSWORD!!", Toast.LENGTH_LONG).show();

                        }

                    }
                });
                alertDialog.show();

            }


        });

        // Create the AlertDialog object and return it
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}






