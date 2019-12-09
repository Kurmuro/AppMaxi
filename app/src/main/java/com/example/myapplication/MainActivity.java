package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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

    public void useradd(View view){
        setContentView(R.layout.useradd);
    }
}
