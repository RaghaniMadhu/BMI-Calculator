package com.example.madhu.bodymassindex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvHeading;
    EditText etName, etAge, etPhone;
    RadioGroup rgGender;
    Button btnRegister;
    SharedPreferences sp;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHeading = (TextView) findViewById(R.id.tvHeading);
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etPhone = (EditText) findViewById(R.id.etPhone);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        sp = getSharedPreferences("p1", MODE_PRIVATE);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });

        String name = sp.getString("name", "");
        /*trying to get the name from sp file if it would have been saved
        we will get it
        and if not saved
        we will get default value=""*/

        if (name.length() != 0)//means this is not first time of running app
        {
            Intent i = new Intent(MainActivity.this, WelcomeAPersonActivity.class);
            startActivity(i);
            finish();

        } else//this is the first time take the details from the user
        {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = etName.getText().toString();
                    String age = etAge.getText().toString();
                    String phone = etPhone.getText().toString();
                    String gender = "";

                    if (name.length() == 0) {
                        tts.speak("Invalid Name",TextToSpeech.QUEUE_ADD,null);
                        Toast.makeText(MainActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show();
                        etName.requestFocus();
                        return;
                    }

                    if(age.length()==0)
                    {
                        tts.speak("Invalid age",TextToSpeech.QUEUE_ADD,null);
                        etAge.setError("Invalid age");
                        etAge.requestFocus();
                        return;
                    }

                    char[] nameArray = name.toLowerCase().toCharArray();
                    for (char temp : nameArray) {
                        if ((('a' <= temp) && (temp <= 'z')) || (temp == ' '))
                            continue;
                        else {
                            tts.speak("Invalid Name",TextToSpeech.QUEUE_ADD,null);
                            Toast.makeText(MainActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show();
                            etName.requestFocus();
                            return;
                        }
                    }

                    if (Integer.parseInt(age) <= 0) {
                        tts.speak("Age is Invalid",TextToSpeech.QUEUE_ADD,null);
                        etAge.setError("Invalid age");
                        etAge.requestFocus();
                        return;
                    }
                    if (phone.length() != 10) {
                        tts.speak("Invalid Phone Number", TextToSpeech.QUEUE_ADD, null);
                        etPhone.setError("Invalid Phone Number");
                        etPhone.requestFocus();
                        return;
                    }
                    /*char []phoneCharCheck=phone.toCharArray();
                    for(char c:phoneCharCheck)
                    {
                        if(!Character.isDigit(c))
                        {
                            etPhone.setError("Invalid Phone Number");
                            etPhone.requestFocus();
                            return;
                        }
                    }
                    try{
                        long phoneNo=Long.parseLong(phone);
                    }catch (Exception e)
                    {
                        etPhone.setError("Invalid Phone Number");
                        etPhone.requestFocus();
                        return;
                    }*/
                    int id = rgGender.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) rgGender.findViewById(id);
                    gender = rb.getText().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", name);
                    editor.putString("age", age);
                    editor.putString("phone", phone);
                    editor.putString("gender", gender);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Your Details Saved!!", Toast.LENGTH_SHORT).show();
                    tts.speak("Your Details are Saved!!", TextToSpeech.QUEUE_ADD, null);

                    Intent i = new Intent(MainActivity.this, WelcomeAPersonActivity.class);
                    startActivity(i);
                    finish();

                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do You Really Want To Exit?");
        tts.speak("Do Youu Really Want To Exit?",TextToSpeech.QUEUE_ADD,null);

        a.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog b = a.create();
        b.setTitle("Exit");
        b.show();
    }
}
