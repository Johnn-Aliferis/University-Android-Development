package com.example.johnaliferishugzz.unipimeterapplication1;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.johnaliferishugzz.unipimeterapplication1.MenyActivity.FirstTime;

public class StartApplicationActivity extends AppCompatActivity implements LocationListener {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_application);
        //check for user permissions!
        if(ContextCompat.checkSelfPermission( this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(StartApplicationActivity.this,
                    new String[]{permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else
        {
          LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
          lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
          this.onLocationChanged(null);
          db = openOrCreateDatabase("SpeedLimitDatabase", Context.MODE_PRIVATE,null);
          db.execSQL("CREATE TABLE IF NOT EXISTS speed(date VARCHAR,currentspeed VARCHAR,longitude VARCHAR,latitude VARCHAR);");
          db.execSQL("CREATE TABLE IF NOT EXISTS closePoi(date VARCHAR,title VARCHAR,longitude VARCHAR,latitude VARCHAR);");
        }
    }
    public int counter = 0;
    public double currentSpeed;
    public double longitude;
    public double latitude;
    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences shared= getSharedPreferences("Values",MODE_PRIVATE);
        TextView SpeedTextView = findViewById(R.id.SpeedTextView);
       if(location==null)
       {
           SpeedTextView.setText("Speed not available !");
       }
       else {
           //calculate user's speed!
           currentSpeed = location.getSpeed() * 3.6;
           currentSpeed = Math.round(currentSpeed * 10.0 / 10.0);
           SpeedTextView.setText(currentSpeed + "  KM / H");
           //making sure that the speed is only registered once and not all the time if speed remains above the limit !
           if (currentSpeed <= Double.parseDouble(shared.getString("SpeedLimit", ""))) {
               counter = 0;
           } else {
               counter++;
           }
           if (currentSpeed > Double.parseDouble(shared.getString("SpeedLimit", "")) && counter == 1) {
               showMessage("You excited the speed limit ! Be careful !");
               //insert into database statistics speed limit excited !
               Calendar calendar = Calendar.getInstance();
               String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
               SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
               String time1 = sdf.format(calendar.getTime());
               double longitude1 = location.getLongitude();
               double latitude1 = location.getLatitude();
               db.execSQL("INSERT INTO speed VALUES('" + currentDate + " " + time1 + "','" + currentSpeed + "','" + longitude1 + "','" + latitude1 + "')");
           }
           //Check if user is close to one or more of his POI
           longitude = location.getLongitude();
           latitude = location.getLatitude();
           Location myLocation = new Location("");
           myLocation.setLatitude(latitude);
           myLocation.setLongitude(longitude);
           Cursor cursor = db.rawQuery("SELECT * FROM registerPOI", null);
           StringBuffer buffer = new StringBuffer();
           StringBuffer buffer2 = new StringBuffer();
           StringBuffer buffer3 = new StringBuffer();
           int rows = cursor.getCount();
           // declaring two arrays for longitude and latitude of our registered points in database !
           String Longitude[] = new String[rows];
           String Latitude[] = new String[rows];
           String TitleOfPoi[] = new String[rows];
           int i = 0;
           if (rows == 0) {
               // no data in database!
           } else {
               while (cursor.moveToNext()) {
                   //1st position of our arrays contain the longitude, latitude and title of the first  point from database, etc... !
                   buffer.append(cursor.getString(2));
                   buffer2.append(cursor.getString(3));
                   buffer3.append(cursor.getString(0));
                   Longitude[i] = buffer.toString();
                   Latitude[i]  = buffer2.toString();
                   TitleOfPoi[i] = buffer3.toString();
                   buffer.delete(0, buffer.length());
                   buffer2.delete(0, buffer2.length());
                   buffer3.delete(0, buffer3.length());
                   i++;
               }
               double distanceInMeters;
               Location poiLocation = new Location("");
               // each time our location changes, we check all of the points in database to see if we are close , and if we are we get an alert and insert the incident into the database !
               for (int j = 0; j <= rows-1; j++) {
                       poiLocation.setLongitude(Double.parseDouble(Longitude[j]));
                       poiLocation.setLatitude(Double.parseDouble(Latitude[j]));
                       distanceInMeters = myLocation.distanceTo(poiLocation);
                   if (distanceInMeters <= Double.parseDouble(shared.getString("Range", ""))) {
                       Toast.makeText(this,"You are close to the point of interest : " + TitleOfPoi[j],Toast.LENGTH_LONG).show();
                       // insert into the database for the statistics !
                       Calendar calendar = Calendar.getInstance();
                       String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                       SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                       String time1 = sdf.format(calendar.getTime());
                       db.execSQL("INSERT INTO closePoi VALUES('" + currentDate + " " + time1 + "','" + TitleOfPoi[j] + "','" + longitude + "','" + latitude + "')");
                   }
               }
           }
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void showMessage(String s){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Alert") ;
        builder.setMessage(s);
        builder.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            lm.removeUpdates(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
