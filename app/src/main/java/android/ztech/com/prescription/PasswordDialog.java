package android.ztech.com.prescription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Zishan on 8/18/2016.
 */
public class PasswordDialog extends DialogFragment {


    OnPositiveClickListener mCallback;

    EditText mEditText;
    View view;



    public interface OnPositiveClickListener {
      void waiting();

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





            // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view =inflater.inflate(R.layout.waiting_hack_layout,null);


        builder.setView(view);

            // Create the AlertDialog object and return it
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mCallback.waiting();
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);

            return alertDialog;
        }
    }






