package com.example.qr_upi_payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    String payeeName = "Anonymous";
    String transactionNote = "Testing for Deeplinking";

    String currencyUnit = "INR";


        public void btn(View v) {

            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode != 1) {

            if (result != null) {
                if (result.getContents() == null) {
                    Log.e("Scan*******", "Cancelled scan");

                } else {
                    Log.e("Scan", "Scanned");

                    EditText ed = findViewById(R.id.editText);
                    String amount = ed.getText().toString();
                   String Txt1=result.getContents(); int counter=0;
                   String Txt="";
                   for(int i=0;i<Txt1.length();i++)
                   {if(Txt1.charAt(i)=='&')
                       break;

                   if(counter==1)
                   { Txt=Txt+Txt1.charAt(i);
                   }
                       if(Txt1.charAt(i)=='='){counter++;}

                   }

                    if (amount != null) {
                        Uri uri = Uri.parse("upi://pay?pa=" + Txt + "&pn=" + payeeName + "&tn=" + transactionNote +
                                "&am=" + amount + "&cu=" + currencyUnit);

                        Log.d("tag", "onClick: uri: " + uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivityForResult(intent, 1);
Log.i("id",result.getContents());
                        Toast.makeText(this, "Scanned: " + Txt, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.d("tag3", "onActivityResult: requestCode: " + requestCode);
            Log.d("tag4", "onActivityResult: resultCode: " + resultCode);
            //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
            //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

            if (data != null) {
                Log.d("tag2", "onActivityResult: data: " + data.getStringExtra("response"));
                String res = data.getStringExtra("response");
                String search = "SUCCESS";
                if (res.toLowerCase().contains(search.toLowerCase())) {
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
           }
        }
    }
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
    }