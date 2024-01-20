package android.ztech.com.prescription;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


public class openingActivity extends AppCompatActivity implements TrialExpiredLockerDialog.OnPositiveClickListener,PasswordDialog2Second.OnPositiveClickListener{

    String FILENAME_CHECK="android.ztech.com.prescription.ISSIGNEDINONCE";
    String FILENAME_CHECK_TRIAL="android.ztech.com.prescription.ISSIGNEDINONCETRIAL";
    String userID="";
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    boolean hasBought;
    private final static String TAG = "BroadcastService";


    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    public void finishIt()
    {
        super.finish();
    }


    public void uploadStartTime(Long startTime)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Device id" + "/" + userID);

            UploadTask uploadTask = storageReference.putBytes(longToBytes(startTime));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    if(!isOnline()) {
                        Toast.makeText(getApplicationContext(), "Sorry no internet, app will close", Toast.LENGTH_LONG).show();
                    finishIt();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...




                }
            });

    }


    private void setStartTime(Long startTime)
    {
                String FILENAME_START_TIME_SAVED = "android.ztech.com.prescription.START_TIME_SAVED";
                SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_START_TIME_SAVED, this.MODE_PRIVATE);
                boolean startTimeSaved = sharedPreferences.getBoolean(FILENAME_START_TIME_SAVED, false);

                if (!startTimeSaved) {

                    String FILENAME_STARTTIME = "android.ztech.com.prescription.STARTTIME";
                    sharedPreferences = getSharedPreferences(FILENAME_STARTTIME, this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(FILENAME_STARTTIME, startTime);
                    editor.commit();

                    sharedPreferences = getSharedPreferences(FILENAME_START_TIME_SAVED, this.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(FILENAME_START_TIME_SAVED, true);
                    editor.commit();


                }


        startService(new Intent(this, BroadcastService.class));
         Log.e(TAG, "Started service");

        }




private static final int GET_EMAIL = 5;
boolean startTimeSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);


        Activity openingActivity = (Activity) openingActivity.this;
        Context context = this;
//        if (ContextCompat.checkSelfPermission(context,
//                android.Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
//                android.Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(openingActivity,
//                    android.Manifest.permission.SEND_SMS)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(openingActivity,
//                        new String[]{
//                           android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE},
//                        1);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//
//
//
//            }
//        }






        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(openingActivity.this));



        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_CHECK_TRIAL,this.MODE_PRIVATE);
        hasBought=sharedPreferences.getBoolean(FILENAME_CHECK_TRIAL,false);

        String FILENAME_START_TIME_SAVED = "android.ztech.com.prescription.START_TIME_SAVED";
        sharedPreferences = getSharedPreferences(FILENAME_START_TIME_SAVED, this.MODE_PRIVATE);
        startTimeSaved = sharedPreferences.getBoolean(FILENAME_START_TIME_SAVED, false);
        if(startTimeSaved) startService(new Intent(this, BroadcastService.class));
        else{
//            Intent intentImplicit = new Intent(this, email.class);
//            intentImplicit.putExtra("get previous backup", "finish it");
//            startActivityForResult(intentImplicit,GET_EMAIL);
            Runnable someRunnable = new Runnable() {
                @Override
                public void run() {
                    // todo: background tasks
                    Looper.prepare();
                    saveFromMail("sufianfcps@gmail.com");
                    authenticateTheUser("sufianfcps@gmail.com");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // todo: update your ui / view in activity
                        }
                    });

                /*
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // todo: update your ui / view in Fragment
                    }
                });*/
                }
            };

            Executors.newSingleThreadExecutor().execute(someRunnable);


        }


        if (!hasBought) {

//            String FILENAME_TRIALDLG_ONCE = "android.ztech.com.prescription.FILENAME_TRIALDIALOG_ONCE";
//
//
//            sharedPreferences = getSharedPreferences(FILENAME_TRIALDLG_ONCE, this.MODE_PRIVATE);
//
//            boolean trial_dlg_appeared_once = sharedPreferences.getBoolean(FILENAME_TRIALDLG_ONCE, false);
//
//            if (trial_dlg_appeared_once) {
//
//                PasswordDialog2Second passwordDialog2Second = new PasswordDialog2Second();
//                passwordDialog2Second.show(getSupportFragmentManager(), "dialog 3");
//
//                TrialExpiredLockerDialog trialExpiredLockerDialog = new TrialExpiredLockerDialog();
//                trialExpiredLockerDialog.show(getFragmentManager(),"dialog3");

//            }



//            TrialExpiredLockerDialog trialExpiredLockerDialog = new TrialExpiredLockerDialog();
//            trialExpiredLockerDialog.show(getFragmentManager(),"dialog3");




//            SharedPreferences sharedPreferences1 = getSharedPreferences("backUpBoolean",this.MODE_PRIVATE);
//
//            if(sharedPreferences1.getBoolean("notBackedUp",true)) {
//
//                Intent intentImplicit = new Intent(this, email.class);
//                intentImplicit.putExtra("get previous backup", "finish it");
//                startActivityForResult(intentImplicit,GET_EMAIL);
//
//
//            }




        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//
//            //If the draw over permission is not available open the settings screen
//            //to grant the permission.
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
//        }








    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isGranted = false;
        Activity openingActivity = openingActivity.this;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                       )) {
                    isGranted = true;

                }


            }

        }

        if (!isGranted) {

            Toast.makeText(openingActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            openingActivity.finish();
        }



    }


private void authenticateTheUser(final String userMail)
{

    if (!startTimeSaved) {


        if (isOnline()) {

            userID = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);


            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()){
                                    setStartTime(doc.getLong("time"));
                                }

                                else{
                                    Long time = System.currentTimeMillis();
                                    setStartTime(time);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("userID", userID);
                                    user.put("userMail",userMail);
                                    user.put("time",time);

                                    // Add a new document with a generated ID
                                    db.collection("users")
                                            .document(userID)
                                            .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });



//            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Device id" + "/" + userID);
//
//            long FIVE_GIGABYTE = 1024 * 1024 * 1024;
//            FIVE_GIGABYTE *= 5;
//
//
//            final Context ctx = this;
//
//
//
//            storageReference.getBytes(FIVE_GIGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//
//                    setStartTime(bytesToLong(bytes));
//                    startService(new Intent(ctx, BroadcastService.class));
//                    Log.e(TAG, "Started service");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    if (!isOnline()) {
//                        Toast.makeText(ctx, "Sorry no internet, app will close", Toast.LENGTH_LONG).show();
//                        finishIt();
//                    } else {
//
//                            setStartTime(System.currentTimeMillis());
//                        uploadStartTime(System.currentTimeMillis());
//                        startService(new Intent(ctx, BroadcastService.class));
//                        Log.e(TAG, "Started service");
//                    }
//
//                }
//
//            });


        } else {
            Toast.makeText(this, "Sorry no internet, app will close", Toast.LENGTH_LONG).show();
            super.finish();
        }


    }


    else
    {
        startService(new Intent(this, BroadcastService.class));
        Log.e(TAG, "Started service");
    }


    SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_CHECK,this.MODE_PRIVATE);


    boolean filecheck=sharedPreferences.getBoolean(FILENAME_CHECK,false);





    if(!filecheck) {


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FILENAME_CHECK, true);
        editor.commit();


        Intent intent = new Intent(this, TimeTickRegisterer.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pintent);



    }
}












    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e(TAG, "recieved something");

            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };




    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.e(TAG, "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.e(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.e(TAG, "Stopped service");
        super.onDestroy();
    }


    @Override
    public void finish()
    {
        ProgressBar pg = (ProgressBar) findViewById(R.id.progressBarOpening);

        pg.setVisibility(View.VISIBLE);

        Intent intentImplicit= new Intent(this,email.class);
        intentImplicit.putExtra("calling from OpAct","finish it");
        startActivityForResult(intentImplicit,7);

        pg.setVisibility(View.GONE);

        super.finish();


    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==7)
        {
            openingActivity.this.finish();

        }

    else if(requestCode == GET_EMAIL) {
            String userMail = data.getStringExtra("email");
            //Toast.makeText(this,userMail,Toast.LENGTH_SHORT).show();
            authenticateTheUser(userMail);

        }
    else if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (resultCode == Activity.RESULT_OK) {

            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void updateGUI(Intent intent) {
        long secUntilFinished;
        if (intent.getExtras() != null) {
            secUntilFinished = intent.getLongExtra("countdown", 0);

            secUntilFinished/=1000;

            int day = (int)secUntilFinished/(24*60*60);
            int hour = (int)(secUntilFinished/(60*60)-(day*24));
            int minute = (int)((secUntilFinished/60)-(hour*60)-(day*24*60));
            int sec = (int)(secUntilFinished-(minute*60)-(hour*60*60)-(day*24*60*60));


            String time = Integer.toString(day)+"days:"+Integer.toString(hour)+"hours:"+Integer.toString(minute)+
                    "mins:"+Integer.toString(sec)+"secs";



            if(secUntilFinished<1){

                Activity openingActivity = this;
               // Log.e(TAG, "Countdown seconds remaining: yes");

                //Toast.makeText(this, "Your time is up",Toast.LENGTH_LONG).show();
               //super.finish();



//                TrialExpiredLockerDialog trialExpiredLockerDialog = new TrialExpiredLockerDialog();
//                trialExpiredLockerDialog.show(openingActivity.getFragmentManager(),"dialog3");
//
//
//                String FILENAME_TRIALDLG_ONCE= "android.ztech.com.prescription.FILENAME_TRIALDIALOG_ONCE";
//                SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_TRIALDLG_ONCE, this.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean(FILENAME_TRIALDLG_ONCE, true);
//                editor.commit();
            }
           // else  ((TextView)findViewById(R.id.timer)).setText(time);
        }
    }




//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)  {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0&&!hasBought) {
//
//            Activity openingActivity = this;
//
//            onBackSignPressed();
//
//            openingActivity.finish();
//        return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    public void onBackSignPressed() {
//
//
//
//
//        return;
//    }








    public void startActivityMain(View view)
    {
        Intent intentImplicit= new Intent(this,MainActivity.class);
        startActivity(intentImplicit);
    }

    public void startActivitySaveAddress(View view)
    {
        Intent intentImplicit= new Intent(this, SaveAddress.class);
        startActivity(intentImplicit);
    }


    public void startActivitySearchPrescription(View view)
    {
        Intent intentImplicit= new Intent(this,SearchPrescription.class);
        startActivity(intentImplicit);
    }


//
//    public void startActivityExpensePerItem(View view)
//    {
//        Intent intentImplicit= new Intent(this,ExpensePerItem.class);
//        startActivity(intentImplicit);
//    }
//
//
//
//
//    public void startActivityExpense(View view)
//        {
//            Intent intentImplicit= new Intent(this,expense.class);
//            startActivity(intentImplicit);
//        }

    public void entryCheck() {


    }


//    public void entryCheckMain() {
//
//            SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_CHECK, this.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(FILENAME_CHECK, true);
//            editor.commit();
//
//
//        Toast.makeText(openingActivity.this,"Access",Toast.LENGTH_LONG);
//    }


    public void entryCheckTrial(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILENAME_CHECK_TRIAL, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FILENAME_CHECK_TRIAL, true);
        editor.commit();


       /* Intent intent = new Intent(this, TimeTickRegisterer.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);*/

    }




    //    public void startActivityExpenseCalulation(View view)
//    {
//        Intent intentImplicit= new Intent(this,expenseCalculationActivity.class);
//        startActivity(intentImplicit);
//    }
//
//
//
//
//    public void startActivityManageItems(View view)
//    {
//        Intent intentImplicit= new Intent(this,ManageItemsActivity.class);
//        startActivity(intentImplicit);
//    }
//
//
//
//
//
//
//
    public void startActivityEmail(View view)
    {
        Intent intentImplicit= new Intent(this,email.class);
        startActivity(intentImplicit);
    }



    private void saveFromMail(String userMail) {

        long FIVE_GIGABYTE = 1024*1024*1024;
        FIVE_GIGABYTE*=5;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(userMail+"/app data");

        Toast.makeText(this, "Getting previous data", Toast.LENGTH_SHORT).show();


        storageReference.getBytes(FIVE_GIGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                setData(new String(bytes));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_LONG).show();
            }
        });




    }




    private void setData(String globalForDownload)
    {
        String FILENAME_MEDICATIONS = "android.ztech.com.prescription.medications_container_1";
        String FILENAME_DIAGNOSIS = "android.ztech.com.prescription.diagnosis_container_1";
        List<String> loadedDiagnosis = new ArrayList<String>();

        if (!globalForDownload.isEmpty()) {

            String[] temp1;
            String[] drugs;

            temp1 = globalForDownload.split("\\*00\\*");
            drugs = temp1[0].split("#6#");


            for (int i = 0; i < drugs.length ; i++) {

                if (!drugs[i].isEmpty()) {
                    try {
                        drugs[i] = drugs[i] + "\n";
                        FileOutputStream fos = openFileOutput(FILENAME_MEDICATIONS, this.MODE_APPEND);

                        fos.write(drugs[i].getBytes());
                        fos.close();

                    } catch (Exception e) {

                    }
                }

            }

            if(temp1.length==2) {
                loadedDiagnosis = new ArrayList<String>();
                loadedDiagnosis.addAll(Arrays.asList(temp1[1].split("#6#")));

                for (int i = 0; i < loadedDiagnosis.size(); i++)

                {
                    temp1 = loadedDiagnosis.get(i).split("\\|\\|");

                    try {


                        FileOutputStream fos = openFileOutput(FILENAME_DIAGNOSIS, this.MODE_APPEND);

                        fos.write((temp1[0] + "\n").getBytes());
                        fos.close();


                    } catch (Exception e) {
                        Toast.makeText(this, "File could not be saved", Toast.LENGTH_SHORT).show();
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription." + temp1[0], this.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("android.ztech.com.prescription." + temp1[0], temp1[1]);
                    editor.commit();


                }

            }

            Toast.makeText(this, "Previous data saved", Toast.LENGTH_LONG).show();


        }




        else  Toast.makeText(this, "Nothing to save", Toast.LENGTH_LONG).show();


    }







}