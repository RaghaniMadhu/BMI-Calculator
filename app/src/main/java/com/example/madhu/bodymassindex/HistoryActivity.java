package com.example.madhu.bodymassindex;

import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    TextView tvDatabaseName;
    ListView lvRecords;
    static MeraDbHandler db;
    TextToSpeech tts;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvDatabaseName=(TextView)findViewById(R.id.tvDatabaseName);
        lvRecords=(ListView)findViewById(R.id.lvRecords);
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
        tvDatabaseName.setText("User:"+name);

        db=new MeraDbHandler(this);

        String bmiRecords[]=db.viewHistory();
        if(bmiRecords.length==0)
        {
            bmiRecords = new  String[1];
            bmiRecords[0]="No Records To Show";
            tts.speak("No Records To Show",TextToSpeech.QUEUE_ADD,null);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bmiRecords);

        lvRecords.setAdapter(adapter);
    }
}
