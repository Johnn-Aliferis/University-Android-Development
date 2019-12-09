package com.example.johnaliferishugzz.unipimeterapplication1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import static com.example.johnaliferishugzz.unipimeterapplication1.MenyActivity.FirstTime;

public class DisplayStatisticsActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_statistics);
        //Instert into Listview data from database for excideening speed limit and getting close to a Poi !
        ListView lv = findViewById(R.id.ExceededSpeedLimitListView);
        ListView lv1 =findViewById(R.id.WentCloseListView);
        db = openOrCreateDatabase("SpeedLimitDatabase", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS speed(date VARCHAR,currentspeed VARCHAR,longitude VARCHAR,latitude VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS closePoi(date VARCHAR,title VARCHAR,longitude VARCHAR,latitude VARCHAR);");
        //List view for exceeding speed limit !
        Cursor cursor = db.rawQuery("SELECT * FROM speed", null);
        StringBuffer buffer = new StringBuffer();
        int rows = cursor.getCount();
        String Rows[] = new String[rows];
        int i=0;
        if (rows== 0)
        {
            // no data in database!
        }
        else
        {
            while (cursor.moveToNext())
            {
                //We do not show longitude and latitude to user because statistics need to be as useful for the user as they must!
                buffer.append("TIMESTAMP : "+cursor.getString(0)+" SPEED: "+cursor.getString(1));
                Rows[i]= buffer.toString();
                buffer.delete(0, buffer.length());
                i++;
            }
        }
        final List Mylist = Arrays.asList(Rows);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, Mylist);
        lv.setAdapter(arrayAdapter);
        //List view for getting close to Registered points of interest !
        Cursor cursor1= db.rawQuery("SELECT * FROM closePoi", null);
        StringBuffer buffer1 = new StringBuffer();
        int rows1 = cursor1.getCount();
            String Rows1[] = new String[rows1];
        int i1=0;
        if (rows1== 0)
        {
            // no data in database!
        }
        else
        {
            while (cursor1.moveToNext())
            {
                buffer1.append("TIMESTAMP : "+cursor1.getString(0)+"\n"+"Point of interest: "+cursor1.getString(1) + "\n" + "Longitude: "+ cursor1.getString(2)+ "\n"+"Latitude : "+cursor1.getString(3));
                Rows1[i1]= buffer1.toString();
                buffer1.delete(0, buffer1.length());
                i1++;
            }
        }

        final List Mylist1 = Arrays.asList(Rows1);
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, Mylist1);
            lv1.setAdapter(arrayAdapter1);
            db.close();
        }
    }

