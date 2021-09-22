package android.ztech.com.prescription;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity  implements PasswordDialog.OnPositiveClickListener{




    boolean printActivty = false;
    Activity firstActivity;
    PrintedPdfDocument mPdfDocument;
    EditText patientcell, days, months, weight, years;
    View focusedTextField;
    boolean isInTouchMode;


    String[] replacements = {

            "", " ১ চামচ দিনে একবার ১ মাস ", " ১ চামচ ১২ ঘন্টা অন্তর ৭ দিন ", " ১ চামচ ৮ ঘন্টা অন্তর ৭ দিন ",
            " ১ চামচ ৬ ঘন্টা অন্তর ৭ দিন ", " আধা চামচ দিনে একবার ১ মাস ", " আধা চামচ ১২ ঘন্টা অন্তর ৭ দিন ", " আধা চামচ  ৮ ঘন্টা অন্তর ৭ দিন ",
            " আধা চামচ ৬ ঘন্টা অন্তর ৭ দিন ", " একটা দিনে একবার ১ মাস ", " একটা ১২ ঘন্টা অন্তর ৭ দিন ", " একটা ৮ ঘন্টা অন্তর ৭ দিন ",
            " একটা ৬ ঘন্টা ৭ দিন ", " নেবুলাইজার Solution ১ এম্পল SALPIUM/SULPREX + ২ মিলি. Norsol  দ্বারা(Salbutamol 2.5 mg + Ipra tropium bromide 0.5 mg) ৪  " +
            "ঘন্টা অন্তর দিবেন ২ দিন । পরে শ্বাম কষ্ট কমে গেলে ৬ ঘন্টা অন্তর ৫/৭ দিন । ", " নেবুলাইজার Solution আধা এম্পল SALPIUM/SULPREX + ২ মিলি. Norsol দ্বারা(Salbutamol 2.5 mg + Ipra tropium bromide 0.5 mg) ৪  " +
            "ঘন্টা অন্তর দিবেন ২ দিন । পরে শ্বাম কষ্ট কমে গেলে ৬ ঘন্টা অন্তর ৫/৭ দিন । ", " ONE IM রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন) ", " ONE IV রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন) ",
            " HALF IM রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন )", " HALF IV  রোজ একবার ৭ দিন ", " ONE IV ১২ ঘন্টা অন্তর ৭ দিন ", " ১ চামচ ৪ ঘন্টা অন্তর জ্বর উঠলে খাবেন  এবং শরীর ভেজা গামছা দ্বারা মুছে দিবেন ",
            " একটা ৪ ঘন্টা অন্তর জ্বর উঠলে মলদ্বারে দিবেন এবং শরীর ভেজা গামছা দ্বারা মুছে দিবেন ", " 150 ml per 24 hours at a rate of 6 Microdrop per minute ",
            " রোজ ৬ ঘন্টা অন্তর আক্যান্ত স্থানে লাগাবেন ", " হাসপাতালে র্িত করবে ", " NPO till patient can take orally ", " Oxygen Inhalation stat and sos "
            , " অবস্থার উন্নতি না হলে উন্নত চিকিৎসার জন্য  REFFERED TO SMCH "

    };

    boolean doSympExist = false, doTestExist = false,isRegSavedOnce=false;
    int userSaveDrugLastIndex;
    String FILENAME_ADVICE = "android.ztech.com.prescription.advice_container_new_0";
    String FILENAME_NAME = "android.ztech.com.prescription.name_container_new";
    String FILENAME_SYMPTOMS = "android.ztech.com.prescription.symptoms_container_023";
    String FILENAME_INVESTIGATION = "android.ztech.com.prescription.investigations_container_023";
    String FILENAME_MEDICATIONS = "android.ztech.com.prescription.medications_container_1";
    String FILENAME_DIAGNOSIS = "android.ztech.com.prescription.diagnosis_container_1";
    String FILENAME_DIAGNOSIS_2 = "android.ztech.com.prescription.diagnosis_container_2";
    String FILENAME_EMERGENGYADVICE = "emergencyAdvice";
    List<Integer> symptomIdArray = new ArrayList<>();
    List<Integer> investigationIdArray = new ArrayList<>();
    List<Integer> medicationIdArray = new ArrayList<>();
    List<Integer> commonIdArray = new ArrayList<Integer>();

    ArrayAdapter<String> adapter, diag2Adapter, testAdapter, symptomsAdapter, adviceAdapter, nameAdapter, diagnosisAdapter,  emergenyAdviceAdapter;


    List<String> loadedAdvice = new ArrayList<String>();
    List<String> loadedName = new ArrayList<String>();
    List<String> loadedDiagnosis = new ArrayList<String>();

    List<String> loadedDiagnosis2 = new ArrayList<String>();
    List<String> loadedSymptoms = new ArrayList<String>();
    List<String> loadedMedication;
    List<String> loadedInvestigation = new ArrayList<String>();
    List<String> loadedEmergencyAdvice = new ArrayList<String>();
    List<String> linedAdvice = new ArrayList<String>();
    ArrayList<LinearLayout> tests = new ArrayList<>();
    LinearLayout deleter, test_deleter, symptom_deleter;

    AutoCompleteTextView advices, diagnosises,diagnosises2, names;
    TextView dateTextView;
    LinearLayout printPagePart;
    LinearLayout.LayoutParams  drugParamsMax, drugParamsMedium, drugParamsMin;

    utilityPrescription idGenerator;
    Context context;
    SpinnerAdapter shortcutsAdapter;

    private class PostTask2 extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String toBeEdited = params[0];



            if(loadedDiagnosis.size()==0)
            {
                try {


                    FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS, context.MODE_PRIVATE);

                    fos.write("".getBytes());
                    fos.close();


                }
                catch (Exception e) {

                }
            }

            else {
                for (int i = 0; i < loadedDiagnosis.size(); i++) {

                    if(i==0) {
                        try {


                            FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS, context.MODE_PRIVATE);

                            fos.write((loadedDiagnosis.get(i) + "\n").getBytes());
                            fos.close();


                        } catch (Exception e) {

                        }

                    }

                    else
                    {
                        try {


                            FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS, context.MODE_APPEND);

                            fos.write((loadedDiagnosis.get(i) + "\n").getBytes());
                            fos.close();


                        } catch (Exception e) {

                        }

                    }

                }

            }



            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarVisibility(false);


        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.*/
    private class deleteDiag extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {



            try {
                FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS_2, context.MODE_PRIVATE);

                for (int i = 0; i <loadedDiagnosis2.size(); i++) {

                    fos.write((loadedDiagnosis2.get(i) + "\n").getBytes());
                }

                fos.close();


            } catch (Exception e) {
            }






            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarVisibility(false);
        }
    }

    private class deleteAdviceSugg extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {



            try {
                FileOutputStream fos = openFileOutput(FILENAME_ADVICE, context.MODE_PRIVATE);

                for (int i = 0; i <loadedAdvice.size(); i++) {

                    fos.write((loadedAdvice.get(i) + "\n").getBytes());
                }

                fos.close();


            } catch (Exception e) {
            }






            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarVisibility(false);
        }
    }


    // The definition of our task class
    private class PostTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarVisibility(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String toBeEdited = params[0];


            //loadedMedication.removeAll(Arrays.asList(getResources().getStringArray(R.array.med)));

            try {
                FileOutputStream fos = openFileOutput(FILENAME_MEDICATIONS, context.MODE_PRIVATE);

                for (int i = 0; i <userSaveDrugLastIndex; i++) {

                    fos.write((loadedMedication.get(i) + "\n").getBytes());
                }

                fos.close();


            } catch (Exception e) {
            }
            //loadedMedication.addAll(Arrays.asList(getResources().getStringArray(R.array.med)));






            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarVisibility(false);
        }
    }




    private void progressBarVisibility(boolean isBackgroundTaskRunning)
    {
        if(isBackgroundTaskRunning)
        {

            Intent intentImplicit = new Intent(this, waitingActivity.class);
            startActivityForResult(intentImplicit,1010);


        }

        else {
            finishActivity(1010);
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        commonIdArray.add(R.id.name);
        commonIdArray.add(R.id.patientCell);
        commonIdArray.add (R.id.years);
        commonIdArray.add(R.id.months);
        commonIdArray.add(R.id.days);
        commonIdArray.add(R.id.Diagnosis);
        commonIdArray.add(R.id.weight);
        commonIdArray.add(R.id.thePrintedOne);
        commonIdArray.add(R.id.advices);
        //        commonIdArray.add(R.id.emergency);

        List drugs = Arrays.asList(getResources().getStringArray(R.array.med));
        patientcell = (EditText) findViewById(R.id.patientCell);
        days = (EditText) findViewById(R.id.days);
        months = (EditText) findViewById(R.id.months);
        years = (EditText) findViewById(R.id.years);
        weight = (EditText) findViewById(R.id.weight);
        //        emergency = (AutoCompleteTextView) findViewById(R.id.emergency);




        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MainActivity.this));


        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(currentDateTimeString);


        advices = (AutoCompleteTextView) findViewById(R.id.advices);
        diagnosises = (AutoCompleteTextView) findViewById(R.id.Diagnosis);
        diagnosises2 = (AutoCompleteTextView) findViewById(R.id.thePrintedOne);
        names = (AutoCompleteTextView) findViewById(R.id.name);

        printPagePart = (LinearLayout) findViewById(R.id.printPagePart);


        try {

            FileInputStream fis = this.openFileInput(FILENAME_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedName.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            FileInputStream fis = this.openFileInput(FILENAME_ADVICE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedAdvice.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            FileInputStream fis = this.openFileInput(FILENAME_DIAGNOSIS);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedDiagnosis.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            FileInputStream fis = this.openFileInput(  FILENAME_DIAGNOSIS_2);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedDiagnosis2.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // adding the user-defined drug-strings here FIRST....

        loadedMedication = new ArrayList<String>();
        userSaveDrugLastIndex = 0;
        try {

            FileInputStream fis = this.openFileInput(FILENAME_MEDICATIONS);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {


                loadedMedication.add(loadedStringTemp);
                userSaveDrugLastIndex++;

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // adding the app-defined drug-strings here......
        loadedMedication.addAll(userSaveDrugLastIndex, drugs);

        try {

            FileInputStream fis = this.openFileInput(FILENAME_INVESTIGATION);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedInvestigation.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            FileInputStream fis = this.openFileInput(FILENAME_SYMPTOMS);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedSymptoms.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        try {

            FileInputStream fis = this.openFileInput(FILENAME_EMERGENGYADVICE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);


            String loadedStringTemp;


            while ((loadedStringTemp = br.readLine()) != null) {

                loadedEmergencyAdvice.add(loadedStringTemp);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        emergenyAdviceAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedEmergencyAdvice);
        testAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item,
                R.id.autoCompleteItem, loadedInvestigation);
        adapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
        symptomsAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedSymptoms);
        adviceAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, loadedAdvice);
        nameAdapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedName);
        diagnosisAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedDiagnosis);
        advices.setAdapter(adviceAdapter);
        advices.setThreshold(1);
        names.setAdapter(nameAdapter);

        diagnosises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription." + diagnosises.getText().toString(), context.MODE_PRIVATE);
                String[] temp;
                try {
                    temp = sharedPreferences
                            .getString("android.ztech.com.prescription." + diagnosises.getText().toString(), "").split("\\$\\$");
                } catch (Exception e) {
                    Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();

                    return;
                }

                for (int i = 0; i < temp.length; i++) {
                    if (i == 0 && !temp[0].isEmpty()) add_symptoms(temp[0].split("\\$"));
                    else if (i == 1 && !temp[1].isEmpty()) add_test(temp[1].split("\\$"));
                    else if (i == 2 && !temp[2].isEmpty()) {
                        create_new_drug(temp[2].split("\\$"), null);
                    } else if (i == 3 && !temp[3].isEmpty() &&!temp[3].contains("$")) advices.setText(temp[3]);

                    //                    else if (i == 4 && !temp[4].isEmpty()) emergency.setText(temp[4]);
                }



                Toast.makeText(context, "Loaded "+"'"+diagnosises.getText().toString()+"'", Toast.LENGTH_LONG).show();
//                    diagnosises.setText("");

            }
        });

        firstActivity = (Activity) MainActivity.this;
        diagnosises.setAdapter(diagnosisAdapter);

        diag2Adapter = new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2,  loadedDiagnosis2);
        diagnosises2.setAdapter(diag2Adapter);

        idGenerator = new utilityPrescription();

        deleter = (LinearLayout) findViewById(R.id.AddMedLayout);
        test_deleter = (LinearLayout) findViewById(R.id.tests);



        //        SpinnerHowManyTimesADay= getResources().getStringArray(R.array.banglaSuggestions);
        shortcutsAdapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem,  replacements);
        //        frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SpinnerHowManyTimesADay);

        context = this;
        symptom_deleter = (LinearLayout) findViewById(R.id.symptoms);


        drugParamsMax = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20f);
        drugParamsMedium = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5f);
        drugParamsMin = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        drugParamsMin.setMargins(0, 40, 0, 0);
        drugParamsMax.setMargins(0, 30, 0, 0);
        drugParamsMedium.setMargins(0, 60, 0, 0);





        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.REG", context.MODE_PRIVATE);
        int regNo = sharedPreferences.getInt("android.ztech.com.prescription.REG", 1);

        ((TextView) findViewById(R.id.regNo)).setText(Integer.toString(regNo));


        Activity mainActivity = MainActivity.this;


        // Here, thisActivity is the current activity



        ViewTreeObserver vto = findViewById(R.id.ZscrollView).getViewTreeObserver();

        vto.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            public void onGlobalFocusChanged(
                    View oldFocus, View newFocus) {


                focusedTextField = newFocus;



            }
        });

        vto.addOnTouchModeChangeListener(
                new ViewTreeObserver.OnTouchModeChangeListener() {
                    public void onTouchModeChanged(
                            boolean isInTouchModeLocal) {

                        isInTouchMode = isInTouchModeLocal;
                    }
                });


        PrintAttributes newAttributes =new PrintAttributes.Builder()
                .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).setMinMargins(new PrintAttributes.Margins(0,0,0,0))
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4).build();
        mPdfDocument = new PrintedPdfDocument(context, newAttributes);



       // create_new_drug(new View(this));

    }



    private void stringProcessor(String string) {
        linedAdvice.clear();
        final int charLim = 91;

        if (string.length() > charLim && (string.contains(" ") || string.contains("\n"))) {


            int i = 0, spaceCharIndex;
            while (true) {

                if (string.length() - 1 - i >= charLim) {
                    if (string.substring(i + charLim - 1, i + charLim).equals(" ") || string.substring(i + charLim - 1, i + charLim).equals("\n")) {
                        linedAdvice.add(string.substring(i, i + charLim));
                        i += charLim;
                        Log.e("the i space", Integer.toString(i));

                    } else {
                        spaceCharIndex = i + charLim - 1;

                        while (!string.substring(spaceCharIndex, spaceCharIndex + 1).equals(" ") && !string.substring(spaceCharIndex, spaceCharIndex + 1).equals("\n")) {
                            spaceCharIndex--;
                            Log.e("charlim :", Integer.toString(spaceCharIndex) + "isSpace?: " + string.substring(spaceCharIndex, spaceCharIndex + 1));
                        }

                        linedAdvice.add(string.substring(i, spaceCharIndex + 1));
                        i = spaceCharIndex + 1;
                        Log.e("the i no space", Integer.toString(i));

                    }
                } else {
                    linedAdvice.add(string.substring(i));
                    break;
                }


            }
        } else linedAdvice.add(string);
    }


    private void stringProcessorFlex(String string, int charLim) {
        linedAdvice.clear();


        if (string.length() > charLim && (string.contains(" ") || string.contains("\n"))) {


            int i = 0, spaceCharIndex;
            while (true) {

                if (string.length() - 1 - i >= charLim) {
                    if (string.substring(i + charLim - 1, i + charLim).equals(" ") || string.substring(i + charLim - 1, i + charLim).equals("\n")) {
                        linedAdvice.add(string.substring(i, i + charLim));
                        i += charLim;
                        Log.e("the i space", Integer.toString(i));

                    } else {
                        spaceCharIndex = i + charLim - 1;

                        while (!string.substring(spaceCharIndex, spaceCharIndex + 1).equals(" ") && !string.substring(spaceCharIndex, spaceCharIndex + 1).equals("\n")) {
                            spaceCharIndex--;
                            Log.e("charlim :", Integer.toString(spaceCharIndex) + "isSpace?: " + string.substring(spaceCharIndex, spaceCharIndex + 1));
                        }

                        linedAdvice.add(string.substring(i, spaceCharIndex + 1));
                        i = spaceCharIndex + 1;
                        Log.e("the i no space", Integer.toString(i));

                    }
                } else {
                    linedAdvice.add(string.substring(i));
                    break;
                }


            }
        } else linedAdvice.add(string);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
//        if(item.getItemId() == R.id.sharePres)
//        {   Button b = new Button(this);
//            b.setText("share");
//            print(b);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (printActivty) {
            reset();
            printActivty = false;

            stopService(new Intent(MainActivity.this, ChatHeadService.class));

        }
    }


    public void autoPrescribe(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription." + diagnosises.getText().toString(), context.MODE_PRIVATE);
        String[] temp;
        try {
            temp = sharedPreferences
                    .getString("android.ztech.com.prescription." + diagnosises.getText().toString(), "").split("\\$\\$");
        } catch (Exception e) {
            Toast.makeText(context, "NO SUCH PRES. WAS SAVED", Toast.LENGTH_LONG).show();

            return;
        }


        String temp1;

        Log.e("the string", temp1 = sharedPreferences.getString("android.ztech.com.prescription." + diagnosises.getText().toString(), ""));

        for (int i = 0; i < temp.length; i++) {
            if (i == 0 && !temp[0].isEmpty()) add_symptoms(temp[0].split("\\$"));
            else if (i == 1 && !temp[1].isEmpty()) add_test(temp[1].split("\\$"));
            else if (i == 2 && !temp[2].isEmpty()) {
                create_new_drug(temp[2].split("\\$"), null);
            }

            else if (i == 3 && !temp[3].isEmpty()&&!temp[3].contains("$")) advices.setText(temp[3]);
            //            else if (i == 4 && !temp[4].isEmpty()) emergency.setText(temp[4]);
        }

    }




    private int drawAndComputePages() {

         // here 30 = 1 c. m
        int titleBaseLine = 60, pageNumber = 1;
        //int leftMargin = 100;
        final int lineGap = 3;

        PdfDocument.Page page;
        page = mPdfDocument.startPage(1);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.REG", context.MODE_PRIVATE);
        int regNo = sharedPreferences.getInt("android.ztech.com.prescription.REG", 1);
        paint.setTextSize(15);
       // canvas.drawText("নবজাতক, শিশু  ও  কিশোর রোগ বিশেষজ্ঞ", 72*2.4f, titleBaseLine,paint);
        titleBaseLine+=30;

        paint.setColor(Color.BLACK);
        float left = 72 * 0.5f;
        float left_rightAl = 72 * 5.5f;
        float leftTextStart = left+ 72 * 0.3f;
        paint.setTextSize(16);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("অধ্যক্ষ  ডাঃ   মোঃ   আবু   সুফিয়ান", left, titleBaseLine,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        titleBaseLine+=20;
        paint.setTextSize(12);
        canvas.drawText("এমবিবিএস,   ডিসিএইচ,   এফসিপিএস", left, titleBaseLine,paint);
        titleBaseLine+=15;
        canvas.drawText("শেখ   হাসিনা  মেডিকেল   কলেজ,  হবিগঞ্জ", left, titleBaseLine,paint);


        titleBaseLine = 60+30;
        paint.setTextSize(14);
        canvas.drawText("চেম্বারঃ ", left_rightAl, titleBaseLine,paint);
        titleBaseLine+=15;
        paint.setTextSize(15);

       canvas.drawText("কনসালটেন্ট ডায়াগনস্টিক সেন্টার ", left_rightAl, titleBaseLine,paint);
        titleBaseLine+=17;
        paint.setTextSize(12);
        canvas.drawText("পুরাতন পাসপোর্ট ভবন", left_rightAl, titleBaseLine,paint);
        titleBaseLine+=15;
        canvas.drawText("সদর হাসপাতাল সংলগ্ন, হবিগঞ্জ।", left_rightAl, titleBaseLine,paint);
        titleBaseLine+=20;
        paint.setTextSize(15);
        canvas.drawText("সময়ঃ বিকাল ৩টা - ৭টা ", left_rightAl, titleBaseLine,paint);
        titleBaseLine+=10;





//        paint.setTextSize(10);
//        canvas.drawText("অধ্যক্ষ  ডাঃ   মোঃ   আবু   সুফিয়ান,  শিশু বিশেষজ্ঞ,  এমবিবিএস,   ডিসিএইচ,   এফসিপিএস।  অধ্যক্ষ, শেখ   হাসিনা  মেডিকেল   কলেজ,  হবিগঞ্জ ",
//                72 * 0.3f, titleBaseLine, paint);


//        canvas.drawText("Reg. no." + ((TextView)findViewById(R.id.regNo)).getText().toString() + ", Date- " + dateTextView.getText().toString(),
//                72 * 0.2f, titleBaseLine, paint);
//
//        titleBaseLine+=15;
//        if(!names.getText().toString().isEmpty()) {
//            canvas.drawText("Patients name :   " + names.getText().toString(),72 * 0.2f, titleBaseLine, paint);
//        }
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(72*0.1f ,144+30+15,72*2.5f,titleBaseLine+15,paint);
//        paint.setStyle(Paint.Style.FILL);

        //canvas.drawText("Date- " + dateTextView.getText().toString(), 72*5, titleBaseLine, paint);



       canvas.drawLine(72 * 0.1f, (float) (titleBaseLine), 72 * 8, (float) (titleBaseLine), paint);
        titleBaseLine+=30;
         paint.setTextSize(10);
         canvas.drawText("সিরিয়ালঃ",72*5.5f, titleBaseLine, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("01716936334", 72*6.12f, titleBaseLine, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        titleBaseLine+=15;
        canvas.drawText("Reg. no." + ((TextView)findViewById(R.id.regNo)).getText().toString() + ", Date- " + dateTextView.getText().toString(),
                72*5.5f, titleBaseLine, paint);
        titleBaseLine-=15;
        String lineOne = "";



        if(!names.getText().toString().isEmpty()) {
            lineOne+="Patient's name : "+names.getText().toString()+",   ";
            //canvas.drawText("Patients name :   " + names.getText().toString(),leftTextStart, titleBaseLine, paint);
        }


        if (!days.getText().toString().isEmpty() || !months.getText().toString().isEmpty()
                || !years.getText().toString().isEmpty()) {

            String yearsT = years.getText().toString().isEmpty()? "00" : years.getText().toString();
            String monthsT = months.getText().toString().isEmpty()?"00" : months.getText().toString();
            String daysT = days.getText().toString().isEmpty()?"00" : days.getText().toString();
            lineOne +=     yearsT +
                    " বছর  " + monthsT + " মাস  " + daysT + " দিন,   ";
//            canvas.drawText("বয়স : " + yearsT +
//                    " বছর  " + monthsT + " মাস  " + daysT + " দিন  ", leftTextStart, titleBaseLine, paint);

        }


        TextView txt = findViewById(R.id.weightGram);
        if (!weight.getText().toString().isEmpty() || !txt.getText().toString().isEmpty()) {

            String weightKg = weight.getText().toString().isEmpty()? "00" : weight.getText().toString();
            String weightGm = txt.getText().toString().isEmpty()? "00": txt.getText().toString();
            lineOne += weightKg + " কেজি " + weightGm +" গ্রাম ";

        }

        if(!lineOne.isEmpty()) {
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(lineOne, left, titleBaseLine, paint);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        }



        //titleBaseLine = titleBaseLine + lineGap;// done later
        if (!patientcell.getText().toString().isEmpty()) {
            titleBaseLine += 15;
            paint.setTextSize(10);
            canvas.drawText("রোগীর ফোন নং:" + patientcell.getText().toString(), left, titleBaseLine, paint);
        }


        if(!isRegSavedOnce) {
            sharedPreferences = getSharedPreferences("android.ztech.com.prescription.REG", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("android.ztech.com.prescription.REG", ++regNo);
            editor.commit();
            isRegSavedOnce=true;
        }


//        if(!names.getText().toString().isEmpty()) {
//            canvas.drawText("নাম :   " + names.getText().toString(), 72 * 0.8f, titleBaseLine, paint);
//        }
//        titleBaseLine+=10;
//        paint.setTextSize(15);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        //canvas.drawText("প্রেসক্রিপশনটি পড়ে ঔষধ ব্যবহার করবেন", 72 * 0.8f,  titleBaseLine, paint);
  //      paint.setTypeface(Typeface.DEFAULT);




//        paint.setTextSize(12);
//        if (!diagnosises2.getText().toString().isEmpty()) {
//            paint.setTextSize(12);
//            titleBaseLine = titleBaseLine + 12 + lineGap;
//            canvas.drawText("রোগ :  " + diagnosises2.getText().toString(), 72 * 0.8f, titleBaseLine, paint);
//        }


//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(left ,144+30+15,right,titleBaseLine+10,paint);
//        paint.setStyle(Paint.Style.FILL);
//        titleBaseLine+=30;

        if (!diagnosises2.getText().toString().isEmpty()) {
            paint.setTextSize(10);//text size updated from 8
            int titleTop= titleBaseLine + 5;
            titleBaseLine+= 24;
            stringProcessorFlex("Patient's Note :  " + diagnosises2.getText().toString(),110);
            for(int i = 0; i<linedAdvice.size();i++) {
                canvas.drawText(linedAdvice.get(i), 72 * 0.8f, titleBaseLine, paint);
                titleBaseLine+= 12+ lineGap;
                if (titleBaseLine >= 792) {
                    mPdfDocument.finishPage(page);
                    pageNumber++;
                    titleBaseLine = 72;
                    page = mPdfDocument.startPage(pageNumber);
                }
            }
           // canvas.drawLine(72 * 0.8f, (float) (titleBaseLine), 72 * 8, (float) (titleBaseLine), paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(72 * 0.5f,titleTop,72*8,++titleBaseLine,paint);

            paint.setStyle(Paint.Style.FILL);
            titleBaseLine+=42;


        }

        int i;
        titleBaseLine+= 30;


        int invesTitleBaseLine = titleBaseLine;
        AutoCompleteTextView autoCompleteTextView;

        if (doSympExist == true) {
            paint.setTextSize(12);
            paint.setUnderlineText(true);
            canvas.drawText("উপসর্গ  :", left , titleBaseLine, paint);
            paint.setUnderlineText(false);

            titleBaseLine = titleBaseLine + 18;
            paint.setTextSize(12);

            for (i = 0; i <= symptomIdArray.size() - 1; i++) {
                autoCompleteTextView = (AutoCompleteTextView) findViewById(symptomIdArray.get(i));
                if (!autoCompleteTextView.getText().toString().isEmpty()) {
                    stringProcessorFlex(autoCompleteTextView.getText().toString(), 40);

                    for (int j = 0; j <= linedAdvice.size() - 1; j++) {

                        if (j == 0) {
                            canvas.drawText(Integer.toString(i + 1) + ") " + linedAdvice.get(j), left , titleBaseLine, paint);
                            titleBaseLine = titleBaseLine + 8;
                            if (titleBaseLine >= 792) {
                                mPdfDocument.finishPage(page);
                                pageNumber++;
                                titleBaseLine = 72;
                                page = mPdfDocument.startPage(pageNumber);
                            }
                        } else {
                            canvas.drawText(linedAdvice.get(j), left, titleBaseLine, paint);

                            titleBaseLine = titleBaseLine + 10;


                            if (titleBaseLine >= 792) {
                                mPdfDocument.finishPage(page);
                                pageNumber++;
                                titleBaseLine = 72;
                                page = mPdfDocument.startPage(pageNumber);
                            }
                        }


                    }


                    titleBaseLine += 12;
                    if (titleBaseLine >= 792) {
                        mPdfDocument.finishPage(page);
                        titleBaseLine = 72;
                        pageNumber++;
                        page = mPdfDocument.startPage(pageNumber);
                    }

                }
            }

            titleBaseLine += 10;
            if (titleBaseLine >= 792) {
                mPdfDocument.finishPage(page);
                titleBaseLine = 72;
                pageNumber++;
                page = mPdfDocument.startPage(pageNumber);
            }

        }
        //        paint.setTextSize(12);
        //        paint.setUnderlineText(true);
        //        canvas.drawText("টেস্ট :",72*0.8f, invesTitleBaseLine + 4, paint);
        //        paint.setUnderlineText(false);


        if (doTestExist == true) {

            paint.setTextSize(12);
            invesTitleBaseLine = invesTitleBaseLine + 18;
            if (invesTitleBaseLine >= 792) {
                mPdfDocument.finishPage(page);
                invesTitleBaseLine = 72;
                pageNumber++;
                page = mPdfDocument.startPage(pageNumber);
            }


            for (i = 0; i <= investigationIdArray.size() - 1; i++) {
                autoCompleteTextView = (AutoCompleteTextView) findViewById(investigationIdArray.get(i));

                if (!autoCompleteTextView.getText().toString().isEmpty()) {
                    stringProcessorFlex(autoCompleteTextView.getText().toString(), 40);


                    for (int j = 0; j <= linedAdvice.size() - 1; j++) {

                        if (j == 0) {
                            canvas.drawText(Integer.toString(i + 1) + ") " + linedAdvice.get(j), left, invesTitleBaseLine, paint);
                            invesTitleBaseLine = invesTitleBaseLine + 8;
                            if (invesTitleBaseLine >= 792) {
                                mPdfDocument.finishPage(page);
                                pageNumber++;
                                invesTitleBaseLine = 72;
                                page = mPdfDocument.startPage(pageNumber);
                            }
                        } else {
                            canvas.drawText(linedAdvice.get(j), left, invesTitleBaseLine, paint);
                            invesTitleBaseLine = invesTitleBaseLine + 8;
                            if (invesTitleBaseLine >= 792) {
                                mPdfDocument.finishPage(page);
                                pageNumber++;
                                invesTitleBaseLine = 72;
                                page = mPdfDocument.startPage(pageNumber);
                            }
                        }

                    }

                    invesTitleBaseLine = invesTitleBaseLine + 9;
                    if (invesTitleBaseLine >= 792) {
                        mPdfDocument.finishPage(page);
                        invesTitleBaseLine = 72;
                        pageNumber++;
                        page = mPdfDocument.startPage(pageNumber);
                    }


                }
            }
        }


        if (invesTitleBaseLine > titleBaseLine) titleBaseLine = invesTitleBaseLine;

        if (titleBaseLine < 72 * 5) {
            titleBaseLine = 72 * 5;

        }

        if (titleBaseLine >= 792) {
            mPdfDocument.finishPage(page);
            titleBaseLine = 72;
            pageNumber++;
            page = mPdfDocument.startPage(pageNumber);
        }

        paint.setTextSize(12);
        paint.setUnderlineText(true);
        //        canvas.drawText("\u0994ষধ :", 72 * 0.8f, titleBaseLine, paint);
        paint.setUnderlineText(false);

        paint.setTextSize(11);
        titleBaseLine += 18-72;
        titleBaseLine+=18;



        int j;
        for (i = 0; i <= medicationIdArray.size() - 1; i++) {
            autoCompleteTextView = (AutoCompleteTextView) findViewById(medicationIdArray.get(i));


            if (!autoCompleteTextView.getText().toString().isEmpty()) {
                stringProcessorFlex(autoCompleteTextView.getText().toString(),100);
                for (j = 0; j <= linedAdvice.size() - 1; j++) {

                    if (j == 0) {


                        canvas.drawText("* " + linedAdvice.get(j), left, titleBaseLine, paint);
                        titleBaseLine += 12;
                        if (titleBaseLine >= 792) {
                            mPdfDocument.finishPage(page);
                            pageNumber++;
                            titleBaseLine = 72;
                            page = mPdfDocument.startPage(pageNumber);
                        }


                    } else {
                        canvas.drawText(linedAdvice.get(j), left, titleBaseLine, paint);
                        titleBaseLine = titleBaseLine + 12;
                        if (titleBaseLine >= 792) {
                            mPdfDocument.finishPage(page);
                            pageNumber++;
                            titleBaseLine = 72;
                            page = mPdfDocument.startPage(pageNumber);
                        }
                    }



                }
                titleBaseLine+=18;
            }


        }


        titleBaseLine = titleBaseLine + 130;

        if (titleBaseLine >= 792) {
            mPdfDocument.finishPage(page);
            pageNumber++;
            titleBaseLine = 72;
            page = mPdfDocument.startPage(pageNumber);
        }

        if (!advices.getText().toString().isEmpty()) {

            paint.setTextSize(12);
            paint.setUnderlineText(true);
            canvas.drawText("পরামর্শ :", 72*0.8f, titleBaseLine, paint);
            paint.setUnderlineText(false);
            titleBaseLine = titleBaseLine + 14;
            paint.setTextSize(11);
            if (advices.getText().toString() != null) {
                stringProcessorFlex(advices.getText().toString(),100);

                for (i = 0; i <= linedAdvice.size() - 1; i++) {
                    canvas.drawText(linedAdvice.get(i), 72*0.8f, titleBaseLine, paint);
                    titleBaseLine = titleBaseLine + lineGap + 12;
                    if (titleBaseLine >= 792) {
                        mPdfDocument.finishPage(page);
                        pageNumber++;
                        titleBaseLine = 72;
                        page = mPdfDocument.startPage(pageNumber);
                    }
                }
            }

        }

        paint.setTextSize(10);
        paint.setUnderlineText(false);
      //  canvas.drawText("** চিকিৎসা চলাকালীন রোগী গুরুতর অসুস্থ হলে হাসপাতালে ভর্তি করবেন **",72 *2.25f, 72*11+18, paint);
        canvas.drawText("** চিকিৎসা চলাকালীন রোগী গুরুতর অসুস্থ হলে হাসপাতালে ভর্তি করবেন **",72 *2.25f, 72*11+36, paint);
        mPdfDocument.finishPage(page);

        return pageNumber;
    }


    public void add_symptoms(View v) {


        //deleting symptoms is not properly defined

        LinearLayout symptom_container = new LinearLayout(this);


        //adding bullets
        ImageView bullets = new ImageView(this);
        bullets.setImageResource(R.drawable.bullet_tab);
        symptom_container.addView(bullets, drugParamsMin);

        //adding symptoms
        AutoCompleteTextView symptom = new AutoCompleteTextView(this);
        symptom.setAdapter(symptomsAdapter);
        symptom.setTextSize(20);
        symptom.setThreshold(1);
        symptom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        int temp;
        symptom.setId(temp = idGenerator.generateViewId());

        if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
        else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size()-1))+1,temp);

        symptomIdArray.add(temp);
        symptom_container.addView(symptom, drugParamsMedium);
        symptom_deleter.addView(symptom_container);
        symptom.requestFocus();

        //    //defining delete button
        //    Button deleteButton =new Button(this);
        //    deleteButton.setBackgroundResource(R.drawable.del_image);
        //    deleteButton.setId();
        //    delete_the_test_buttons.add(deleteButton);

        //   symptoms.add(symptom_container);


        //    for(Button button : delete_the_test_buttons){
        //
        //        button.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                int Index = v.getId()-1;
        //
        //                deleteTheRemoveButton2 =delete_the_test_buttons.get(Index);
        //                deleteTheTest=tests.get(Index);
        //                test_deleter.removeView(deleteTheRemoveButton2);
        //                test_deleter.removeView(deleteTheTest);
        //
        //            }
        //
        //        });
        //    }

    }


    private void add_symptoms(String[] symptomString) {

        for (int i = 0; i < symptomString.length; i++) {
            //deleting symptoms is not properly defined

            LinearLayout symptom_container = new LinearLayout(this);


            //adding bullets
            ImageView bullets = new ImageView(this);
            bullets.setImageResource(R.drawable.bullet_tab);
            symptom_container.addView(bullets, drugParamsMin);

            //adding symptoms
            AutoCompleteTextView symptom = new AutoCompleteTextView(this);
            symptom.setAdapter(symptomsAdapter);
            symptom.setText(symptomString[i]);
            symptom.setTextSize(20);
            symptom.setThreshold(1);
            symptom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            int temp;
            symptom.setId(temp = idGenerator.generateViewId());

            if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
            else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size()-1))+1,temp);

            symptomIdArray.add(temp);
            symptom_container.addView(symptom, drugParamsMedium);
            symptom_deleter.addView(symptom_container);


            //    //defining delete button
            //    Button deleteButton =new Button(this);
            //    deleteButton.setBackgroundResource(R.drawable.del_image);
            //    deleteButton.setId();
            //    delete_the_test_buttons.add(deleteButton);

            //   symptoms.add(symptom_container);


            //    for(Button button : delete_the_test_buttons){
            //
            //        button.setOnClickListener(new View.OnClickListener() {
            //            @Override
            //            public void onClick(View v) {
            //
            //                int Index = v.getId()-1;
            //
            //                deleteTheRemoveButton2 =delete_the_test_buttons.get(Index);
            //                deleteTheTest=tests.get(Index);
            //                test_deleter.removeView(deleteTheRemoveButton2);
            //                test_deleter.removeView(deleteTheTest);
            //
            //            }
            //
            //        });
            //    }


        }
    }


    private void add_test(String[] testStrings) {

        for (int i = 0; i < testStrings.length; i++) {
            LinearLayout test_container = new LinearLayout(this);


            //adding bullets
            ImageView bullets = new ImageView(this);
            bullets.setImageResource(R.drawable.bullet_tab);
            test_container.addView(bullets, drugParamsMin);

            //adding tests
            AutoCompleteTextView test = new AutoCompleteTextView(this);
            int temp;
            test.setId(temp = idGenerator.generateViewId());



            if(investigationIdArray.isEmpty())
            {
                if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
                else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size() - 1)) + 1, temp);

            }
            else commonIdArray.add(commonIdArray.indexOf(investigationIdArray.get(investigationIdArray.size() - 1)) + 1, temp);


            investigationIdArray.add(temp);
            test.setText(testStrings[i]);
            test.setAdapter(testAdapter);
            test.setThreshold(1);
            test.setTextSize(25);
            test.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            test_container.addView(test, drugParamsMedium);


            //    //defining delete button
            //    Button deleteButton =new Button(this);
            //    deleteButton.setBackgroundResource(R.drawable.del_image);
            //    deleteButton.setId();
            //    delete_the_test_buttons.add(deleteButton);
            //    test_deleter.addView(deleteButton,deleteButtonParams);
            tests.add(test_container);
            test_deleter.addView(test_container);


            //    for(Button button : delete_the_test_buttons){
            //
            //        button.setOnClickListener(new View.OnClickListener() {
            //            @Override
            //            public void onClick(View v) {
            //
            //                int Index = v.getId()-1;
            //
            //                deleteTheRemoveButton2 =delete_the_test_buttons.get(Index);
            //                deleteTheTest=tests.get(Index);
            //                test_deleter.removeView(deleteTheRemoveButton2);
            //                test_deleter.removeView(deleteTheTest);
            //
            //            }
            //
            //        });
            //    }

        }
    }


    public void add_test(View v) {


        LinearLayout test_container = new LinearLayout(this);


        //adding bullets
        ImageView bullets = new ImageView(this);
        bullets.setImageResource(R.drawable.bullet_tab);
        test_container.addView(bullets, drugParamsMin);

        //adding tests
        AutoCompleteTextView test = new AutoCompleteTextView(this);
        int temp;
        test.setId(temp = idGenerator.generateViewId());


        if(investigationIdArray.isEmpty())
        {
            if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
            else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size() - 1)) + 1, temp);

        }
        else commonIdArray.add(commonIdArray.indexOf(investigationIdArray.get(investigationIdArray.size() - 1)) + 1, temp);

        investigationIdArray.add(temp);
        test.setAdapter(testAdapter);
        test.setThreshold(1);
        test.setTextSize(25);
        test.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        test_container.addView(test, drugParamsMedium);


        //    //defining delete button
        //    Button deleteButton =new Button(this);
        //    deleteButton.setBackgroundResource(R.drawable.del_image);
        //    deleteButton.setId();
        //    delete_the_test_buttons.add(deleteButton);
        //    test_deleter.addView(deleteButton,deleteButtonParams);
        tests.add(test_container);
        test_deleter.addView(test_container);
        test.requestFocus();

        //    for(Button button : delete_the_test_buttons){
        //
        //        button.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                int Index = v.getId()-1;
        //
        //                deleteTheRemoveButton2 =delete_the_test_buttons.get(Index);
        //                deleteTheTest=tests.get(Index);
        //                test_deleter.removeView(deleteTheRemoveButton2);
        //                test_deleter.removeView(deleteTheTest);
        //
        //            }
        //
        //        });
        //    }
    }


    public void create_new_drug(View v) {


        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout drugReq = new LinearLayout(this);
        //        drugReq.setId(utilityPrescription.generateViewId());
        final LinearLayout drugReqPart2 = new LinearLayout(this);


        drugReq.setOrientation(LinearLayout.HORIZONTAL);
        drugReqPart2.setOrientation(LinearLayout.HORIZONTAL);


        //        //adding bullets
        //        ImageView bullets = new ImageView(this);
        //        bullets.setImageResource(R.drawable.bullet_tab);
        //        drugReq.addView(bullets, drugParamsMin);
        //        int temp;

        //        defining drug suggetion
        //        AutoCompleteTextView drugSuggestion = new AutoCompleteTextView(this);//meaning the 50mg or 1g
        //        drugSuggestion.setAdapter(suggestionAdapter);
        //        drugSuggestion.setThreshold(1);
        //        drugSuggestion.setId(idGenerator.generateViewId());
        //


        final Button editButton = new Button(context);
        editButton.setText("Del");
        editButton.setId(idGenerator.generateViewId());
        drugReq.addView(editButton, drugParamsMin);



        final Spinner mSpinner = new Spinner(context,Spinner.MODE_DIALOG);
        //defining drug text box





        int temp;
        final AutoCompleteTextView new_drug = new AutoCompleteTextView(this);
        new_drug.setAdapter(adapter);
        new_drug.setId(temp=idGenerator.generateViewId());
        new_drug.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        new_drug.setSingleLine(false);

        if(medicationIdArray.isEmpty()) {

            if(investigationIdArray.isEmpty())
            {   if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
            else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size() - 1)) + 1, temp);
            }
            else commonIdArray.add(commonIdArray.indexOf(investigationIdArray.get(investigationIdArray.size() - 1)) + 1, temp);

        }
        else commonIdArray.add(commonIdArray.indexOf(medicationIdArray.get(medicationIdArray.size()-1))+1,temp);


        medicationIdArray.add(temp);

        new_drug.setThreshold(1);
        new_drug.setTextSize(20);
        new_drug.setHint("Type in the drugs here....");



        mSpinner.setAdapter(shortcutsAdapter);
        mSpinner.setSelection(0);
        mSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                mSpinner.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        final Spinner quantSpinner = new Spinner(context,Spinner.MODE_DIALOG);
        quantSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", " ১"," ০", " ২", " ৩", " আধা"}));
        quantSpinner.setSelection(0);
        quantSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        quantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                quantSpinner.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //I'll write the code here InshaAllah
            }
        });

        final Spinner mediumSpinner = new Spinner(context,Spinner.MODE_DIALOG);

        mediumSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", "টা ", "চামচ ", "এম্পুল ", "মি.লি. ","ফোটা ", "লাগাবেন "}));
        mediumSpinner.setSelection(0);
        mediumSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        mediumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                mediumSpinner.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //I'll write the code here InshaAllah
            }
        });


        final Spinner hourSpinner = new Spinner(context,Spinner.MODE_DIALOG);
        hourSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", "৬ ঘন্টা অন্তর ", "৮ ঘন্টা অন্তর ", "১২ ঘন্টা অন্তর ", "রোজ ১ বার সন্ধ্যায় "}));
        hourSpinner.setSelection(0);
        hourSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                hourSpinner.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //I'll write the code here InshaAllah
            }

        });

        final Spinner daySpinner = new Spinner(context,Spinner.MODE_DIALOG);
        daySpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", "৭ দিন ", "১৫ দিন ", "১ মাস ","চলবে "}));
        daySpinner.setSelection(0);
        daySpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                daySpinner.setSelection(0);


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //I'll write the code here InshaAllah
            }

        });


        drugReqPart2.addView(quantSpinner);
        drugReqPart2.addView(mediumSpinner);
        drugReqPart2.addView(hourSpinner);
        drugReqPart2.addView(daySpinner);
        drugReq.addView(new_drug, drugParamsMedium);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                int i;
                String toBeEdited = new_drug.getText().toString();


                if (!toBeEdited.isEmpty()&&loadedMedication.contains(toBeEdited)) {

                    loadedMedication.remove(toBeEdited);
                    adapter = new ArrayAdapter<String>(context, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
                    new_drug.setText("");
                    new_drug.setAdapter(adapter);
                    new PostTask().execute(toBeEdited);




                } else if (!toBeEdited.isEmpty())
                    Toast.makeText(context, "Deletion failed -> App's drug or Unsaved", Toast.LENGTH_LONG).show();


            }

        });


        //
        //        //defining when the patient should take drugs
        //        TextView spinnerHint = new TextView(this);
        //        spinnerHint.setText("Hourly");
        //        spinnerHint.setTextSize(16);
        //        spinnerHint.setTypeface(null, Typeface.BOLD);

        //        final Spinner frequencyOfTakingMedicine = new Spinner(this);
        //        frequencyOfTakingMedicine.setAdapter(frequencyAdapter);

        //defining BiD, QiD, etc spinner hint

        TextView timeAndAmount = new TextView(this);
        timeAndAmount.setText("Investigation :");
        timeAndAmount.setTextSize(19);
        timeAndAmount.setTypeface(null, Typeface.BOLD);


        //        //defining delete button
        //        Button deleteButton =new Button(this);
        //        deleteButton.setBackgroundResource(R.drawable.del_image);
        //        deleteButton.setId(view_id);
        //        delete_the_drug_buttons.add(deleteButton);


        //now adding everything to the screen

        //        layout.addView(drugReqPart2);
        //        layout.addView(mSpinner);


        //        layout.addView(drugSuggestion,suggestionParams);
        //        layout.addView(editButton, 150, 150);
        //

        new_drug.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    layout.addView(drugReqPart2);
                    layout.addView(mSpinner);


                }

                else
                {
                    layout.removeView(mSpinner);
                    layout.removeView(drugReqPart2);

                }



            }
        });

        layout.addView(drugReq);
        deleter.addView(layout);
        new_drug.requestFocus();
    }



    public void waiting()
    {


        //should be implemtnted if things get slower



    }
    private void create_new_drug(String[] drugStrings, String[] directionStrings) {
        for (int i = 0; i < drugStrings.length; i++) {

            final LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout drugReq = new LinearLayout(this);
            final LinearLayout drugReqPart2 = new LinearLayout(this);


            drugReq.setOrientation(LinearLayout.HORIZONTAL);
            drugReqPart2.setOrientation(LinearLayout.HORIZONTAL);


            //            //adding bullets
            //            ImageView bullets = new ImageView(this);
            //            bullets.setImageResource(R.drawable.bullet_tab);
            //            drugReq.addView(bullets, drugParamsMin);
            int temp;

            //            //defining drug suggetion
            //            final AutoCompleteTextView drugSuggestion = new AutoCompleteTextView(this);//meaning the 50mg or 1g
            //            drugSuggestion.setAdapter(suggestionAdapter);
            //            drugSuggestion.setThreshold(1);
            //            drugSuggestion.setId(temp = idGenerator.generateViewId());
            //            suggestionIdArray.add(temp);
            //

            final Button editButton = new Button(context);
            editButton.setText("Del");
            editButton.setId(idGenerator.generateViewId());
            drugReq.addView(editButton, drugParamsMin);

            final Spinner mSpinner = new Spinner(context,Spinner.MODE_DIALOG);
            //defining drug text box
            final AutoCompleteTextView new_drug = new AutoCompleteTextView(this);
            new_drug.setAdapter(adapter);
            new_drug.setId(temp = idGenerator.generateViewId());

            if(medicationIdArray.isEmpty()) {

                if(investigationIdArray.isEmpty())
                {   if(symptomIdArray.isEmpty())commonIdArray.add(7,temp);
                else commonIdArray.add(commonIdArray.indexOf(symptomIdArray.get(symptomIdArray.size() - 1)) + 1, temp);
                }
                else commonIdArray.add(commonIdArray.indexOf(investigationIdArray.get(investigationIdArray.size() - 1)) + 1, temp);

            }
            else commonIdArray.add(commonIdArray.indexOf(medicationIdArray.get(medicationIdArray.size()-1))+1,temp);

            medicationIdArray.add(temp);
            new_drug.setThreshold(1);
            new_drug.setText(drugStrings[i]);
            new_drug.setTextSize(20);
            new_drug.setHint("Type in the drugs here....");



            mSpinner.setAdapter(shortcutsAdapter);
            mSpinner.setSelection(0);
            mSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));


            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    mSpinner.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });


            final Spinner quantSpinner = new Spinner(context,Spinner.MODE_DIALOG);
            quantSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", " ১"," ০"," ২", " ৩", " আধা"}));
            quantSpinner.setSelection(0);
            quantSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            quantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    quantSpinner.setSelection(0);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //I'll write the code here InshaAllah
                }

            });

            final Spinner mediumSpinner = new Spinner(context,Spinner.MODE_DIALOG);
            mediumSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2,
                    new String[]{"", "টা ", "চামচ ", "এম্পুল ", "মি.লি.","ফোটা","লাগাবেন"}));
            mediumSpinner.setSelection(0);
            mediumSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            mediumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    mediumSpinner.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //I'll write the code here InshaAllah
                }

            });


            final Spinner hourSpinner = new Spinner(context,Spinner.MODE_DIALOG);
            hourSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", "৪ ঘন্টা অন্তর ","৬ ঘন্টা অন্তর ", "৮ ঘন্টা অন্তর ", "১২ ঘন্টা অন্তর ", "রোজ একবার সন্ধ্যায় "}));
            hourSpinner.setSelection(0);
            hourSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    hourSpinner.setSelection(0);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            final Spinner daySpinner = new Spinner(context,Spinner.MODE_DIALOG);
            daySpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, new String[]{"", "৭ দিন ", "১৫ দিন ", "১ মাস ","চলবে "}));
            daySpinner.setSelection(0);
            daySpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    daySpinner.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }


            });


            drugReqPart2.addView(quantSpinner);
            drugReqPart2.addView(mediumSpinner);
            drugReqPart2.addView(hourSpinner);
            drugReqPart2.addView(daySpinner);
            drugReq.addView(new_drug, drugParamsMedium);





            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i;


                    String toBeEdited = new_drug.getText().toString();

                    if (!toBeEdited.isEmpty()) {

                        loadedMedication.remove(toBeEdited);
                        adapter = new ArrayAdapter<String>(context, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
                        new_drug.setText("");
                        new_drug.setAdapter(adapter);
                        new PostTask().execute(toBeEdited);



                    }
                    else if (!toBeEdited.isEmpty())
                        Toast.makeText(context, "Deletion failed, App's drug or Unsaved", Toast.LENGTH_LONG).show();


                }

            });


            TextView timeAndAmount = new TextView(this);
            timeAndAmount.setText("Investigation :");
            timeAndAmount.setTextSize(19);
            timeAndAmount.setTypeface(null, Typeface.BOLD);


            //        //defining delete button
            //        Button deleteButton =new Button(this);
            //        deleteButton.setBackgroundResource(R.drawable.del_image);
            //        deleteButton.setId(view_id);
            //        delete_the_drug_buttons.add(deleteButton);


            //now adding everything to the screen


            //            layout.addView(drugReqPart2);
            //            layout.addView(mSpinner);

            new_drug.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if(hasFocus)
                    {

                        layout.addView(drugReqPart2);
                        layout.addView(mSpinner);

                    }

                    else
                    {
                        layout.removeView(mSpinner);
                        layout.removeView(drugReqPart2);

                    }



                }
            });

            //        layout.addView(drugSuggestion,suggestionParams);
            //            layout.addView(editButton, 150, 150);
            //

            layout.addView(drugReq);

            deleter.addView(layout);


        }
    }


    public void print(View view) {

        String advice="", name="", diagnosis="", symptom="", medication="", investigation = "";




        drawAndComputePages();


        AutoCompleteTextView symptoms, medications, investigations;

        String prescription = "";


        //            String emergencyAdvices = emergency.getText().toString();


        advice = advices.getText().toString();
        name = names.getText().toString();
        diagnosis = diagnosises.getText().toString();


        int i;


        if (!loadedAdvice.contains(advice)) {
            loadedAdvice.add(advice);
            adviceAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, loadedAdvice);
            advices.setAdapter(adviceAdapter);
            advice = advice + "\n";

            try {


                FileOutputStream fos = openFileOutput(FILENAME_ADVICE, context.MODE_APPEND);

                fos.write(advice.getBytes());
                fos.close();
                Toast.makeText(this, "File  saved", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

            }


        }


        //            if (!loadedEmergencyAdvice.contains(emergencyAdvices)) {
        //                loadedEmergencyAdvice.add(emergencyAdvices);
        //                emergenyAdviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedEmergencyAdvice);
        //                emergency.setAdapter(emergenyAdviceAdapter);
        //                emergencyAdvices = emergencyAdvices + "\n";
        //
        //                try {
        //
        //
        //                    FileOutputStream fos = openFileOutput(FILENAME_EMERGENGYADVICE, context.MODE_APPEND);
        //
        //                    fos.write(emergencyAdvices.getBytes());
        //                    fos.close();
        //                    Toast.makeText(this, "File  saved", Toast.LENGTH_SHORT).show();
        //
        //                } catch (Exception e) {
        //
        //                }
        //
        //
        //            }


        if (!loadedName.contains(name)) {
            loadedName.add(name);
            nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedName);
            names.setAdapter(nameAdapter);
            name = name + "\n";
            try {

                FileOutputStream fos = openFileOutput(FILENAME_NAME, context.MODE_APPEND);
                fos.write(name.getBytes());
                fos.close();

            } catch (Exception e) {
                Toast.makeText(this, "File could not be saved", Toast.LENGTH_SHORT).show();
            }


        }
        if (!loadedDiagnosis.contains(diagnosis)) {
            loadedDiagnosis.add(diagnosis);
            diagnosisAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedDiagnosis);
            diagnosises.setAdapter(diagnosisAdapter);

            diagnosis = diagnosis + "\n";
            try {


                FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS, context.MODE_APPEND);

                fos.write(diagnosis.getBytes());
                fos.close();


            } catch (Exception e) {
                Toast.makeText(this, "File could not be saved", Toast.LENGTH_SHORT).show();
            }


        }


        else diagnosises.setText("");

        if (!loadedDiagnosis2.contains(diagnosises2.getText().toString())) {
            loadedDiagnosis2.add(diagnosises2.getText().toString());
            diag2Adapter = new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2,  loadedDiagnosis2);
            diagnosises2.setAdapter(diag2Adapter);


            try {


                FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS_2, context.MODE_APPEND);

                fos.write((diagnosises2.getText().toString() + "\n").getBytes());
                fos.close();


            } catch (Exception e) {
                Toast.makeText(this, "File could not be saved", Toast.LENGTH_SHORT).show();
            }


        }


        for (i = 0; i <= symptomIdArray.size() - 1; i++) {
            symptoms = (AutoCompleteTextView) findViewById(symptomIdArray.get(i));
            symptom = symptoms.getText().toString();
            if (!symptom.isEmpty() && doSympExist == false) {

                doSympExist = true;

            }
            prescription += symptom + "$";


            if (loadedSymptoms.contains(symptom) && symptom != null) continue;
            else {
                loadedSymptoms.add(symptom);
                symptomsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedSymptoms);
                symptoms.setAdapter(symptomsAdapter);
                try {

                    symptom = symptom + "\n";
                    FileOutputStream fos = openFileOutput(FILENAME_SYMPTOMS, context.MODE_APPEND);

                    fos.write(symptom.getBytes());
                    fos.close();


                } catch (Exception e) {
                    Log.e("symptom saver method: ", "File could not be saved");
                }
            }


        }


        if (i > 0) {
            prescription += "$";
        } else prescription += "$$";


        for (i = 0; i <= investigationIdArray.size() - 1; i++) {

            investigations = (AutoCompleteTextView) findViewById(investigationIdArray.get(i));

            investigation = investigations.getText().toString();
            prescription += investigation + "$";


            if (!investigation.isEmpty() && doTestExist == false) {

                doTestExist = true;

            }


            if (loadedInvestigation.contains(investigation) && investigation != null)
                continue;

            else {
                loadedInvestigation.add(investigation);
                testAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedInvestigation);
                investigations.setAdapter(testAdapter);
                try {

                    investigation = investigation + "\n";
                    FileOutputStream fos = openFileOutput(FILENAME_INVESTIGATION, context.MODE_APPEND);

                    fos.write(investigation.getBytes());
                    fos.close();

                } catch (Exception e) {
                    Log.e(" saver method: ", "investigation File could not be saved");
                }
            }

        }


        if (i > 0) {
            prescription += "$";
        } else prescription += "$$";

        for (i = 0; i <= medicationIdArray.size() - 1; i++) {
            medications = (AutoCompleteTextView) findViewById(medicationIdArray.get(i));
            medication = medications.getText().toString();
            prescription += medication + "$";


            if ( medication.isEmpty() || loadedMedication.contains(medication)) continue;
            else {

                loadedMedication.add(userSaveDrugLastIndex, medication);
                userSaveDrugLastIndex++;
                adapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);

                try {
                    medication = medication + "\n";
                    FileOutputStream fos = openFileOutput(FILENAME_MEDICATIONS, context.MODE_APPEND);

                    fos.write(medication.getBytes());
                    fos.close();

                } catch (Exception e) {
                    Log.e("medication saver: ", "File could not be saved");
                }
            }


        }

        if (i > 0) {
            prescription += "$";
        } else prescription += "$$";


        prescription += advices.getText().toString();


        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription." + diagnosises.getText().toString(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("android.ztech.com.prescription." + diagnosises.getText().toString(), prescription);
        editor.commit();





        sharedPreferences = getSharedPreferences("android.ztech.com.prescription.REG", context.MODE_PRIVATE);
        final int regNo = (sharedPreferences.getInt("android.ztech.com.prescription.REG", 1));
        final String regNoString = Integer.toString(regNo);


        prescription = regNoString + "$$" + names.getText().toString() + "$$" + patientcell.getText().toString()
                +"$$"+diagnosises2.getText().toString()
                + "$$" + years.getText().toString() + "$$" + months.getText().toString() + "$$" +
                days.getText().toString() + "$$" + weight.getText().toString() + prescription;

        //        sharedPreferences = getSharedPreferences(
        //                regNoString + names.getText().toString() + patientcell.getText().toString()
        //                + diagnosises2.getText().toString(), context.MODE_PRIVATE);
        //
        //        editor = sharedPreferences.edit();
        //
        //        editor.putString(regNoString + names.getText().toString() + patientcell.getText().toString()
        //                + diagnosises2.getText().toString(),
        //                prescription);
        //
        //        editor.commit();
        // sending sms from here
        //            if (!patientcell.getText().toString().isEmpty()) {
        //
        //                String[] tempString = prescription.split("\\$\\$");
        //
        //                int tempStringIndex;
        //
        //                if (tempString.length < 5) {
        //
        //                    tempStringIndex = tempString.length;// or, i can also say: tempStringIndex=tempString.length-1+1
        //                    tempString = Arrays.copyOf(tempString, 5);
        //
        //
        //                    for (; tempStringIndex <= 4; tempStringIndex++) {
        //                        tempString[tempStringIndex] = "";
        //
        //                    }
        //
        //                }
        //
        //
        //                i = 0;
        //                while (i <= 2) {
        //
        //
        //                    if (tempString[i].contains("$")) {
        //
        //                        String[] tempLvlTwo = tempString[i].split("\\$");
        //                        tempString[i] = "";
        //
        //                        for (int j = 0; j < tempLvlTwo.length; j++) {
        //                            tempString[i] += Integer.toString(j + 1)
        //                                    + ")" + tempLvlTwo[j] + "\n";
        //
        //                        }
        //
        //                    } else tempString[i] += "1)" + tempString[i] + "\n";
        //
        //
        //                    i++;
        //                }
        //
        //
        //                String smsMessage =
        //                        "সিরিয়াল " + regNoString + "\nনাম " + names.getText().toString() + "\n" +
        //                                "\nরোগ " + diagnosises2.getText().toString() + "\n" +
        //                                "\nউপসর্\n" + tempString[0] + "\n" +
        //                                "\n" + tempString[1] + "\n" +
        //                                "\n\u0994ষধ\n" + tempString[2] + "\n" +
        //                                "\nউপদেশ\n" + tempString[3] + "\n";
        //
        //// here emergency advice is just not added but that exist
        //
        //
        //                final SmsManager smsManager = SmsManager.getDefault();
        //                final String SENT = "SMS_SENT";
        //
        //                registerReceiver(new BroadcastReceiver() {
        //                    @Override
        //                    public void onReceive(Context arg0, Intent arg1) {
        //
        //
        //                        switch (getResultCode()) {
        //
        //                            case Activity.RESULT_OK:
        //                                Toast.makeText(getBaseContext(), "SMS 1 sent",
        //                                        Toast.LENGTH_SHORT).show();
        //                                break;
        //
        //                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
        //                                Toast.makeText(getBaseContext(), "Generic failure",
        //                                        Toast.LENGTH_SHORT).show();
        //                                break;
        //                            case SmsManager.RESULT_ERROR_NO_SERVICE:
        //                                Toast.makeText(getBaseContext(), "No service",
        //                                        Toast.LENGTH_SHORT).show();
        //                                break;
        //                            case SmsManager.RESULT_ERROR_NULL_PDU:
        //                                Toast.makeText(getBaseContext(), "Null PDU",
        //                                        Toast.LENGTH_SHORT).show();
        //                                break;
        //                            case SmsManager.RESULT_ERROR_RADIO_OFF:
        //                                Toast.makeText(getBaseContext(), "Radio off",
        //                                        Toast.LENGTH_SHORT).show();
        //                                break;
        //                        }
        //                    }
        //                }, new IntentFilter(SENT));
        //
        //                //---when the SMS has been delivered---
        //
        //
        //                ArrayList<String> parts = smsManager.divideMessage(smsMessage);
        //
        //
        //                ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        //
        //                PendingIntent sentPI1 = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        //
        //
        //                for (int j = 0; j < parts.size(); j++) {
        //                    sentIntents.add(sentPI1);
        //                }
        //
        //
        //                smsManager.sendMultipartTextMessage("88" + patientcell.getText().toString(), null, parts, sentIntents, null);
        //
        //            }



        savePresForSharing();





        //String temp = ((names.getText().toString() + diagnosises2.getText().toString()).toLowerCase()).replaceAll(" ", "");
        String temp = (names.getText().toString()).replaceAll(" ", "");
        temp = temp.replaceAll("\n", "");
        String addressRelative = regNoString + temp + patientcell.getText().toString();

        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(imagePath, "/doc"+addressRelative+".pdf");
        Uri contentUri = FileProvider.getUriForFile(context,"android.ztech.com.prescription", newFile);
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        editor.putString("addressRelative", addressRelative);
        editor.commit();

        if (contentUri != null) {

//            try{
//                Intent share = new Intent();
//                share.setAction(Intent.ACTION_SEND);
//                share.setType("application/pdf");
//                share.putExtra(Intent.EXTRA_STREAM, contentUri);
//                share.putExtra("jid", "88"+(patientcell.getText().toString()).trim() +"@s.whatsapp.net");
//                share.setPackage("com.whatsapp");
//
//                 startActivity(share);
//
//            }
//            catch(Exception e)
//            {
//                Toast.makeText(this,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
//            }



            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.createChooser(shareIntent,"Share your prescription");
//
////            Button b = (Button)view;
////            if(b.getText().toString().equals("share")){
////
////                shareIntent = Intent.createChooser(shareIntent,"Share your prescription");
////            }
//
         //   startService(new Intent(MainActivity.this, ChatHeadService.class));

            try {
                startActivity(shareIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Can't read pdf file", Toast.LENGTH_SHORT).show();
            }


        }

        printActivty = true;

    }


    public void quickScrolling(View view)
    {
        Button b = (Button)view;

        if(isInTouchMode) {

            if (b.getText().toString().equals("Next")) {

                if(!(commonIdArray.indexOf(focusedTextField.getId())==commonIdArray.size()-1)) {
                    int viewId = commonIdArray.get(commonIdArray.indexOf(focusedTextField.getId()) + 1);
                    View v = findViewById(viewId);
                    v.requestFocus();
                }

            }


            else {

                if(!(commonIdArray.indexOf(focusedTextField.getId())==0)) {
                    View v = findViewById(commonIdArray.get(commonIdArray.indexOf(focusedTextField.getId()) - 1));

                    v.requestFocus();
                }

            }



        }
    }

    private void reset() {

        deleteCache(context);
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,1);


    }

    public void deleteDiagSugg(View v){

        AutoCompleteTextView a = findViewById(R.id.thePrintedOne);
        String toBeEdited = a.getText().toString();

        if (!toBeEdited.isEmpty()&&loadedDiagnosis2.contains(toBeEdited)) {
            a.setText("");
            loadedDiagnosis2.remove(toBeEdited);
            diag2Adapter = new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2,  loadedDiagnosis2);
            a.setAdapter(diag2Adapter);
            new deleteDiag().execute();
        }

        else   Toast.makeText(this, "Deletion failed, this wasn't saved",Toast.LENGTH_SHORT).show();

    }

    public void deleteAdvices(View v){

        AutoCompleteTextView a = findViewById(R.id.advices);
        String toBeEdited = a.getText().toString();

        if (!toBeEdited.isEmpty()&&loadedAdvice.contains(toBeEdited)) {
            a.setText("");
            loadedAdvice.remove(toBeEdited);
            adviceAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item_2, R.id.autoCompleteItem2, loadedAdvice);
            a.setAdapter(adviceAdapter);

            new deleteAdviceSugg().execute();
        }

        else   Toast.makeText(this, "Deletion failed, this wasn't saved",Toast.LENGTH_SHORT).show();


    }


    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    private void savePresForSharing()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.REG", context.MODE_PRIVATE);
        final int regNo = sharedPreferences.getInt("android.ztech.com.prescription.REG", 1);
        final String regNoString = Integer.toString(regNo);


        try {


            String temp = (names.getText().toString()).replaceAll(" ", "");



            temp = temp.replaceAll("\n", "");
            String addressRelative = regNoString + temp + patientcell.getText().toString();


            String FILENAME_PDFKEYS = "keysToPresPDF";

            File pdfSaveDir = new File(getFilesDir(), "images");
            pdfSaveDir.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(pdfSaveDir + "/doc" + addressRelative + ".pdf"); // overwrites this image every t


            SharedPreferences sharedPreferences1 = getSharedPreferences(FILENAME_PDFKEYS, MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString(FILENAME_PDFKEYS,
                    sharedPreferences1.getString(FILENAME_PDFKEYS, "") + addressRelative + "\n");
            editor1.commit();

            mPdfDocument.writeTo(stream);
            stream.close();


        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error saving", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "IO Error saving", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }




    }

    public void delAutoPrescribe(View v)
    {

        if(loadedDiagnosis.contains(diagnosises.getText().toString()))
        {
            loadedDiagnosis.remove(diagnosises.getText().toString());
            diagnosises.setText("");
            diagnosisAdapter =  new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedDiagnosis);
            diagnosises.setAdapter(diagnosisAdapter);
            new PostTask2().execute("");
        }
    }
}







