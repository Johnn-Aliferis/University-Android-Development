package com.example.johnaliferishugzz.unipimeterapplication1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.AudioEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import javax.security.auth.Destroyable;

import static com.example.johnaliferishugzz.unipimeterapplication1.MenyActivity.FirstTime;

public class RegisterPoiActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_poi);
        db = openOrCreateDatabase("SpeedLimitDatabase", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS registerPOI(title1 VARCHAR,category1 VARCHAR,longitude1 VARCHAR,latitude1 VARCHAR,description1 VARCHAR);");
    }
    public void DoneCardViewRegisterClicked(View view)
    {
         EditText Title       = findViewById(R.id.editText);
         EditText Category    = findViewById(R.id.editText2);
         EditText Longitude   = findViewById(R.id.editText6);
         EditText Latitude    = findViewById(R.id.editText5);
         EditText Description = findViewById(R.id.editText8);
         String myTitle       = Title.getText().toString();
         String myCategory    = Category.getText().toString();
         String myLongitude   = Longitude.getText().toString();
         String myLatitude    = Latitude.getText().toString();
         String myDescription = Description.getText().toString();
         boolean areEmpty=false;
         //Checking if one of the fields is empty...!
         if(myTitle.matches("") || myCategory.matches("") || myLongitude.matches("") || myLatitude.matches("") || myDescription.matches("")){
             areEmpty=true;
         }
         if(!areEmpty)
         {       // insert and register the poi into the  database
                 Toast.makeText(this, "Point of interest registered!", Toast.LENGTH_LONG).show();
                 db.execSQL("INSERT INTO  registerPOI  VALUES('"+myTitle+"','"+myCategory+"','"+myLongitude+"','"+myLatitude+"','"+myDescription+"')");
                 db.close();
                 Intent intent = new Intent(this, MenyActivity.class);
                 startActivity(intent);
         }
         else
         {
             Toast.makeText(this,"Please fill in all fields !",Toast.LENGTH_LONG).show();
         }
    }
    public void MyPOIsClicked(View view){
        Intent intent = new Intent(this,MyRegisteredPOIsActivity.class);
        startActivity(intent);
    }
}
