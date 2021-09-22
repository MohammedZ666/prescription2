package android.ztech.com.prescription;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String addressRelative = sharedPref.getString("addressRelative", "");
        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(imagePath, "/doc" + addressRelative + ".pdf");
        Uri contentUri = FileProvider.getUriForFile(this, "android.ztech.com.prescription", newFile);


        if (contentUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent = Intent.createChooser(shareIntent, "Share your prescription");


            try {
                startActivity(shareIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
            }
        }
   }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}