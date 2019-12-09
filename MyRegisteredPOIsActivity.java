package com.example.johnaliferishugzz.unipimeterapplication1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.johnaliferishugzz.unipimeterapplication1.MenyActivity.FirstTime;

public class MyRegisteredPOIsActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_registered_pois);
        //insert into ListView data from database for user's registred Points of Interest!
        ListView lv = findViewById(R.id.MyListView1);
        db = openOrCreateDatabase("SpeedLimitDatabase", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM registerPOI", null);
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
             buffer.append(cursor.getString(0));
                 Rows[i]= buffer.toString();
                 buffer.delete(0, buffer.length());
                 i++;
             }
            }
        final List Mylist = Arrays.asList(Rows);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, Mylist);
        lv.setAdapter(arrayAdapter);
        db.close();
    }
}