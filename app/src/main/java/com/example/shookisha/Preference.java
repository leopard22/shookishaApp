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
import android.widget.ListView;
import android.widget.Toast;

import com.example.shookisha.data.model.LoggedInUser;
import com.example.shookisha.entity.Categorie;
import com.example.shookisha.shared.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Preference extends AppCompatActivity {

    private ListView listPreferencesView;
    private LoggedInUser loggedinUser;
    private Categorie []tabCategori;
    private ArrayList<Categorie> modelArrayList;
    private ListPreferenceAdapter listPreferenceAdapter;
    private Button btnSavePreference;
    private String from="";
    private String user;
    private String mdp;
    private String username;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        from = intent.getStringExtra("from");
        mdp = intent.getStringExtra("mdp");
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        System.out.println("preference activity :: "+user);
        System.out.println("preference activity :: sharedPref : login = "+sharedPref.getString(getString(R.string.username_or_email),""));
        System.out.println("preference activity :: sharedPref : password = "+sharedPref.getString(getString(R.string.passwor),""));

        setAttribut(user);

        setContentView(R.layout.activity_preference);

        //liste des éléments
        ImageButton btnBackPref = findViewById(R.id.btnBackPref);
        ImageButton btnGoPerimetre = findViewById(R.id.btnGoPerimetre);
        listPreferencesView = findViewById(R.id.listPreferences);
        btnSavePreference = findViewById(R.id.btnSavePreference);

        if(from.compareTo("profil") == 0){
            btnGoPerimetre.setEnabled(false);
        }
        modelArrayList = getModel(tabCategori);
        listPreferenceAdapter = new ListPreferenceAdapter(this,modelArrayList);
        listPreferencesView.setAdapter(listPreferenceAdapter);
        btnSavePreference = findViewById(R.id.btnSavePreference);

        btnGoPerimetre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , PerimetreActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("from",from);
                startActivity(intent);
            }
        });

        btnBackPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSavePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(getApplicationContext(), "Aucune connexion à internet.", Toast.LENGTH_LONG).show();
                    return;
                }
                new SavePreference().execute(getSelected());
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

    private ArrayList<Categorie> getModel(Categorie []tab){
        ArrayList<Categorie> list = new ArrayList<>();

        for(int i=0; i< tab.length;i++ ){
            Categorie model = new Categorie(tab[i].getId(),tab[i].getLabel(),tab[i].getImg(),tab[i].getTotalOffers(),tab[i].getChecked());
            list.add(model);
        }

        return list;
    }

    private String getSelected(){

        List<String> catSelected = new ArrayList<>();

        for(int j=0; j < modelArrayList.size(); j++){
            /*  if(modelArrayList.get(j).getChecked().compareTo("true") == 0){

                catSelected.add("{ \"catId\": "+Integer.parseInt( modelArrayList.get(j).getId())+ "," + "  \"checked\": "+Boolean.valueOf(modelArrayList.get(j).getChecked())+"}");
                System.out.println( modelArrayList.get(j).getId()+" "+modelArrayList.get(j).getLabel()+" "+ modelArrayList.get(j).getChecked());
            }*/
          catSelected.add("{ \"catId\": "+Integer.parseInt( modelArrayList.get(j).getId())+ "," + "  \"checked\": "+Boolean.valueOf(modelArrayList.get(j).getChecked())+"}");
        }

        System.out.println(" toString function "+ catSelected.toString() );

        return catSelected.toString();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class SavePreference extends AsyncTask <String,Void,String>{
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

            String resultApi = "null";
            Api api = new Api();
            try{
                resultApi = api.postAuth("setpreferences",strings[0],loggedinUser.getToken());
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println(" reponse server :: "+ s );
            connect(s);
        }
    }

    private void connect(String s){
        String code,message;

        try{
            JSONObject obj = new JSONObject(s);

            System.out.println(s);
/**/
            if(from.compareTo("pages") == 0){
                new ConnexionTask().execute(sharedPref.getString(getString(R.string.username_or_email),""),sharedPref.getString(getString(R.string.passwor),""));
                //finish();
            }else{}

            if (obj.getString("status").compareTo("200") == 0 && from.compareTo("profil") == 0){
                Intent intent = new Intent(getApplicationContext() , PerimetreActivity.class);
                intent.putExtra("loggedinUser", loggedinUser);
                intent.putExtra("user",user);
                intent.putExtra("from","profil");
                startActivity(intent);
            }else{}
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAttribut(String user){
        try {
            JSONObject obj = new JSONObject(user);
            JSONObject content = obj.getJSONObject("content");
            JSONObject userData = content.getJSONObject("user");
            JSONObject authorization =  content.getJSONObject("authorization");
            JSONArray categorieData = content.getJSONArray("categories");

            username = userData.getString("firstname");
            loggedinUser = new LoggedInUser(userData.getString("id"),userData.getString("firstname")+" "+userData.getString("lastname"),authorization.getString("accessToken"));

            tabCategori = new Categorie[categorieData.length()];
            for(int i=0; i < categorieData.length(); i++){
                JSONObject catDetail = categorieData.getJSONObject(i);
                tabCategori[i] = new Categorie(catDetail.getString("id"),catDetail.getString("label"),catDetail.getString("img"),catDetail.getString("totalOffers"),catDetail.getString("checked"));
            }

        }catch (JSONException e) {
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
                   // loadingProgressBar.setVisibility(View.INVISIBLE);
                    //Snackbar.make(view, "mot de passe ou username incorrect ", Snackbar.LENGTH_LONG)
                    //      .setAction("Action", null).show();
                    Toast.makeText(getApplicationContext(), "mot de passe ou username incorrect ", Toast.LENGTH_LONG).show();
                    System.out.println("ok erreur 401");
                }else{

                    if (obj.getString("status").compareTo("200") == 0 ){
                        // Intent intent = new Intent(getApplicationContext() , OffresActivity.class);
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


}
