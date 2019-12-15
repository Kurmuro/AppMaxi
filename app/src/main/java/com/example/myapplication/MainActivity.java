package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper userDB;
    Button btnAddData, btnEditData, btnDeleteData, btnAddUserdata, btnSelectUser;
    EditText etFirstname, etLastname, etBoattype, etYardstick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Zucück zum Hauptmenü
    public void zurück(View view) { setContentView (R.layout.activity_main); }

    //Zur Stegbelegung
    public void stegbelegung(View view) { setContentView(R.layout.steganlage); }

    //Zur Regatta
    public void regatta (View view) {
        userDB = new DatabaseHelper(this);
        setContentView(R.layout.regatta);
        btnSelectUser = (Button) findViewById(R.id.btnSelectUser);
        SelectUserData();
    }

    //Zum Blauenband
    public void blauesband (View view) {setContentView(R.layout.blauesband);}

    //Zu den Blauesband Regeln
    public void regelnbb (View view) {setContentView(R.layout.regelnbb);}

    //Benutzerverwaltung
    public void usersettings(View view){
        setContentView(R.layout.usersettings);

        userDB = new DatabaseHelper(this);

        btnEditData = (Button) findViewById(R.id.btnEditData);
        btnDeleteData = (Button) findViewById(R.id.btnDeleteData);
        btnAddUserdata = (Button) findViewById(R.id.btnAddUserdata);

        btnAddUserdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userfilltable();
            }
        });

        EditUserData();
        DeleteData();
    }

    //Formular zum Benutzer erstellen
    public void userfilltable(){
        setContentView(R.layout.userfilltable);

        userDB = new DatabaseHelper(this);

        etFirstname = (EditText) findViewById(R.id.firstname);
        etLastname = (EditText) findViewById(R.id.lastname);
        etBoattype = (EditText) findViewById(R.id.boattype);
        etYardstick = (EditText) findViewById(R.id.yardstick);
        btnAddData = (Button) findViewById(R.id.btnaddData);

        AddData();

        }

    //Formular zum Benutzer bearbeiten
    public void UserEdittable(int userid) {
        setContentView(R.layout.userfilltable);

        userDB = new DatabaseHelper(this);

        etFirstname = (EditText) findViewById(R.id.firstname);
        etLastname = (EditText) findViewById(R.id.lastname);
        etBoattype = (EditText) findViewById(R.id.boattype);
        etYardstick = (EditText) findViewById(R.id.yardstick);
        btnAddData = (Button) findViewById(R.id.btnaddData);

        Cursor data = userDB.showData();
        data.move(1);

        etFirstname.setText(data.getString(1));
        etLastname.setText(data.getString(2));
        etBoattype.setText(data.getString(3));
        etYardstick.setText(data.getString(4));

        EditData(userid);


    }

    //Benutzer neu anlegen
    public void AddData(){
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = etFirstname.getText().toString();
                String lastname = etLastname.getText().toString();
                String boattype = etBoattype.getText().toString();
                int yardstick = 0;
                
                if(!etYardstick.getText().toString().isEmpty()) {
                    yardstick = Integer.parseInt(etYardstick.getText().toString());
                }
                
                
                if(firstname != null && !firstname.isEmpty() && lastname != null && !lastname.isEmpty() &&
                        boattype != null && !boattype.isEmpty() && yardstick >= 1 && yardstick <= 9999 ) {
                    boolean insertData = userDB.AddData(firstname, lastname, boattype, yardstick);

                    if(insertData){
                        Toast.makeText(MainActivity.this, "Benutzer hinzugefügt", Toast.LENGTH_LONG).show();
                        userfilltable();
                    }else{
                        Toast.makeText(MainActivity.this, "Irgendwas ist schief gelaufen", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Fehler: Alles richtig eingegeben?", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    //Datenbank eintrag ändern
    public void EditData(final int userid){
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = etFirstname.getText().toString();
                String lastname = etLastname.getText().toString();
                String boattype = etBoattype.getText().toString();
                int yardstick = 0;

                if(!etYardstick.getText().toString().isEmpty()) {
                    yardstick = Integer.parseInt(etYardstick.getText().toString());
                }


                if(firstname != null && !firstname.isEmpty() && lastname != null && !lastname.isEmpty() &&
                        boattype != null && !boattype.isEmpty() && yardstick >= 1 && yardstick <= 9999 ) {
                    boolean insertData = userDB.EditData(Integer.toString(userid), firstname, lastname, boattype, yardstick);

                    if(insertData){
                        Toast.makeText(MainActivity.this, "Benutzer geändert", Toast.LENGTH_LONG).show();
                        usersettings(null);
                    }else{
                        Toast.makeText(MainActivity.this, "Irgendwas ist schief gelaufen", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Fehler: Alles richtig eingegeben?", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //Liste erstellen mit allen Editierbaren Benutzern
    public void EditUserData(){
        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = userDB.showData();

                if(data.getCount() == 0){
                    Toast.makeText(MainActivity.this, "Keine Teilnehmer Vorhanden", Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> users = new ArrayList<String>();
                String[] userlist = new String[users.size()];

                List<Integer> numbers = new ArrayList<Integer>();

                while(data.moveToNext()){
                    StringBuffer buffer = new StringBuffer();
                    numbers.add(data.getInt(0));
                    buffer.append("Vorname: " + data.getString(1) + "\n");
                    buffer.append("Nachname: " + data.getString(2) + "\n");
                    buffer.append("Boottyp: " + data.getString(3) + "\n");
                    buffer.append("Yardstick: " + data.getInt(4) + "\n");

                    users.add( buffer.toString());

                }

                userlist = users.toArray(userlist);
                displayEditView("Teilnehmer auswählen:", userlist ,numbers);
            }
        });
    }

    //Liste erstellen mit allen Löschbaren Benutzern
    public void DeleteData(){
        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = userDB.showData();

                if(data.getCount() == 0){
                    Toast.makeText(MainActivity.this, "Keine Teilnehmer Vorhanden", Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> users = new ArrayList<String>();
                String[] userlist = new String[users.size()];

                List<Integer> numbers = new ArrayList<Integer>();

                while(data.moveToNext()){
                    StringBuffer buffer = new StringBuffer();
                    numbers.add(data.getInt(0));
                    buffer.append("Vorname: " + data.getString(1) + "\n");
                    buffer.append("Nachname: " + data.getString(2) + "\n");
                    buffer.append("Boottyp: " + data.getString(3) + "\n");
                    buffer.append("Yardstick: " + data.getInt(4) + "\n");

                    users.add( buffer.toString());

                }

                userlist = users.toArray(userlist);
                displayDeleteView("Teilnehmer auswählen:", userlist ,numbers);
            }
        });
    }

    //Lite der Löschbaren Benutzer anzeigen
    public void displayDeleteView(String title, String[] userlist, final List<Integer> numbers){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(userlist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int deleteRow = userDB.deleteUser(Integer.toString(numbers.get(which)));
                if(deleteRow > 0){
                    Toast.makeText(MainActivity.this, "Erfolgreich gelöscht", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Irgendwas ist schief gegangen", Toast.LENGTH_LONG).show();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Liste der Edittierbaren Benutzer anzeigen
    public void displayEditView(String title, String[] userlist, final List<Integer> numbers){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(userlist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer userid = numbers.get(which);
                UserEdittable(userid);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Liste erstellen mit allen Auswählbaren Benutzern
    public void SelectUserData(){
        btnSelectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = userDB.showData();

                if(data.getCount() == 0){
                    Toast.makeText(MainActivity.this, "Keine Teilnehmer Vorhanden", Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> users = new ArrayList<String>();
                String[] userlist = new String[users.size()];


                List<Integer> numbers = new ArrayList<Integer>();

                while(data.moveToNext()){
                    StringBuffer buffer = new StringBuffer();
                    numbers.add(data.getInt(0));
                    buffer.append("Vorname: " + data.getString(1) + "\n");
                    buffer.append("Nachname: " + data.getString(2) + "\n");
                    buffer.append("Boottyp: " + data.getString(3) + "\n");
                    buffer.append("Yardstick: " + data.getInt(4) + "\n");

                    users.add( buffer.toString());

                }

                boolean[] checked = new boolean[users.size()];

                userlist = users.toArray(userlist);

                displaySelectView("Teilnehmer auswählen:", userlist , checked ,numbers);
            }
        });
    }

    //Liste der Teilnehmenden Benutzer anzeigen
    public void displaySelectView(final String title, final String[] userlist, final boolean[] checked, final List<Integer> numbers){

        final List<Integer> usersid = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMultiChoiceItems(userlist, checked , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(!usersid.contains(numbers.get(which))) {
                    usersid.add(numbers.get(which));
                    Array.setBoolean(checked, which, true);
                }else{
                    usersid.remove(numbers.get(which));
                    Array.setBoolean(checked, which, false);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                regatta(null);
                addUserstoList(usersid, checked, title, userlist, numbers);
            }
        });
        builder.setNegativeButton("Abbrechen", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //
    public void addUserstoList(List<Integer> usersid, final boolean[] checked, final String title, final String[] userlist, final List<Integer> numbers){

        Integer[] usersidstring = new Integer[usersid.size()];
        usersidstring = usersid.toArray(usersidstring);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.da_item, usersidstring);
        ListView list  = findViewById(R.id.regatteusertabel);
        list.setAdapter(adapter);



    }
}
