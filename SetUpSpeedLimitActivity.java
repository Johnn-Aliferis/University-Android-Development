package com.example.johnaliferishugzz.unipimeterapplication1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.johnaliferishugzz.unipimeterapplication1.MenyActivity.FirstTime;

public class SetUpSpeedLimitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_speed_limit);
    }
    public void DoneCardViewClicked(View view){
        // checking if user input for speed limit is a number updating the speed limit !
        EditText myText= findViewById(R.id.NewSpeedLimitEditText);
        EditText myText2 =findViewById(R.id.editText3);
        if(!myText.getText().toString().matches("") && !myText2.getText().toString().matches(""))
        {
            //Save the input the user gave into a Shared Preferences file!
            SharedPreferences shared= getSharedPreferences("Values",MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("SpeedLimit",myText.getText().toString());
            editor.putString("Range",myText2.getText().toString());
            editor.apply();
            Toast.makeText(this,"Speed limit updated to : "+myText.getText().toString()+" and range : " +myText2.getText().toString(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MenyActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Check your input ! ",Toast.LENGTH_SHORT).show();
        }
    }
    public void DisplayValuesClicked(View view){
        //display the values for speed limit and the range in meters !
        SharedPreferences shared= getSharedPreferences("Values",MODE_PRIVATE);
        String speedLimit = shared.getString("SpeedLimit","");
        String range = shared.getString("Range","");
        Toast.makeText(this,"Speed limit is : " +speedLimit + " and range is: " + range,Toast.LENGTH_LONG).show();
    }
}
