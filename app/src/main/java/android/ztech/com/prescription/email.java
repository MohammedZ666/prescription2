package android.ztech.com.prescription;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class email extends AppCompatActivity {
    private StorageReference mStorageRef;
    String FILENAME_MEDICATIONS = "android.ztech.com.prescription.medications_container_1";
    String FILENAME_DIAGNOSIS = "android.ztech.com.prescription.diagnosis_container_1";
    List<String> loadedDiagnosis = new ArrayList<String>();
    ProgressBar pg;
    private static final String TAG = "save 2 firebase storage";
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private String userMail="";
    private FirebaseAuth mAuth;


    public email()
    {


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_email);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        pg = (ProgressBar) findViewById(R.id.progressBar);
        pg.setVisibility(View.GONE);


        //if(mAuth.getCurrentUser()!=null) {

        if( mAuth.getCurrentUser()!=null){
            String uploadCode = getIntent().getExtras().getString("calling from OpAct");
            String downloadCode = getIntent().getExtras().getString("get previous backup");
            //userMail = mAuth.getCurrentUser().getEmail();
            //userMail = GoogleSignIn.getLastSignedInAccount(email.this).getEmail();
            userMail = "sufianfcps@gmail.com";
            //Toast.makeText(this,userMail,Toast.LENGTH_SHORT).show();
            if (uploadCode!=null) emailDrugs();

         else if(downloadCode!=null)  saveFromMail();


            email.this.finish();

             }

     signIn();

    }


    /** Start sign in activity. */
    private void signIn() {
        Log.i(TAG, "Start sign in");



            GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
            startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);





    }

    /** Build a Google SignIn client. */
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("539433558762-2puqgp6u4u176hoie8qirjtlvf9bquhh.apps.googleusercontent.com")
                        .requestProfile()
                        .requestEmail()
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Signed in successfully.");
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                       // firebaseAuthWithGoogle(account.getIdToken());
                        userMail = GoogleSignIn.getLastSignedInAccount(email.this).getEmail();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("email",userMail);
                        setResult(5,returnIntent);

                        if(getIntent().getExtras()!=null) {

                            String uploadCode = getIntent().getExtras().getString("calling from OpAct");
                            String downloadCode = getIntent().getExtras().getString("get previous backup");
                            if (uploadCode!=null) emailDrugs();
                            else if(downloadCode!=null)  saveFromMail();
                            email.this.finish();

                        }
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Google sign in failed", e);
                        // ...
                    }


                }




                else {
                    Toast.makeText(email.this, "Sign In Failed, Check Internet and Try Again" + resultCode ,Toast.LENGTH_LONG).show();
                    email.this.finish();
                }
                break;
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            userMail = GoogleSignIn.getLastSignedInAccount(email.this).getEmail();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("email",userMail);
                            setResult(5,returnIntent);

                            if(getIntent().getExtras()!=null) {

                                String uploadCode = getIntent().getExtras().getString("calling from OpAct");
                                String downloadCode = getIntent().getExtras().getString("get previous backup");
                                if (uploadCode!=null) emailDrugs();
                                else if(downloadCode!=null)  saveFromMail();
                                email.this.finish();

                            }

                            //        FirebaseUser user = mAuth.getCurrentUser();
                     //       updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           Toast.makeText(email.this,"sign in failed",Toast.LENGTH_SHORT);
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void emailDrugs()
    {
        if(GoogleSignIn.getLastSignedInAccount(email.this)!=null) {

            String loadedMedandAutoPres = "";


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

                FileInputStream fis = this.openFileInput(FILENAME_MEDICATIONS);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);


                String loadedStringTemp;


                while ((loadedStringTemp = br.readLine()) != null) {


                    loadedMedandAutoPres = loadedMedandAutoPres + loadedStringTemp + "#6#";


                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            loadedMedandAutoPres += "*00*";

            for (int i = 0; i < loadedDiagnosis.size(); i++) {
                SharedPreferences sharedPreferences = getSharedPreferences
                        ("android.ztech.com.prescription." + loadedDiagnosis.get(i), this.MODE_PRIVATE);

            if(!loadedDiagnosis.get(i).isEmpty()&&!sharedPreferences.
                    getString("android.ztech.com.prescription." +
                            loadedDiagnosis.get(i), "").isEmpty()) {
                loadedMedandAutoPres += loadedDiagnosis.get(i) + "||" + sharedPreferences
                        .getString("android.ztech.com.prescription." + loadedDiagnosis.get(i), "") + "#6#";
            }


            }


            StorageReference storageReference = mStorageRef.child(userMail + "/app data");

        if(loadedMedandAutoPres.getBytes().length>4) {
            UploadTask uploadTask = storageReference.putBytes((loadedMedandAutoPres.getBytes()));
            Toast.makeText(getApplicationContext(), "Auto backup started, please wait...", Toast.LENGTH_SHORT).show();

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    exception.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Upload failed, check internet", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...

                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();


                }
            });


        }

        }


    }

    public void emailDrugs(View v)
    {
        if(GoogleSignIn.getLastSignedInAccount(email.this)!=null) {

            String loadedMedandAutoPres = "";


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

                FileInputStream fis = this.openFileInput(FILENAME_MEDICATIONS);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);


                String loadedStringTemp;


                while ((loadedStringTemp = br.readLine()) != null) {


                    loadedMedandAutoPres = loadedMedandAutoPres + loadedStringTemp + "#6#";


                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            loadedMedandAutoPres += "*00*";

            for (int i = 0; i < loadedDiagnosis.size(); i++) {
                SharedPreferences sharedPreferences = getSharedPreferences
                        ("android.ztech.com.prescription." + loadedDiagnosis.get(i), this.MODE_PRIVATE);

                if(!loadedDiagnosis.get(i).isEmpty()&&!sharedPreferences.
                        getString("android.ztech.com.prescription." +
                                loadedDiagnosis.get(i), "").isEmpty()) {
                    loadedMedandAutoPres += loadedDiagnosis.get(i) + "||" + sharedPreferences
                            .getString("android.ztech.com.prescription." + loadedDiagnosis.get(i), "") + "#6#";
                }


            }


            StorageReference storageReference = mStorageRef.child(userMail + "/app data");

            if(loadedMedandAutoPres.getBytes().length>4) {
                UploadTask uploadTask = storageReference.putBytes((loadedMedandAutoPres.getBytes()));
                Toast.makeText(getApplicationContext(), "Auto backup started, please wait...", Toast.LENGTH_SHORT).show();

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads

                        Toast.makeText(getApplicationContext(), "Upload failed, check internet", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...

                        Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();


                    }
                });
            }

        }

    }


    private void saveFromMail()
    {

        long FIVE_GIGABYTE = 1024*1024*1024;
        FIVE_GIGABYTE*=5;

        StorageReference storageReference = mStorageRef.child(userMail+"/app data");

        Toast.makeText(email.this, "Getting previous data", Toast.LENGTH_SHORT).show();


        storageReference.getBytes(FIVE_GIGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                setData(new String(bytes));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(email.this, "Data not found", Toast.LENGTH_LONG).show();
            }
        });


        SharedPreferences sharedPreferences1 = getSharedPreferences("backUpBoolean",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putBoolean("notBackedUp", false);
        editor.commit();

    }

    public void saveFromMail(View v) {

        long FIVE_GIGABYTE = 1024*1024*1024;
        FIVE_GIGABYTE*=5;

        StorageReference storageReference = mStorageRef.child(userMail+"/app data");

        Toast.makeText(email.this, "Getting previous data", Toast.LENGTH_SHORT).show();


        storageReference.getBytes(FIVE_GIGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                setData(new String(bytes));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(email.this, "Data not found", Toast.LENGTH_LONG).show();
            }
        });




        }




private void setData(String globalForDownload)
{
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








