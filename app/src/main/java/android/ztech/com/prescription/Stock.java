package android.ztech.com.prescription;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Stock extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {



        String FILENAME_INVESTIGATION = "2.1.android.ztech.com.prescription.name_container_new.manageItems.";

        List<Integer> investigationIdArray = new ArrayList<>();

        LinearLayout test_deleter;

        List<String> loadedInvestigation = new ArrayList<String>();


        utilityPrescription idGenerator;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        test_deleter = new LinearLayout(this);
try {
    test_deleter = (LinearLayout) findViewById(R.id.stock_info);
}
catch (Exception e)
{

    e.printStackTrace();

}

        idGenerator = new utilityPrescription();



        TextView totalPriceOfAll = (TextView) findViewById(R.id.total_cost);
        float totalPrice=0;


        LinearLayout.LayoutParams defaultParams= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        LinearLayout.LayoutParams specialParams= new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

        int temp=0;
        int temp1=0;





            SharedPreferences sharedPreferences = getSharedPreferences("android.ztech.com.prescription.SAVED_PRES", this.MODE_PRIVATE);
            String theUserDrugs=sharedPreferences.getString("android.ztech.com.prescription.SAVED_PRES","");
            if(theUserDrugs.isEmpty())return;
//            \.[]{}()*+-?^$| these are the characters to be escaped
            loadedInvestigation.addAll(Arrays.asList(theUserDrugs.split("!@%&!!@@##%%###%\n")));




            for(int j=0;j<loadedInvestigation.size();j++){

            String[] retval  = loadedInvestigation.get(j).split("\\$\\$");
                TextView[] textViews= new TextView[7];

                LinearLayout test_container = new LinearLayout(this);
                test_container.setOrientation(LinearLayout.HORIZONTAL);
                test_deleter.addView(test_container,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                for(int i=0;i<=6;i++)
                {

                    if(i==0) {

                        textViews[i] = new TextView(this);
                        textViews[i].setId(temp1 = idGenerator.generateViewId());
                        textViews[i].setBackgroundResource(R.drawable.box_border);
                        textViews[i].setText(retval[retval.length-1]);
                        textViews[i].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2f));
                        investigationIdArray.add(temp1);


                    }
                    else {
                        textViews[i] = new TextView(this);
                        textViews[i].setBackgroundResource(R.drawable.box_border);
                        textViews[i].setId(idGenerator.generateViewId());
//                        textViews[i].setText();
                        textViews[i].setLayoutParams(defaultParams);



                    }



                test_container.addView(textViews[i]);

                    if(i!=6) {

//                        if (retval[i].length() > retval[i + 1].length()) temp = i;
//                        else if (retval[i].length() < retval[i + 1].length()) temp = i + 1;

                    }
                }





                if(temp==0) textViews[temp].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
                else  textViews[temp].setLayoutParams(specialParams);





//                totalPrice+=Float.parseFloat(retval[1])*Float.parseFloat(retval[4]);




//                int temp;
//
//                TextView nameTextView = new TextView(this);
//                nameTextView.setId(temp = idGenerator.generateViewId());
//                nameTextView.setBackgroundResource(R.drawable.box_border);
//                nameTextView.setText(retval[0]);
//                investigationIdArray.add(temp);
//
//                TextView priceTextView = new TextView(this);
//                priceTextView.setBackgroundResource(R.drawable.box_border);
//                priceTextView.setId(idGenerator.generateViewId());
//                priceTextView.setText(retval[1]);
//
//
//
//
//                TextView qtnPutIntoStockTextView = new TextView(this);
//                qtnPutIntoStockTextView.setBackgroundResource(R.drawable.box_border);
//                qtnPutIntoStockTextView.setId(idGenerator.generateViewId());
//                qtnPutIntoStockTextView.setText(retval[2]);
//
//                TextView qtnSoldTextView = new TextView(this);
//                qtnSoldTextView.setBackgroundResource(R.drawable.box_border);
//                qtnSoldTextView.setId(idGenerator.generateViewId());
//                qtnSoldTextView.setText(retval[3]);
//
//                TextView qtnRemainingTextView = new TextView(this);
//                qtnRemainingTextView.setBackgroundResource(R.drawable.box_border);
//                qtnRemainingTextView.setId(idGenerator.generateViewId());
//                qtnRemainingTextView.setText(retval[4]);
//                totalPrice+=Float.parseFloat(retval[1])*Float.parseFloat(retval[4]);
//
//
//                TextView dateOfPuttingIntoStockTextView = new TextView(this);
//                dateOfPuttingIntoStockTextView.setBackgroundResource(R.drawable.box_border);
//                dateOfPuttingIntoStockTextView.setId(idGenerator.generateViewId());
//                dateOfPuttingIntoStockTextView.setText(retval[5]);
//
//                TextView timeOfPuttingIntoStockTextView = new TextView(this);
//                timeOfPuttingIntoStockTextView.setBackgroundResource(R.drawable.box_border);
//                timeOfPuttingIntoStockTextView.setId(idGenerator.generateViewId());
//                timeOfPuttingIntoStockTextView.setText(retval[6]);
//
//
////                0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f
//
//                String tempOfretval="";
//                int tempIntOfretval;
//                for(int i=0;i<=6;i++)
//                {
//
//                    if(tempOfretval.length()<retval[i].length())
//                    {
//                        tempOfretval=retval[i];
//                        tempIntOfretval=i;
//                    }
//
//                }
//
//
//
//
//
//                test_container.addView(nameTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
//                test_container.addView(priceTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                test_container.addView(qtnPutIntoStockTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                test_container.addView(qtnRemainingTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                test_container.addView(qtnSoldTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                test_container.addView(dateOfPuttingIntoStockTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
//                test_container.addView(timeOfPuttingIntoStockTextView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));




            }






//    totalPriceOfAll.setText(Float.toString(totalPrice));




    }





    public void addItem(View view)
    {


        Intent intent = new Intent();
        intent.setClass(Stock.this,savingItems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);





    }






}
