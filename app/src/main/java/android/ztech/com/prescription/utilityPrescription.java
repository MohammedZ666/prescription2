package android.ztech.com.prescription;
import android.print.PrintDocumentAdapter;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * Created by Zishan on 7/19/2016.
 */
public class utilityPrescription {
    protected utilityPrescription(){


    }



    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

   // creating a function to produce unique ID's for the autocompletes.For more info just google.
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }





}
