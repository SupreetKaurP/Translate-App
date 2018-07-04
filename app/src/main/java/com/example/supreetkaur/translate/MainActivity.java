package com.example.supreetkaur.translate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.supreetkaur.translate.TranslatorBackgroundTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    //Set context
    Context context=this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Languages languages = new Languages();

       // setSupportActionBar(mTopToolbar);
      //  HashMap<String,String> Lang = languages.map;




        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();


        if (activeNetworkInfo == null  ){  // no internet

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Unable to proceed without Internet Connectivity!");
            builder1.setCancelable(true);


            builder1.setNegativeButton(
                    "Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);


                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();





        }




        String value = Languages.getmap().get("English").toString();
        Log.e("value", value);
        final Spinner spinner1 = findViewById(R.id.spinner1);
        final Spinner spinner2 = findViewById(R.id.spinner2);
//  Languages.put("Azerbaijan" ,"");

        //Default variables for translation
        String textToBeTranslated = "Bienvenue";
        String languagePair = "fr-en"; //English to French ("<source_language>-<target_language>")
        //Executing the translation function
      //  Translate(textToBeTranslated,languagePair);



        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String text1 = spinner1.getSelectedItem().toString();
                String text2 = spinner2.getSelectedItem().toString();

                String value = Languages.getmap().get(text1).toString();
                Log.e("value", value);

                String languagePair = Languages.getmap().get(text1).toString() + "-" + Languages.getmap().get(text2).toString(); //English to French ("<source_language>-<target_language>")
                //Executing the translation function

                EditText txtname = (EditText)findViewById(R.id.textbox1);
                String textToBeTranslated    =  txtname.getText().toString();

                if (textToBeTranslated != null && !textToBeTranslated.isEmpty()) {

                    String T = Translate(textToBeTranslated, languagePair).replace("\\n", "\n");

                    EditText editText = (EditText) findViewById(R.id.textbox2);
                    editText.setSingleLine(false);
                    editText.setText(T, TextView.BufferType.EDITABLE);
                }

else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please Type Something!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });



    }

    //Function for calling executing the Translator Background Task
    String Translate(String textToBeTranslated,String languagePair){
        TranslatorBackgroundTask translatorBackgroundTask= new TranslatorBackgroundTask(context);
        String translationResult = null; // Returns the translated text as a String
      String  resultString = null;
        try {
            translationResult = translatorBackgroundTask.execute(textToBeTranslated,languagePair).get();
           resultString = translationResult.toString().trim();
            //Getting the characters between [ and ]
            resultString = resultString.substring(resultString.indexOf('[')+1);
            resultString = resultString.substring(0,resultString.indexOf("]"));
            //Getting the characters between " and "
            resultString = resultString.substring(resultString.indexOf("\"")+1);
            resultString = resultString.substring(0,resultString.indexOf("\""));
            Log.d("Translation Result", resultString); // Logs the result in Android Monitor
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       // Log.d("Translation Result", ); // Logs the result in Android Monitor
        return resultString;
    }

}