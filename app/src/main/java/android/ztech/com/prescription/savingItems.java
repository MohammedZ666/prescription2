package android.ztech.com.prescription;


import android.content.Context;


import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;


import android.net.Uri;
import android.print.pdf.PrintedPdfDocument;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import  java.util.Arrays;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class savingItems extends AppCompatActivity {


    boolean printActivty = false;
    PrintedPdfDocument mPdfDocument;
    EditText patientcell, days, months, weight, years;
    List drugs;


    String[] replacements = {

            " ১ চামচ দিনে একবার ৩০ দিন ", " ১ চামচ ১২ ঘন্টা অন্তর ৭ দিন ", " ১ চামচ ৮ ঘন্টা অন্তর ৭ দিন ",
            " ১ চামচ ৬ ঘন্টা অন্তর ৭ দিন ", " আধা চামচ দিনে একবার ৩০ দিন ", " আধা চামচ ১২ ঘন্টা অন্তর ৭ দিন ", " আধা চামচ  ৮ ঘন্টা অন্তর ৭ দিন ",
            " আধা চামচ ৬ ঘন্টা অন্তর ৭ দিন ", " একটা দিনে একবার ৩০ দিন ", " একটা ১২ ঘন্টা অন্তর ৭ দিন ", " একটা ৮ ঘন্টা অন্তর ৭ দিন ",
            " একটা ৬ ঘন্টা ৭ দিন ", " নেবুলাইজার Solution SALPIUM/SULPREX দ্বারা(Salbutamol 2.5 mg + Ipra tropium bromide 0.5 mg) ৪  " +
            "ঘন্টা অন্তর দিবেন ২ দিন । পরে শ্বাম কষ্ট কমে গেলে ৬ ঘন্টা অন্তর ৫ দিন । ", " নেবুলাইজার Solution SALPIUM/SULPREX আধা এম্পল দ্বারা(Salbutamol 2.5 mg + Ipra tropium bromide 0.5 mg) ৪  " +
            "ঘন্টা অন্তর দিবেন ২ দিন । পরে শ্বাম কষ্ট কমে গেলে ৬ ঘন্টা অন্তর ৫ দিন । ", " ONE IM রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন) ", " ONE IV রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন) ",
            " HALF IM রোজ একবার ৭ দিন (ছোট নিডেল কিনবেন )", " HALF IV  রোজ একবার ৭ দিন ", " ONE IV ১২ ঘন্টা অন্তর ৭ দিন ", " ১ চামচ ৪ ঘন্টা অন্তর জ্বর উঠলে খাবেন  এবং শরীর ভেজা গামছা দ্বারা মুছে দিবেন ",
            " একটা ৪ ঘন্টা অন্তর জ্বর উঠলে মলদ্বারে দিবেন এবং শরীর ভেজা গামছা দ্বারা মুছে দিবেন ", " 150 ml per 24 hours at a rate of 6 Microdrop per minute ",
            " রোজ ৬ ঘন্টা অন্তর আক্যান্ত স্থানে লাগাবেন ", " হাসপাতালে র্িত করবে ", " NPO till patient can take orally ", " Oxygen Inhalation stat and sos "
            , " অবস্থার উন্নতি না হলে উন্নত চিকিৎসার জন্য  REFFERED TO SMCH ", ""

    };

    boolean doSympExist = false, doTestExist = false;
    int userSaveDrugLastIndex;
    String FILENAME_ADVICE = "android.ztech.com.prescription.advice_container_new_0";
    String FILENAME_NAME = "android.ztech.com.prescription.name_container_new";
    String FILANAME_DIAGNOSIS = "android.ztech.com.prescription.diagnosis_container_1";
    String FILENAME_SYMPTOMS = "android.ztech.com.prescription.symptoms_container_023";
    String FILENAME_INVESTIGATION = "android.ztech.com.prescription.investigations_container_023";
    String FILENAME_MEDICATIONS = "android.ztech.com.prescription.medications_container_1";
    String FILENAME_DIAGNOSIS = "android.ztech.com.prescription.diagnosis_container_1";
    String FILENAME_DIAGNOSIS_2 = "android.ztech.com.prescription.diagnosis_container_2";
    String FILENAME_SUGGESTION = "suggestion_1_1";
    String FILENAME_EMERGENGYADVICE = "emergencyAdvice";
    List<Integer> symptomIdArray = new ArrayList<>();
    List<Integer> investigationIdArray = new ArrayList<>();
    List<Integer> medicationIdArray = new ArrayList<>();
    List<Integer> suggestionIdArray = new ArrayList<>();
    List<Integer> buttonSuggestionIdArray = new ArrayList<>();


    ArrayAdapter<String> adapter, diag2Adapter, testAdapter, symptomsAdapter, adviceAdapter, nameAdapter, diagnosisAdapter, emergenyAdviceAdapter;


    List<String> loadedAdvice = new ArrayList<String>();
    List<String> loadedDiagnosis = new ArrayList<String>();
    List<String> loadedDiagnosis2 = new ArrayList<String>();
    List<String> loadedSymptoms = new ArrayList<String>();
    List<String> loadedMedication;
    List<String> loadedInvestigation = new ArrayList<String>();
    List<String> loadedEmergencyAdvice = new ArrayList<String>();
    List<String> linedAdvice = new ArrayList<String>();
    ArrayList<LinearLayout> tests = new ArrayList<>();
    LinearLayout deleter, test_deleter, symptom_deleter;

    AutoCompleteTextView advices, diagnosises, diagnosises2;
    TextView dateTextView;
    LinearLayout printPagePart;
    LinearLayout.LayoutParams textViewMargin, drugParamsMax, drugParamsMedium, drugParamsMin, testParams, testBulletParams, paramsForTimeTextView, paramsForTimeEditText, drugReqViewParams, drugReqViewParams1, drugReqViewParams2, suggestionParams, doseQuantityParams, doseTextViewParams, doseUnitParams, frequencyParams;

    utilityPrescription idGenerator;
    Context context;
    SpinnerAdapter shortcutsAdapter;
    String advice, name, diagnosis, symptom, medication, investigation;
    AutoCompleteTextView emergency;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_items_stock_activity);

//        testStrings =Arrays.asList( getResources().getStringArray(R.array.medicalTests));
        drugs = Arrays.asList(getResources().getStringArray(R.array.med));
//        symptomsString =Arrays.asList(getResources().getStringArray(R.array.symptoms));
        days = (EditText) findViewById(R.id.days);
        months = (EditText) findViewById(R.id.months);
        years = (EditText) findViewById(R.id.years);
//        patientSerial=(EditText)findViewById(R.id.serial);
        weight = (EditText) findViewById(R.id.weight);
//        rmonths=(EditText)findViewById(R.id.REPORTAFTERMONTHS);
//        rdays=(EditText)findViewById(R.id.REPORTDAYS);


//        loadedSymptoms= new ArrayList<>(symptomsString);

//        loadedInvestigation = new ArrayList<>(testStrings);


        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));


        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(currentDateTimeString);


        advices = (AutoCompleteTextView) findViewById(R.id.advices_Save);
        diagnosises = (AutoCompleteTextView) findViewById(R.id.DiagnosisSave);


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

            FileInputStream fis = this.openFileInput(FILANAME_DIAGNOSIS);
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
        loadedMedication.addAll(drugs);

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


        emergenyAdviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedEmergencyAdvice);
        testAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedInvestigation);
        adapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
        symptomsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedSymptoms);
        adviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedAdvice);
        diagnosisAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedDiagnosis);
//        banglaSuggestion = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedSuggestionsBangla);
        advices.setAdapter(adviceAdapter);
        advices.setThreshold(1);


        diagnosises.setAdapter(diagnosisAdapter);

        diag2Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedDiagnosis2);
        diagnosises2.setAdapter(diag2Adapter);

        idGenerator = new utilityPrescription();

        deleter = (LinearLayout) findViewById(R.id.AddMedLayout);
        test_deleter = (LinearLayout) findViewById(R.id.tests);


//        SpinnerHowManyTimesADay= getResources().getStringArray(R.array.banglaSuggestions);
        shortcutsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, replacements);
//        frequencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SpinnerHowManyTimesADay);
        context = this;
        symptom_deleter = (LinearLayout) findViewById(R.id.symptoms);
        testParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        testBulletParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
//        testParams.setMargins(0, 70, 0, 0);
//        testBulletParams.setMargins(0, 60, 0, 0);
        paramsForTimeEditText = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        paramsForTimeTextView = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);


        drugParamsMax = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20f);
        drugParamsMedium = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5f);
        drugParamsMin = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        drugParamsMin.setMargins(0, 40, 0, 0);
        drugParamsMax.setMargins(0, 30, 0, 0);
        drugParamsMedium.setMargins(0, 60, 0, 0);

        doseQuantityParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f);
        doseUnitParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        doseTextViewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        frequencyParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        suggestionParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        doseUnitParams.setMargins(0, 18, 0, 0);
        doseQuantityParams.setMargins(0, 18, 0, 0);
        frequencyParams.setMargins(0, 18, 0, 0);
        doseTextViewParams.setMargins(0, 18, 0, 0);
        drugReqViewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        drugReqViewParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        drugReqViewParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        textViewMargin.setMargins(10,0,0,0);
//        deleteButtonParams= new LinearLayout.LayoutParams(50,50);
//        deleteButtonParams.setMargins(0,30,0,0);
//        drugReqViewParams2.setMargins(0,40,0,0);
//        drugReqViewParams1.setMargins(0,20,0,0);
        emergency.setThreshold(1);
        emergency.setAdapter(emergenyAdviceAdapter);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (printActivty) {
            reset();
            printActivty = false;
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
            } else if (i == 3 && !temp[3].isEmpty()) advices.setText(temp[3]);
            else if (i == 4 && !temp[4].isEmpty()) emergency.setText(temp[4]);
        }

    }





    public void add_symptoms(View v) {


        //deleting symptoms is not properly defined

        LinearLayout symptom_container = new LinearLayout(this);


        //adding bullets
        ImageView bullets = new ImageView(this);
        bullets.setImageResource(R.drawable.bullet_tab);
        symptom_container.addView(bullets, testBulletParams);

        //adding symptoms
        AutoCompleteTextView symptom = new AutoCompleteTextView(this);
        symptom.setAdapter(symptomsAdapter);
        symptom.setTextSize(20);
        symptom.setThreshold(1);
        symptom.requestFocus();
        int temp;
        symptom.setId(temp = idGenerator.generateViewId());
        symptomIdArray.add(temp);
        symptom_container.addView(symptom, testParams);
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


    private void add_symptoms(String[] symptomString) {

        for (int i = 0; i < symptomString.length; i++) {
            //deleting symptoms is not properly defined

            LinearLayout symptom_container = new LinearLayout(this);


            //adding bullets
            ImageView bullets = new ImageView(this);
            bullets.setImageResource(R.drawable.bullet_tab);
            symptom_container.addView(bullets, testBulletParams);

            //adding symptoms
            AutoCompleteTextView symptom = new AutoCompleteTextView(this);
            symptom.setAdapter(symptomsAdapter);
            symptom.setText(symptomString[i]);
            symptom.setTextSize(20);
            symptom.setThreshold(1);
//            symptom.requestFocus();
            int temp;
            symptom.setId(temp = idGenerator.generateViewId());
            symptomIdArray.add(temp);
            symptom_container.addView(symptom, testParams);
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


    public void add_test(String[] testStrings) {

        for (int i = 0; i < testStrings.length; i++) {
            LinearLayout test_container = new LinearLayout(this);


            //adding bullets
            ImageView bullets = new ImageView(this);
            bullets.setImageResource(R.drawable.bullet_tab);
            test_container.addView(bullets, testBulletParams);

            //adding tests
            AutoCompleteTextView test = new AutoCompleteTextView(this);
            int temp;
            test.setId(temp = idGenerator.generateViewId());
            investigationIdArray.add(temp);
            test.setText(testStrings[i]);
            test.setAdapter(testAdapter);
            test.setThreshold(1);
            test.setTextSize(25);
//            test.requestFocus();
            test_container.addView(test, testParams);


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
        test_container.addView(bullets, testBulletParams);

        //adding tests
        AutoCompleteTextView test = new AutoCompleteTextView(this);
        int temp;
        test.setId(temp = idGenerator.generateViewId());
        investigationIdArray.add(temp);
        test.setAdapter(testAdapter);
        test.setThreshold(1);
        test.setTextSize(25);
        test.requestFocus();
        test_container.addView(test, testParams);


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


        final Spinner mSpinner = new Spinner(context);
        //defining drug text box
        int temp;
        final AutoCompleteTextView new_drug = new AutoCompleteTextView(this);
        new_drug.setAdapter(adapter);
        new_drug.setId(temp = idGenerator.generateViewId());
        medicationIdArray.add(temp);
        new_drug.setThreshold(1);
        new_drug.setTextSize(20);
        new_drug.setHint("Type in the drugs here....");


        mSpinner.setAdapter(shortcutsAdapter);
        mSpinner.setSelection(replacements.length - 1);
        mSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                mSpinner.setSelection(replacements.length - 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        final Spinner quantSpinner = new Spinner(context);
        quantSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", " ১", " ২", " ৩", " আধা"}));
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

        final Spinner mediumSpinner = new Spinner(context);

        mediumSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "টা ", "চামচ ", "এম্পুল ", "ml "}));
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


        final Spinner hourSpinner = new Spinner(context);
        hourSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "৬ ঘন্টা অন্তর ", "৮ ঘন্টা অন্তর ", "১২ ঘন্টা অন্তর ", "২৪ ঘন্টা অন্তর "}));
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

        final Spinner daySpinner = new Spinner(context);
        daySpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "৭ দিন ", "১৫ দিন ", "৩০ দিন "}));
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

                if (!toBeEdited.isEmpty() && loadedMedication.contains(toBeEdited)) {
                    Log.e(this.toString(), toBeEdited);
                    loadedMedication.remove(toBeEdited);
                    Toast.makeText(context, "Deletion successful", Toast.LENGTH_LONG).show();

                    try {
                        FileOutputStream fos = openFileOutput(FILENAME_MEDICATIONS, context.MODE_PRIVATE);

                        for (i = 0; i < loadedMedication.size(); i++) {

                            fos.write((loadedMedication.get(i) + "\n").getBytes());
                        }

                        fos.close();


                        adapter = new ArrayAdapter<String>(context, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
                        new_drug.setText("");
                        new_drug.setAdapter(adapter);
                    } catch (Exception e) {

                    }


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
        layout.addView(drugReq);
        new_drug.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    layout.addView(drugReqPart2);
                    layout.addView(mSpinner);

                } else {
                    layout.removeView(mSpinner);
                    layout.removeView(drugReqPart2);
                }


            }
        });

        new_drug.requestFocus();

        deleter.addView(layout);

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

            final Spinner mSpinner = new Spinner(context);
            //defining drug text box
            final AutoCompleteTextView new_drug = new AutoCompleteTextView(this);
            new_drug.setAdapter(adapter);
            new_drug.setId(temp = idGenerator.generateViewId());
            medicationIdArray.add(temp);
            new_drug.setThreshold(1);
            new_drug.setText(drugStrings[i]);
            new_drug.setTextSize(20);
            new_drug.setHint("Type in the drugs here....");


            mSpinner.setAdapter(shortcutsAdapter);
            mSpinner.setSelection(replacements.length - 1);
            mSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0));


            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    new_drug.getText().insert(new_drug.getSelectionStart(), parent.getSelectedItem().toString());
                    mSpinner.setSelection(replacements.length - 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });


            final Spinner quantSpinner = new Spinner(context);
            quantSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", " ১", " ২", " ৩", " আধা"}));
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

            final Spinner mediumSpinner = new Spinner(context);
            mediumSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "টা ", "চামচ ", "এম্পুল ", "ml"}));
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


            final Spinner hourSpinner = new Spinner(context);
            hourSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "৬ ঘন্টা অন্তর ", "৮ ঘন্টা অন্তর ", "১২ ঘন্টা অন্তর ", "২৪ ঘন্টা অন্তর "}));
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

            final Spinner daySpinner = new Spinner(context);
            daySpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new String[]{"", "৭ দিন ", "১৫ দিন ", "৩০ দিন "}));
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

                    if (!toBeEdited.isEmpty() && loadedMedication.contains(toBeEdited)) {
                        Log.e(this.toString(), toBeEdited);
                        loadedMedication.remove(toBeEdited);
                        Toast.makeText(context, "Deletion successful", Toast.LENGTH_LONG).show();
                        i = 0;
                        try {
                            FileOutputStream fos = openFileOutput(FILENAME_MEDICATIONS, context.MODE_PRIVATE);

                            for (i = 0; i < loadedMedication.size(); i++) {

                                fos.write((loadedMedication.get(i) + "\n").getBytes());
                            }

                            fos.close();


                            adapter = new ArrayAdapter<String>(context, R.layout.custom_item, R.id.autoCompleteItem, loadedMedication);
                            new_drug.setText("");
                            new_drug.setAdapter(adapter);
                        } catch (Exception e) {

                        }


                    } else if (!toBeEdited.isEmpty())
                        Toast.makeText(context, "Deletion failed -> App's drug or Unsaved", Toast.LENGTH_LONG).show();
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

            layout.addView(drugReq);
//            layout.addView(drugReqPart2);
//            layout.addView(mSpinner);

            new_drug.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus) {

                        layout.addView(drugReqPart2);
                        layout.addView(mSpinner);

                    } else {
                        layout.removeView(mSpinner);
                        layout.removeView(drugReqPart2);
                    }


                }
            });


//        layout.addView(drugSuggestion,suggestionParams);
//            layout.addView(editButton, 150, 150);
//


            deleter.addView(layout);


        }
    }


    public void print(View view) {

        if (diagnosises.getText().toString().isEmpty()) {

            Toast.makeText(context, "Fill the shortcut name", Toast.LENGTH_SHORT).show();
            return;

        }
        AutoCompleteTextView symptoms, medications, investigations;

        String prescription = "";


        String emergencyAdvices = emergency.getText().toString();


        advice = advices.getText().toString();
        diagnosis = diagnosises.getText().toString();


        int i;


        if (!loadedAdvice.contains(advice)) {
            loadedAdvice.add(advice);
            adviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedAdvice);
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


        if (!loadedEmergencyAdvice.contains(emergencyAdvices)) {
            loadedEmergencyAdvice.add(emergencyAdvices);
            emergenyAdviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, loadedEmergencyAdvice);
            emergency.setAdapter(emergenyAdviceAdapter);
            emergencyAdvices = emergencyAdvices + "\n";

            try {


                FileOutputStream fos = openFileOutput(FILENAME_EMERGENGYADVICE, context.MODE_APPEND);

                fos.write(emergencyAdvices.getBytes());
                fos.close();
                Toast.makeText(this, "File  saved", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

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


            if (loadedMedication.contains(medication) && medication != null) continue;
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


        prescription += advices.getText().toString() + "$$" + emergency.getText().toString() + "$$" + diagnosises.getText().toString() +
                "!@%&!!@@##%%###%\n";


        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.SAVED_PRES", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("android.ztech.com.prescription.SAVED_PRES",
                sharedPreferences.getString("android.ztech.com.prescription.SAVED_PRES", "") + prescription);
        editor.commit();


//        prescription=names.getText().toString()+"$$"+patientcell.getText().toString()
//                +"$$"+years.getText().toString()+"$"+months.getText().toString()+"$"+
//                days.getText().toString()+"$$"+weight.getText().toString()+prescription;
//


        printActivty = true;



    }


    private void reset() {


        this.finish();
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);


    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("savingItems Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}







