package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.text.DateTimePatternGenerator;
import android.icu.text.IDNA;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.DatabaseHelper.COL1;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper userDB;
    Button btnAddData, btnEditData, btnDeleteData, btnAddUserdata, btnSelectUser;
    EditText etFirstname, etLastname, etBoattype, etYardstick;

    boolean[] checked;



    static boolean timerisrunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Zucück zum Hauptmenü
    public void zurück(View view) { setContentView (R.layout.activity_main); }

    //Zur Stegbelegung
    public void stegbelegung(View view) { setContentView(R.layout.steganlage); }



    static long start;
    //timer
    public void timer(){

        //long timeElapsed = finish - start;


        final TextView timeview = findViewById(R.id.EtTime);
        final Button btnStartTime = (Button) findViewById(R.id.btnStartTime);
        final Timer stoppuhr =  new Timer();
        final String[] secondString = new String[3];
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!timerisrunning) {
                    if(checked != null) {
                        start = System.currentTimeMillis();
                        btnStartTime.setText("Beenden");
                        timerisrunning = true;
                        stoppuhr.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                final long longseconds = (System.currentTimeMillis() - start)/1000;
                                final int a = (int)longseconds;
                                final int stunden = a / 3600;
                                final int minuten = (a % 3600) / 60;
                                final Integer sekunden = (a % 3600) % 60;


                                secondString[0] = Integer.toString(sekunden);
                                if(sekunden <=9) {
                                    secondString[0] = "0" + sekunden;

                                }
                                secondString[1] = Integer.toString(minuten);
                                if(minuten <=9) {
                                    secondString[1] = "0" + minuten;

                                }
                                secondString[2] = Integer.toString(stunden);
                                if(stunden <=9) {
                                    secondString[2] = "0" + stunden;

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()

                                    { timeview.setText(secondString[2]+":"+secondString[1]+":"+ secondString[0]);
                                    }
                                });
                            }
                        },0,1000);

                    }

                }else{
                    stoppuhr.cancel();
                    regatta(null);
                    checked = null;
                    timerisrunning = false;
                }
            }
        });
    }

    //Zur Regatta
    public void regatta (View view) {


        userDB = new DatabaseHelper(this);
        setContentView(R.layout.regatta);
        btnSelectUser = (Button) findViewById(R.id.btnSelectUser);
        SelectUserData();
        timerisrunning = false;

        timer();

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

        data.moveToFirst();
        while (data.getInt(data.getColumnIndex("ID")) != userid) {
            data.moveToNext();
        }

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

                if(checked == null) {
                    checked = new boolean[users.size()];
                }

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
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < checked.length; i++){
                    if(checked[i]){
                        usersid.add(numbers.get(i));
                    }
                }
                addUserstoList(usersid);
            }
        });
        builder.setNegativeButton("Abbrechen", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //
    public void addUserstoList(List<Integer> usersid){

        Cursor data = userDB.showData();

        List<String> users = new ArrayList<String>();

        for(int i:usersid){
            data.moveToFirst();
            while (data.getInt(data.getColumnIndex("ID")) != i) {
                data.moveToNext();
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(data.getString(1) + " ");
            buffer.append(data.getString(2));

            users.add( buffer.toString());

        }

        ListView list  = findViewById(R.id.regatteusertabel);
        list.setAdapter(new MyListAdapter(this, R.layout.regatta_items, users));

    }

    //wechsel der Stegbelegung
    boolean stegaisshowing = true;
    public void wechsel (View view) {
        ImageView imageViewStegA = findViewById(R.id. imageViewStegA);
        ImageView imageViewStegB = findViewById(R.id. imageViewStegB);

        if(stegaisshowing) {
            stegaisshowing = false;
            imageViewStegA.animate().alpha(0).setDuration(2000);
            imageViewStegB.animate().alpha(1).setDuration(2000);
        }else {
            stegaisshowing = true;
            imageViewStegA.animate().alpha(1).setDuration(2000);
            imageViewStegB.animate().alpha(0).setDuration(2000);

        }

    }

    }
    class MyListAdapter extends ArrayAdapter<String>{

        int layout;
        List<String> object;
        boolean timerisrunning;
        public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
            object = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.regatta_name);
                viewHolder.name.setText(object.get(position));
                viewHolder.time = (TextView) convertView.findViewById(R.id.regatta_timer);
                viewHolder.btnStop = (Button) convertView.findViewById(R.id.regatta_btn_stop);
                viewHolder.btnStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean test = MainActivity.timerisrunning;
                        long longseconds = (System.currentTimeMillis() - MainActivity.start)/1000;
                        if(test) {
                            //Toast.makeText(getContext(), "Teilnehmer " + object.get(position) + " im Ziel", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), Long.toString(longseconds), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Du musst zuerst die Zeit Starten", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                convertView.setTag(viewHolder);
            }
            else{
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.name.setText(getItem(position));
            }

            return convertView;
        }
    }
    class ViewHolder{
    TextView name;
    TextView time;
    Button btnStop;
    }



