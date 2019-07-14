package com.example.madhu.bodymassindex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class WelcomeAPersonActivity extends AppCompatActivity
    implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks

{

    TextView tvCity,tvTemperature,tvWelcomeName,tvHeight,tvFeet,tvInch,tvWeight,tvColon,tvTempValue;
    Spinner spnFeet,spnInch;
    Button btnCalculate,btnViewHistory;
    EditText etWeight;
    FloatingActionButton fabCallDeveloper;
    ImageView ivLocationUnavailabale,ivNoWifi;
    TextToSpeech tts;

    SharedPreferences sp;//for welcoming a person

    GoogleApiClient gac;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_aperson);

        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvWelcomeName = (TextView) findViewById(R.id.tvWelcomeName);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvFeet = (TextView) findViewById(R.id.tvFeet);
        tvInch = (TextView) findViewById(R.id.tvInch);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvColon = (TextView) findViewById(R.id.tvColon);
        tvTempValue = (TextView) findViewById(R.id.tvTempValue);

        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInch = (Spinner) findViewById(R.id.spnInch);

        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnViewHistory = (Button) findViewById(R.id.btnViewHistory);

        etWeight = (EditText) findViewById(R.id.etWeight);

        fabCallDeveloper = (FloatingActionButton) findViewById(R.id.fabCallDeveloper);

        ivLocationUnavailabale = (ImageView) findViewById(R.id.ivLocationUnavailable);
        ivNoWifi = (ImageView) findViewById(R.id.ivNoWifi);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.ENGLISH);
            }
        });

        sp = getSharedPreferences("p1", MODE_PRIVATE);//p1 is the file name
        //in which key value pairs are stored
        final String name = sp.getString("name", "");//name string is the key which
        // was used in previous activity to store key value pair of name in sp
        // "" is the default value if we dont get anything
        tvWelcomeName.setText("Welcome " + name);


        //made google client in these 5 lines
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();
        //but still you aren't connected...its just client on your machine
        //connection is done in onResume
        //connection is undone/disconnected in onPause


        final Integer feet[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        ArrayAdapter<Integer> adapterFeet = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, feet);

        spnFeet.setAdapter(adapterFeet);


        final Integer inch[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        ArrayAdapter<Integer> adapterInch = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, inch);

        spnInch.setAdapter(adapterInch);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double weight;
                try {
                    weight = Double.parseDouble(etWeight.getText().toString());
                } catch (Exception e) {
                    tts.speak("Invalid Weight",TextToSpeech.QUEUE_ADD,null);
                    etWeight.setError("Invalid Weight");
                    etWeight.requestFocus();
                    return;
                }
                if (weight <= 0) {
                    tts.speak("Invalid Weight",TextToSpeech.QUEUE_ADD,null);
                    etWeight.setError("Invalid Weight");
                    etWeight.requestFocus();
                    return;
                }
                double height = 0.0;//1 inch = 0.0254 metre & 12 inches = 1 feet
                int feetposition = spnFeet.getSelectedItemPosition();
                int noOfFeets = feet[feetposition];
                int inchPosition = spnInch.getSelectedItemPosition();
                int noOfInches = inch[inchPosition];

                height = 0.0254 * (noOfInches + 12 * noOfFeets);

                double bmiValue = weight / (height * height);

                Intent i = new Intent(WelcomeAPersonActivity.this, ResultActivity.class);
                i.putExtra("BMIValue", bmiValue);
                startActivity(i);

                etWeight.setText("");
            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeAPersonActivity.this, HistoryActivity.class);
                startActivity(i);

                etWeight.setText("");
            }
        });

        fabCallDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + "8087270139"));
                startActivity(i);

                etWeight.setText("");
            }
        });

    }//end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1,menu);
        return true;
    }//end of OnCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.website)
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://"+"www.kamalsir.com"));
            startActivity(i);
        }
        if(item.getItemId()==R.id.about)
        {
            Snackbar.make(findViewById(android.R.id.content),"App Developed By Raghani Madhu",Snackbar.LENGTH_LONG).show();
            tts.speak("App Developed By Raghani Madhu",TextToSpeech.QUEUE_ADD,null);
        }
        if(item.getItemId()==R.id.help) {
            Toast.makeText(this,
                    "A BMI Calculator which will help you to pay attention On your BMI,thus,helping you take care of yourself ",
                    Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }//end of Options Selected


    //3 below methods are overriden methods of interfaces
    //GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks
    //for getting the location of the user
    @Override
    public void onConnected(Bundle bundle) {

        loc=LocationServices.FusedLocationApi.getLastLocation(gac);

        if(loc!=null)
        {
            double lat=loc.getLatitude();
            double lon=loc.getLongitude();

            Geocoder g = new Geocoder(this, Locale.ENGLISH);

            try {
                List<android.location.Address> la= g.getFromLocation(lat,lon,1);
                android.location.Address add = la.get(0);
                String cityName=add.getLocality();
                if(cityName==null)
                {
                    tvCity.setText("");
                    ivLocationUnavailabale.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvCity.setText(cityName);
                    ivLocationUnavailabale.setVisibility(View.INVISIBLE);
                }

                //for getting the weather u want cityName and you will get it
                //from geoLocation only
                //thus for that after getting the cityNme and setting it
                //we are getting temperature here only
                String url = "http://api.openweathermap.org/";
                String specificurl = "data/2.5/weather?units=metric";
                String query = "&q=" + cityName;
                String id = "f19960f53da88b1ceefc4852de4d1fdf";//khudki nikalna
                String message = url + specificurl + query + "&appid=" + id;

                MyTemperatureTask t = new MyTemperatureTask();
                t.execute(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            tts.speak("Please Switch On The GPS", TextToSpeech.QUEUE_ADD, null);
            Toast.makeText(this, "bahar niklkar...gps on karey", Toast.LENGTH_SHORT).show();
        }
    }//end of onConnected

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    //these onPause and onResume are functions where the connection is actually build or broke
    @Override
    protected void onResume() {
        super.onResume();
        if(gac!=null)
            gac.connect();
    }//End of onPause

    @Override
    protected void onPause() {
        super.onPause();
        if(gac!=null)
            gac.disconnect();
    }//End of onResume

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

    //for getting temperature
    class MyTemperatureTask extends AsyncTask<String,Void,Double>
    {
        @Override
        protected Double doInBackground(String... strings) {
            double temp=0.0;
            String line="",jsonData="";

            try {
                //making connection to that website from where we will give cityname
                //and will get weather
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                //input Stream of connrction in InputStreamReader Constructor
                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                while ((line=br.readLine())!=null)
                    jsonData+=line+"\n";

                JSONObject o = new JSONObject(jsonData);
                JSONObject p = o.getJSONObject("main");
                temp=p.getDouble("temp");
            } catch (Exception e) {}
            return temp;
        }//end of doInBackGround

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            if(aDouble==0.0)
            {
                tvTempValue.setText("");
                ivNoWifi.setVisibility(View.VISIBLE);
            }
            else
            {
                tvTempValue.setText(aDouble+"°");
                ivNoWifi.setVisibility(View.INVISIBLE);
            }
        }//end of OnPostExecute
    }//end of MyTask
}//end of WelcomeAPersonActicity
 