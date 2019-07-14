package com.example.madhu.bodymassindex;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Madhu on 03-07-2018.
 */

public class MeraDbHandler extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;

    MeraDbHandler(Context context)
    {
        super(context,"bmivaluedb",null,1);
        this.context=context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="CREATE TABLE bmivaluetable(primarydate TEXT PRIMARY KEY,"
                +"date TEXT,"
                +"day TEXT,"
                +"bmivalue REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }


    public void addARecord(String primarydate,String date,String day,double bmivalue)
    {
        ContentValues cv = new ContentValues();
        cv.put("primarydate",primarydate);
        cv.put("date",date);
        cv.put("day",day);
        cv.put("bmivalue",bmivalue);
        long rowId=db.insert("bmivaluetable",null,cv);
        if(rowId<0)
            Toast.makeText(context, "Can't Save :(", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Saved!!",Toast.LENGTH_SHORT).show();
    }
    public String[] viewHistory()
    {
        Cursor cursor=db.query("bmivaluetable",null,null,null,null,null,null);
        cursor.moveToFirst();
        String msg = "No Of Records:" + String.format("%d",cursor.getCount());
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();

        String []bmiValuesString=new String[cursor.getCount()];
        cursor.moveToFirst();
        int i=0;

        if(bmiValuesString.length!=0)
        {
            do
            {
                String condition="";
                double bmiValue=Double.parseDouble(cursor.getString(3));
                if(bmiValue<18.5)
                    condition="(UnderWeight)";
                else if(bmiValue<25)
                    condition="(Normal)";
                else if(bmiValue<30)
                    condition="(Overweight)";
                else
                    condition="(Obese)";
                bmiValuesString[i++] = "Date:"+cursor.getString(1)+"\nDay:"+cursor.getString(2)
                        +"\nBMI Value:"+cursor.getString(3)+condition;
            }while (cursor.moveToNext());
        }
        return bmiValuesString;
    }
}