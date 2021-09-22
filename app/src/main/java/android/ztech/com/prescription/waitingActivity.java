package android.ztech.com.prescription;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class waitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.waiting_hack_layout);

    }


    @Override
    public void onBackPressed()
    {

    }
}
