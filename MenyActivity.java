package com.example.johnaliferishugzz.unipimeterapplication1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.location.Location;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MenyActivity extends AppCompatActivity {

    public static boolean FirstTime = true;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meny);
        SharedPreferences shared = getSharedPreferences("Values", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        if (shared.getBoolean("my_first_time", true)) {
            //check if the app is launched for the first time !
            Log.d("Comments", "First time");

            // first time values (default)
            editor.putString("SpeedLimit", "40");
            editor.putString("Range", "10");
            editor.apply();
            // Default pois for user! (registered during the first launch of the application!)
            db = openOrCreateDatabase("SpeedLimitDatabase", Context.MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS registerPOI(title1 VARCHAR,category1 VARCHAR,longitude1 VARCHAR,latitude1 VARCHAR,description1 VARCHAR);");
            db.execSQL("INSERT INTO registerPOI VALUES('Parthenon','Sights','37.97025','23.72247','Parthenon of Athens,created in BC years!')");
            db.execSQL("INSERT INTO registerPOI VALUES('University of Piraeus','University','37.941557662723305','23.65279197692871','University of Piraeus')");
            db.close();
            // app launched, so put boolean variable to false !
            shared.edit().putBoolean("my_first_time", false).commit();
        }
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this,"GPS must be enabled for this app to work!",Toast.LENGTH_LONG).show();
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }
    public void StartApplicationClicked(View view){
        Intent intent = new Intent(this,StartApplicationActivity.class);
        startActivity(intent);
    }
    public void SetUpSpeedLimitClicked(View view){
        Intent intent = new Intent(this,SetUpSpeedLimitActivity.class);
        startActivity(intent);
    }
    public void RegisterPOIClicked(View view){
        Intent intent = new Intent(this,RegisterPoiActivity.class);
        startActivity(intent);
    }
    public void DisplayStatisticsClicked(View view) {
        Intent intent = new Intent(this,DisplayStatisticsActivity.class);
        startActivity(intent);
    }
}
