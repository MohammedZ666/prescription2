package android.ztech.com.prescription;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SearchPrescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_prescription);
        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(SearchPrescription.this));



    }



//    prescription = regNoString + "$$" + names.getText().toString() + "$$" + patientcell.getText().toString()
//                +"$$"+diagnosises2.getText().toString()
//                + "$$" + years.getText().toString() + "$$" + months.getText().toString() + "$$" +
//            days.getText().toString() + "$$" + weight.getText().toString() + prescription;
//
//    sharedPreferences = getSharedPreferences(
//            regNoString + names.getText().toString() + patientcell.getText().toString()
//                + diagnosises2.getText().toString(), context.MODE_PRIVATE);
//
//    editor = sharedPreferences.edit();
//
//        editor.putString(regNoString + names.getText().toString() + patientcell.getText().toString()
//                + diagnosises2.getText().toString(),
//    prescription);
//
//        editor.commit();


    public void searchPres(View view)
    {


        final Context context = this;
        String diagnosisString="";
        String phoneString="";
        String nameString="";
        String FILENAME_PDFKEYS="keysToPresPDF";


        if(!((((EditText)findViewById(R.id.nameEditText)).getText().toString()).isEmpty()))
        {
           nameString=(((EditText)findViewById(R.id.nameEditText)).getText().toString()).replaceAll(" ","");
           nameString=nameString.replaceAll("\n","");
           nameString="(.*)"+nameString.toLowerCase() +"(.*)";
        }


        if(!((((EditText)findViewById(R.id.phoneEditText)).getText().toString()).isEmpty()))
        {
            phoneString=((EditText)findViewById(R.id.phoneEditText)).getText().toString();
            phoneString="(.*)"+phoneString+"(.*)";
        }

//        if(!((((EditText)findViewById(R.id.diaseaseEditText)).getText().toString()).isEmpty()))
//        {
//            diagnosisString=(((EditText)findViewById(R.id.diaseaseEditText)).getText().toString()).replaceAll(" ","");
//            diagnosisString=diagnosisString.replaceAll("\n","");
//            diagnosisString="(.*)"+diagnosisString.toLowerCase() +"(.*)";
//        }


        SharedPreferences sharedPreferences1=getSharedPreferences(FILENAME_PDFKEYS,MODE_PRIVATE);
        String[] fileNames = sharedPreferences1.getString(FILENAME_PDFKEYS,"").split("\n");
        LinearLayout linearLayout =(LinearLayout)findViewById(R.id.searchResultLayout);
        linearLayout.removeAllViews();



        for(int i=0;i<fileNames.length;i++)
        {
            if(!fileNames[i].isEmpty())
            {
                if((fileNames[i].toLowerCase()).matches(nameString)||fileNames[i].matches(phoneString))
                {

                    LinearLayout.LayoutParams tVParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    tVParams.setMargins(0,40,0,0);
                    final TextView textView = new TextView(this);
                    textView.setLayoutParams(tVParams);
                    textView.setTextSize(20);
                    textView.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    TextView txtView=(TextView) v;
                                    File imagePath = new File(getFilesDir(), "images");
                                    File newFile = new File(imagePath, "/doc"+txtView.getText().toString()+".pdf");
                                    Uri contentUri = FileProvider.getUriForFile(context,"android.ztech.com.prescription", newFile);


                                    if (contentUri != null) {

                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_VIEW);
                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                                        shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);




                                        try {
                                            startActivity(shareIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
                                        }



                                    }

                                }
                            }
                    );

                    textView.setText(fileNames[i]);
                    linearLayout.addView(textView);




                }


            }


        }


        if(linearLayout.getChildCount()==0)
        {
            Toast.makeText(context, "No match found", Toast.LENGTH_LONG).show();

        }




    }




}
