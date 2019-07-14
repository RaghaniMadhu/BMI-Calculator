package com.example.madhu.bodymassindex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    TextView tvYourBMI,tvBMIValue1,tvBMIValue2,tvBMIValue3,tvBMIValue4;
    Button btnSave;
    FloatingActionButton fabBack,fabShare;
    TextToSpeech tts;
    static MeraDbHandler db;
    SharedPreferences sp;
    int noOfChancesToSave=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvYourBMI=(TextView)findViewById(R.id.tvYourBMI);

        tvBMIValue1=(TextView)findViewById(R.id.tvBMIValue1);
        tvBMIValue2=(TextView)findViewById(R.id.tvBMIValue2);
        tvBMIValue3=(TextView)findViewById(R.id.tvBMIValue3);
        tvBMIValue4=(TextView)findViewById(R.id.tvBMIValue4);

        fabBack=(FloatingActionButton)findViewById(R.id.fabBack);
        fabShare=(FloatingActionButton)findViewById(R.id.fabShare);

        btnSave=(Button)findViewById(R.id.btnSave);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });


        db = new MeraDbHandler(this);

        tvBMIValue1.setText("Below 18.5 is Underweight");
        tvBMIValue2.setText("Between 18.5 to 25 is Normal");
        tvBMIValue3.setText("Between 25 to 30 is Overweight");
        tvBMIValue4.setText("More than 30 is Obese");

        String condition="";

        Intent i = getIntent();
        final double BMIValue=i.getDoubleExtra("BMIValue",0.0);

        if(BMIValue<18.5)
        {
            tvBMIValue1.setTextColor(Color.parseColor("#ff0000"));
            condition="UnderWeight";
        }
        else if(BMIValue<25)
        {
            tvBMIValue2.setTextColor(Color.parseColor("#ff0000"));
            condition="Normal";
        }
        else if(BMIValue<30)
        {
            tvBMIValue3.setTextColor(Color.parseColor("#ff0000"));
            condition="Overweight";
        }
        else
        {
            tvBMIValue4.setTextColor(Color.parseColor("#ff0000"));
            condition="Obese";
        }
        String bmiText = "Your BMI value is "+String.format("%.2f",BMIValue)+".\nYou are "+condition+".";
        tvYourBMI.setText(bmiText);
        tts.speak(condition,TextToSpeech.QUEUE_ADD,null);

        sp=getSharedPreferences("p1",MODE_PRIVATE);//p1 is the file name
        //in which key value pairs are stored
        String name ="Name:" + sp.getString("name","")+"\n";//name string is the key which
        // was used in previous activity to store key value pair of name in sp
        // "" is the default value if we dont get anything
        String age ="Age:" + sp.getString("age","")+"\n";
        String phone = "Phone:" + sp.getString("phone","")+"\n";
        String gender = "Gender:" + sp.getString("gender","")+"\n";
        String bmiValue = "Your BMIValue is " +String.format("%.2f",BMIValue)+"\n";
        String uare="You are " + condition+"\n";
        final String sendData=name+age+phone+gender+bmiValue+uare;

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noOfChancesToSave==0)
                {
                    Date d=new Date();

                    HashMap<Integer,String> hashMap=new HashMap<Integer, String>();
                    hashMap.put(0,"January");
                    hashMap.put(1,"February");
                    hashMap.put(2,"March");
                    hashMap.put(3,"April");
                    hashMap.put(4,"May");
                    hashMap.put(5,"June");
                    hashMap.put(6,"July");
                    hashMap.put(7,"August");
                    hashMap.put(8,"September");
                    hashMap.put(9,"October");
                    hashMap.put(10,"November");
                    hashMap.put(11,"December");

                    String date=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
                    String day=d.getDay()
                            +"-"+hashMap.get(d.getMonth())
                            +"-"+d.getMonth();

                    db.addARecord(d.toString(),date,day,BMIValue);
                    noOfChancesToSave++;
                }
                else Toast.makeText(ResultActivity.this, "Already Saved", Toast.LENGTH_SHORT).show();
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,sendData);
                startActivity(i);
            }
        });
    }
}
