package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void zurück(View view) { setContentView (R.layout.activity_main); }

    public void stegbelegung(View view) { setContentView(R.layout.steganlage); }

    public void regatta (View view) {
        setContentView(R.layout.regatta);
    }

    public void blauesband (View view) {setContentView(R.layout.blauesband);}

    public void regelnbb (View view) {setContentView(R.layout.regelnbb);}

    public void usersettings(View view){
        setContentView(R.layout.usersettings);
    }

    public void userfilltable(View view){
        setContentView(R.layout.userfilltable);
    }

    public void useradd(View view){
        //useredit("add");
        Log.i("Knopf","Gedrückt");
        useredit("get");
    }

    public void useredit(String option){
        try{
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("users", MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (firstname VARCHAR, lastname VARCHAR, boattype VARCHAR, yardstick INT)");

            switch(option){
                case "add":
                    TextView firstname = findViewById(R.id.firstname);//"+firstname.getText().toString()+"
                    TextView lastname = findViewById(R.id.lastname);//"+lastname.getText().toString()+"
                    TextView boattype = findViewById(R.id.boattype);//"+boattype.getText().toString()+"
                    TextView yardstick = findViewById(R.id.yardstick);//"+Integer.parseInt(yardstick.getText().toString())+"


                    sqLiteDatabase.execSQL("INSERT INTO users (firstname, lastname, boattype, yardstick) VALUES ('Tom','jo','igel',5)");
                    Toast.makeText(this,"Benutzer hinzugefügt", Toast.LENGTH_LONG).show();
                    break;
                case "get":
                    Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users", null);
                    int first = c.getColumnIndex("name");
                    int last = c.getColumnIndex("last");

                    if(c.moveToFirst()){
                        do{
                            Log.i("Vorname", c.getString(first));
                            Log.i("Vorname", c.getString(last));
                        }while(c.moveToNext());
                    }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
