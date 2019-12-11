package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper userDB;
    Button btnAddData, btnViewData;
    EditText etFirstname, etLastname, etBoattype, etYardstick;

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

        userDB = new DatabaseHelper(this);

        btnViewData = (Button) findViewById(R.id.btnViewData);

        ViewData();
    }

    public void userfilltable(View view){
        setContentView(R.layout.userfilltable);

        userDB = new DatabaseHelper(this);

        etFirstname = (EditText) findViewById(R.id.firstname);
        etLastname = (EditText) findViewById(R.id.lastname);
        etBoattype = (EditText) findViewById(R.id.boattype);
        etYardstick = (EditText) findViewById(R.id.yardstick);
        btnAddData = (Button) findViewById(R.id.btnaddData);

        AddData();
    }

    public void AddData(){
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = etFirstname.getText().toString();
                String lastname = etLastname.getText().toString();
                String boattype = etBoattype.getText().toString();
                int yardstick = Integer.parseInt(etYardstick.getText().toString());

                boolean insertData = userDB.AddData(firstname, lastname, boattype, yardstick);

                if(insertData == true){
                    Toast.makeText(MainActivity.this, "Benutzer hinzugefügt", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Irgendwas ist schief gelaufen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void ViewData(){
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = userDB.showData();

                if(data.getCount() == 0){
                    display("Fehler", "Keine Teilnehmer vorhanden");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(data.moveToNext()){
                    buffer.append("Nummer: " + data.getString(0) + "\n");
                    buffer.append("Vorname: " + data.getString(1) + "\n");
                    buffer.append("Nachname: " + data.getString(2) + "\n");
                    buffer.append("Boottyp: " + data.getString(3) + "\n");
                    buffer.append("Yardstick: " + data.getInt(4) + "\n\n");


                }
                display("Teilnehmer:", buffer.toString());
            }
        });
    }

    public void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
