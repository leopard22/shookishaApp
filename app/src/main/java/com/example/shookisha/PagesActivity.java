package com.example.shookisha;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.entity.Offre;
import com.example.shookisha.ui.main.OffreFragment;
import com.example.shookisha.ui.main.SectionsPagerAdapter;
import com.example.shookisha.utility.UserDataSource;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class PagesActivity extends AppCompatActivity {

    private UserDataSource shookisher;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Location longitudeLatitude;
    private LocationManager locationManager;

   // private Bundle bundle;
    //private OffreFragment offreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        shookisher = new UserDataSource(user);

        System.out.println("je suis passé par ici PagesActivity");
        System.out.println(user);

        setContentView(R.layout.activity_pages);

       /* TextView title = findViewById(R.id.title);
        title.setText("");*/
        ImageButton btnPreference = findViewById(R.id.btnPreference);
        btnPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"bouton image cliqué",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext() , Preference.class);
                intent.putExtra("user", shookisher.getUserSource());
                intent.putExtra("from","pages");
                startActivity(intent);
            }
        });
        ImageButton btnPerimetre = findViewById(R.id.btnPerimetre);
        btnPerimetre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"bouton image cliqué",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext() , PerimetreActivity.class);
                intent.putExtra("user", shookisher.getUserSource());
                intent.putExtra("from","pages");
                startActivity(intent);
            }
        });


        checkLocation();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),user);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    /*private void detailOffreActivity(Offre o){

        Intent intent = new Intent(getApplicationContext() , OffreDetailActivity.class);
        intent.putExtra("uneOffre", o);
        startActivity(intent);
    }*/

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Activer la localisation")
                .setMessage("Les paramètres de votre emplacement est réglé sur 'Off'. S'il vous plaît activer l'emplacement pour utiliser cette application")
                .setPositiveButton("Les paramètres de localisation", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
}