package android.ztech.com.prescription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveAddress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_address);
        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.PHYSICIAN_ADDRESS", MODE_PRIVATE);
        String [] prevAddress = sharedPreferences.getString("address", "কনসালটেন্ট ডায়াগনস্টিক সেন্টার $$পুরাতন পাসপোর্ট ভবন$$সদর হাসপাতাল সংলগ্ন, হবিগঞ্জ।$$সময়ঃ বিকাল ৩টা - ৭টা ").split("\\$\\$");
        EditText [] addressLines = {(EditText) findViewById(R.id.addressLine1), (EditText) findViewById(R.id.addressLine2),(EditText) findViewById(R.id.addressLine3), (EditText) findViewById(R.id.timeLine)};
        for (int i=0; i<addressLines.length; i++){
           addressLines[i].setText(prevAddress[i]);
        }
    }

    public void saveAddress(View view){
        EditText [] addressLines = {(EditText) findViewById(R.id.addressLine1), (EditText) findViewById(R.id.addressLine2),(EditText) findViewById(R.id.addressLine3), (EditText) findViewById(R.id.timeLine)};
        StringBuilder address = new StringBuilder();
        for (int i=0; i<addressLines.length; i++){
            if(addressLines[i].getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please ensure no fields are empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(i<addressLines.length-1) address.append(addressLines[i].getText().toString()).append("$$");
            else address.append(addressLines[i].getText().toString());
        }
        SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.PHYSICIAN_ADDRESS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", address.toString());
        editor.commit();

        Toast.makeText(getApplicationContext(), "Address Saved Successfully", Toast.LENGTH_SHORT).show();
    }
}