package com.example.shookisha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.data.model.LoggedInUser;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.UserDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PerimetreActivity extends AppCompatActivity {

    private int distance;
    private Button saveDistance;
    private LoggedInUser loggedinUser;
    private String username,mdp,from,userReceive;
    private SeekBar distanceSeekBar;
    private TextView kilometreTextView;
    private UserDataSource shookisher;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i = getIntent();
        loggedinUser = (LoggedInUser) i.getSerializableExtra("loggedinUser");
        username = i.getStringExtra("username");
        userReceive = i.getStringExtra("user");
        from = i.getStringExtra("from");

        setContentView(R.layout.activity_perimetre);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        ImageButton btnBackPerim = findViewById(R.id.btnBackPerim);
        ImageButton btnReg = findViewById(R.id.btnReg);
        kilometreTextView = findViewById(R.id.kilometreTextView);
        distanceSeekBar = findViewById(R.id.distanceSeekBar);
        saveDistance = findViewById(R.id.savePerimetrebutton);
        if(from.compareTo("pages")==0){
            System.out.println("from pages oncreate Perimetre");
            setPerimetre(userReceive);
            btnReg.setVisibility(View.VISIBLE);
        }
        if(from.compareTo("profil") == 0){
            shookisher = new UserDataSource(userReceive);
            btnReg.setVisibility(View.GONE);
        }
       // btnReg.setEnabled(false);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext() , Preference.class);
                intent.putExtra("user", userReceive);
                intent.putExtra("from",from);
                startActivity(intent);
            }
        });

        kilometreTextView.setText(getString(R.string.distance_kilometre,distanceSeekBar.getProgress()));

        btnBackPerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                kilometreTextView.setText(getString(R.string.distance_kilometre,i));
                distance = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(getApplicationContext(), "Aucune connexion à internet.", Toast.LENGTH_LONG).show();
                    return;
                }
                new SavePerimetre().execute(getPerimetre());
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class SavePerimetre extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {

            Api api = new Api();
            String resultApi="";
            try{
                resultApi = api.postAuth("updatedistance",strings[0],shookisher.getUser().getLoggedInUser().getToken());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println(" reponse server "+ s );
            connect(s);
        }
    }

    private String getPerimetre(){
        String jsonStr = "{\"distance\": "+distance+ " } ";
        return jsonStr ;
    }

    private void connect(String s){

        try{
            JSONObject obj = new JSONObject(s);

            if (obj.getString("status").compareTo("200") == 0 ){

                new ConnexionTask().execute(sharedPref.getString(getString(R.string.username_or_email),""),sharedPref.getString(getString(R.string.passwor),""));

            }else{
             //   finish();
            }

        }
        catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private class ConnexionTask extends AsyncTask<String,Void,String  > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String leloggin = strings[0];
            String lepassword = strings[1];
            String resultApi = "null";
            Api api = new Api();

            String userlogin = "{\n" +
                    "  \"password\": \""+lepassword+ "\",\n" +
                    "  \"usernameOrEmail\": \""+leloggin+"\"\n" +
                    "}";

            try{
                resultApi = api.post("auth/signin",userlogin);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            try{
                JSONObject obj = new JSONObject(s);

                if(obj.getString("status").compareTo("401") == 0){
                    //loadingProgressBar.setVisibility(View.INVISIBLE);
                  //  Toast.makeText(getApplicationContext(), "mot de passe ou username incorrect ", Toast.LENGTH_LONG).show();
                    System.out.println("ok erreur 401");
                }else{

                    if (obj.getString("status").compareTo("200") == 0  ){
                        Intent intent = new Intent(getApplicationContext() , PagesActivity.class);
                        intent.putExtra("user", s);
                        startActivity(intent);
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPerimetre(String user){
        shookisher = new UserDataSource(user);
        kilometreTextView.setText(getString(R.string.distance_kilometre, shookisher.getUser().getConsommateur().getPrefDistance()));
        distance = shookisher.getUser().getConsommateur().getPrefDistance();
        distanceSeekBar.setProgress(shookisher.getUser().getConsommateur().getPrefDistance());
    }

}
