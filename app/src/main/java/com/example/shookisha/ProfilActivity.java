package com.example.shookisha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shookisha.entity.Consommateur;
import com.example.shookisha.shared.Api;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class ProfilActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Consommateur shookisheur;
    String[] genre ={"Homme","Femme","Autre"};
    private LinearLayout mainLayout;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        Boolean newcompte = intent.getBooleanExtra("newcompte",false);

        setContentView(R.layout.activity_profil);

        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mainLayout =  findViewById(R.id.mainActivityLayout);

        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        cacherProfilDiv(newcompte);

        final Button validerProfil = findViewById(R.id.validerProfil);
        EditText jour = findViewById(R.id.jourEditText);
        EditText mois = findViewById(R.id.moisEditText);
        EditText annee = findViewById(R.id.anneeditText);
        EditText nom = findViewById(R.id.nomEditText);
        EditText prenom = findViewById(R.id.prenomEditText);
        EditText mdp2 = findViewById(R.id.mdpEditText2);
        EditText mail = findViewById(R.id.emailEditText);
        EditText mdp = findViewById(R.id.mdpEditText);
        shookisheur = new Consommateur();

        Spinner genreSpinner = findViewById(R.id.genreSpinner);
        genreSpinner.setOnItemSelectedListener(this);

        ArrayAdapter genreAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,genre);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        /* listener section */

        nom.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidChar( s.toString())){
                    System.out.println("n\'est pas alphabet ::"+s);
                    nom.setTextColor(getResources().getColor(R.color.colorDanger));
                }
                if(isValidChar( s.toString()))
                    nom.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        prenom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable e) {
                if(!isValidChar( e.toString())){
                    System.out.println("n\'est pas alphabet ::"+e);
                    prenom.setTextColor(getResources().getColor(R.color.colorDanger));
                }
                if(isValidChar(e.toString()))
                    prenom.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        jour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString()) ){
                    return;
                }
                if(!isValideDay(s.toString()) ){
                    System.out.println("pas bonne date");
                    if(s.length() == 2)
                        s.clear();
                }
            }
        });

        mois.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString()) ){
                    return;
                }
                if(!isValideMonth(s.toString()) ){
                    System.out.println("pas bonne mois");
                    if(s.length() == 2)
                        s.clear();
                }
            }
        });

        annee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if(TextUtils.isEmpty(c.toString()) ){
                    return;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString()) ){
                    return;
                }
                if(!isValideYear(s.toString()) ){
                    System.out.println("pas bonne annee");
                    if(s.length() == 4)
                        s.clear();
                }
            }
        });

        mdp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if(TextUtils.isEmpty(c.toString()) ){
                    return;
                }
                if(!confirmMdp(c.toString())){
                    System.out.println("pas bon");
                    mdp2.setTextColor(getResources().getColor(R.color.colorDanger));
                }
                if(confirmMdp(c.toString()))
                    mdp2.setTextColor(getResources().getColor(R.color.vertShookisha));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable e) {
                if(!isValidMail(e.toString())){
                    System.out.println("pas valide mail");
                    mail.setTextColor(getResources().getColor(R.color.colorDanger));
                }
                if(isValidMail(e.toString()))
                    mail.setTextColor(getResources().getColor(R.color.vertShookisha));
            }
        });

        validerProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    Toast.makeText(getApplicationContext(), "Aucune connexion à internet.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(isFilled(nom,prenom,jour,mois,annee,mail,mdp,mdp2)){
                    setNewConsommateur();
                    new SaveTask().execute(shookisheur);
                }

            }
        });

    }



    private void cacherProfilDiv(Boolean nouveau){

        LinearLayout divInfo = findViewById(R.id.infosUserDiv);
        LinearLayout divPersonne = findViewById(R.id.p_nomDateSexeDiv);
        LinearLayout divContact = findViewById(R.id.emailMdpDiv);

        if(nouveau == true){
            divInfo.setVisibility(View.GONE);
        }else{
            divPersonne.setVisibility(View.GONE);
            divContact.setVisibility(View.GONE);
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("select position ::"+i);
        System.out.println("select item :: "+genre[i] );
        System.out.println("parent.getItemAtPosition(pos) ::"+adapterView.getItemAtPosition(i));
        shookisheur.setGenId(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("onNothingSelected ::"+adapterView);
    }

    private class SaveTask extends AsyncTask<Consommateur,Void,String >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            System.out.println("onpreexecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            System.out.println("onprogress");
        }

        @Override
        protected String doInBackground(Consommateur... consommateurs) {
            System.out.println("doinbackground");
            String user = consommateurs[0].toString();
            ObjectMapper Obj = new ObjectMapper();
            Api api = new Api();
            String resultApi="";

               try {
                   // transform consommateur json string
                   String jsonStr = Obj.writeValueAsString( consommateurs[0]);

                   // Displaying JSON String
                   System.out.println("jackson APi ::");

                   System.out.println(jsonStr);

                   resultApi = api.post("auth/signup",jsonStr);
                   System.out.println("ResultatApi "+resultApi);

               }
               catch (IOException e) {
                   e.printStackTrace();
               }
               System.out.println("tostring function ::");
               System.out.println(user);
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("onpostExecute :: "+s);
            connect(s);
        }
    }

    private void connect(String s){

        try{
            JSONObject obj = new JSONObject(s);

          String  message = "erreur connexion";

            if (obj.getString("status").compareTo("200") == 0 ){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.username_or_email),shookisheur.getEmail()).apply();
                editor.putString(getString(R.string.passwor),shookisheur.getPassword()).apply();
                Intent intent = new Intent(getApplicationContext() , Preference.class);
                intent.putExtra("user", s);
                intent.putExtra("from","profil");
                startActivity(intent);
            }else{
                Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Boolean isValideDay(String day){

        if((Integer.parseInt(day) <= 31 ) && (Integer.parseInt(day) > 0 ) ){
            System.out.println("bon jour");
            return true;
        }

        return false;
    }

    private Boolean isValideMonth(String month){

        if( (Integer.parseInt(month) <= 12 ) && (Integer.parseInt(month) > 0 )  ){
            System.out.println("bon mois");
            return true;
        }

        return false;
    }

    private Boolean isValideYear(String year){
// annee 2001 = 18
        if( (Integer.parseInt(year) <= 2001) && (Integer.parseInt(year) >= 1940) ){
            System.out.println("bonne date");
            return true;
        }

        return false;
    }

    private Boolean confirmMdp(String mdp2){
        EditText mdp = findViewById(R.id.mdpEditText);
        System.out.println(mdp.getText().toString()+" <=mdp doit etre pareil que =>"+mdp2);
        if(mdp2.compareTo(mdp.getText().toString()) == 0){
            return true;
        }
        return false;
    }

    private Boolean isValidChar(String s){

        Pattern pattern = Pattern.compile("[A-Za-z ]{1,100}");
        String reg ="[A-Za-z ]{1,100}";

        System.out.println(s.toString()+" <= matches => "+pattern.matches(reg,s));

        return pattern.matches(reg,s);
    }

    private Boolean isValidMail(String m){

        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$");
        String reg ="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$";
        System.out.println(m+" <= bon adresse mail => "+pattern.matches(reg,m));
        return pattern.matches(reg,m);
    }

    private Boolean isFilled(EditText nom, EditText prenom, EditText jour, EditText mois, EditText annee,EditText mail, EditText mdp, EditText mdp2){

        if(TextUtils.isEmpty(nom.getText()) || TextUtils.isEmpty(prenom.getText())  || TextUtils.isEmpty(jour.getText())
                || TextUtils.isEmpty(mois.getText()) || TextUtils.isEmpty(annee.getText()) || TextUtils.isEmpty(mail.getText())
                || TextUtils.isEmpty(mdp.getText())  || TextUtils.isEmpty(mdp2.getText()) || nom.getText().toString().equals(" ")
                || prenom.getText().toString().equals(" ") || jour.getText().toString().equals(" ") || mois.getText().toString().equals(" ")
                || annee.getText().toString().equals(" ") || mail.getText().toString().equals(" ") || mdp.getText().toString().equals(" ")
                || mdp2.getText().toString().equals(" ") ){

                System.out.println("pas bon");
                return false;
        }
        return true;
    }

    private Consommateur setNewConsommateur(){
        EditText nom = findViewById(R.id.nomEditText);
        EditText prenom = findViewById(R.id.prenomEditText);
        EditText email = findViewById(R.id.emailEditText);
        EditText mdp = findViewById(R.id.mdpEditText);

        EditText jour = findViewById(R.id.jourEditText);
        EditText mois = findViewById(R.id.moisEditText);
        EditText annee = findViewById(R.id.anneeditText);

        String bornAt = annee.getText()+"-"+mois.getText()+"-"+jour.getText();
        shookisheur.setBornAt(bornAt);
        shookisheur.setEmail(email.getText().toString());
        shookisheur.setFirstname(prenom.getText().toString());
        shookisheur.setLastname(nom.getText().toString());
        shookisheur.setPassword(mdp.getText().toString());
        shookisheur.setUsername(prenom.getText().toString());

        System.out.println("setconsommateur bornat ::"+shookisheur.getGenId()+" "+shookisheur.getBornAt()+" "+shookisheur.getUsername()+" "+shookisheur.getFirstname()+" "+shookisheur.getEmail()+" "+shookisheur.getPassword());
        return new Consommateur();
    }





}
