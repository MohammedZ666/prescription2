package android.ztech.com.prescription;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * Created by Zishan on 07/Mar/2017.
 */

public class CustomAutoComplete extends AppCompatAutoCompleteTextView {

    public CustomAutoComplete(Context context) {
        super(context);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void replaceText(CharSequence text) {

            String[] tempArray = text.toString().split("\nG.name:");
            super.replaceText(tempArray[0]);

    }
}
