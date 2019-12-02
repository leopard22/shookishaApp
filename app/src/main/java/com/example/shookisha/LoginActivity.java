package com.example.shookisha;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shookisha.shared.Api;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private EditText usernameEditText;
    private EditText passwordEditText,emailForgetEditText;
    private Button loginButton;
    private Button forgetMdpButton;
    private Button newCompteButton;
    private Button btnForgetSend;
    private Button btnForgetCancel;
    private SharedPreferences sharedPref;
    private LinearLayout connexionLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        forgetMdpButton = findViewById(R.id.forgetMdp);
        newCompteButton = findViewById(R.id.newCompte);
        loadingProgressBar = findViewById(R.id.loading);
        btnForgetSend = findViewById(R.id.btnForgetSend);
        btnForgetCancel = findViewById(R.id.btnForgetCancel);
        emailForgetEditText = findViewById(R.id.emailForgetEditText);
        connexionLayout = findViewById(R.id.connexionLayout);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidChar( s.toString())){
                    System.out.println("n\'est pas alphabet ::"+s);

                }
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        emailForgetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!isValidMail(s.toString())){
                    System.out.println("pas valide mail");
                    emailForgetEditText.setTextColor(getResources().getColor(R.color.colorDanger));
                }
                if(isValidMail(s.toString()))
                    emailForgetEditText.setTextColor(getResources().getColor(R.color.vertShookisha));

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyer(v);
            }
        });

        forgetMdpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout connexionLayout = findViewById(R.id.connexionLayout);
                LinearLayout forgetMdpLayout = findViewById(R.id.forgetMdpLayout);
                connexionLayout.setVisibility(View.GONE);
                forgetMdpLayout.setVisibility(View.VISIBLE);

                /* Intent intent = new Intent(getApplicationContext(), PerimetreActivity.class);
                startActivity(intent);*/
            }
        });

        newCompteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , ProfilActivity.class);
                intent.putExtra("newcompte",true);
                startActivity(intent);
            }
        });

        btnForgetSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(emailForgetEditText.getText()) && isValidMail(emailForgetEditText.getText().toString())) {
                    emailForgetEditText.clearFocus();
                    new UpdateMdp().execute(emailForgetEditText.getText().toString());
                }else{
                   // Toast.makeText(getApplicationContext(), getString(R.string.login_empty_email), Toast.LENGTH_LONG).show();
                    Snackbar.make(v, getString(R.string.login_empty_email), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    emailForgetEditText.requestFocus();
                    if(emailForgetEditText.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }

            }
        });

        btnForgetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout connexionLayout = findViewById(R.id.connexionLayout);
                LinearLayout forgetMdpLayout = findViewById(R.id.forgetMdpLayout);
                connexionLayout.setVisibility(View.VISIBLE);
                forgetMdpLayout.setVisibility(View.GONE);

            }
        });


        //emailForgetEditText.onKeyDown(KeyEvent.KEYCODE_ENTER, K){}
        /*emailForgetEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

              /*  if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    envoyer();
                }

                if(keyCode == KeyEvent.KEYCODE_A ){
                  //  envoyer();
                    Toast.makeText(getApplicationContext(), "mot de passe ou username incorrect "+KeyEvent.KEYCODE_A, Toast.LENGTH_LONG).show();
                    System.out.println("keyboard click "+KeyEvent.KEYCODE_A);
                }

                return true;
            }
        });*/

    }

    private void envoyer(View view){
        if (!isConnected()) {

            Snackbar.make(view, getString(R.string.message_no_connexion), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if(isFilled(usernameEditText,passwordEditText)){
            loadingProgressBar.setVisibility(View.VISIBLE);
            new ConnexionTask().execute(usernameEditText.getText().toString(),passwordEditText.getText().toString() );
        }else{
          //  Toast.makeText(getApplicationContext(), "mot de passe ou username incorrect ", Toast.LENGTH_LONG).show();
            Snackbar.make(view, getString(R.string.login_password_empty), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private Boolean isFilled(EditText usernameEditText, EditText passwordEditText){

        if(TextUtils.isEmpty(usernameEditText.getText()) || TextUtils.isEmpty(passwordEditText.getText()) || usernameEditText.getText().toString().equals(" ")
                || passwordEditText.getText().toString().equals(" ") ){

            System.out.println("pas bon");
            return false;
        }
        return true;
    }

    private Boolean isValidChar(String s){

        Pattern pattern = Pattern.compile("[A-Za-z ]{1,100}");
        String reg ="[A-Za-z ]{1,100}";

        System.out.println(s.toString()+" <= matches => "+pattern.matches(reg,s));

        return pattern.matches(reg,s);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private Boolean isValidMail(String m){

        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$");
        String reg ="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$";
        System.out.println(m+" <= bon adresse mail => "+pattern.matches(reg,m));
        return pattern.matches(reg,m);
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


                if(obj.getString("status").compareTo("413") == 0 || obj.getString("status").compareTo("408") == 0 || obj.getString("status").compareTo("504") == 0){
                    System.out.println("connection :: timeout");
                    Snackbar.make(connexionLayout, getString(R.string.login_timeout), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    if(obj.getString("status").compareTo("401") == 0 || obj.getString("status").compareTo("400") == 0){
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(connexionLayout, getString(R.string.invalid_username_password), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        //Toast.makeText(getApplicationContext(),  getString(R.string.invalid_username_password), Toast.LENGTH_LONG).show();
                        System.out.println("ok erreur 401");
                    }else{
                        String status = obj.getString("status");
                        System.out.println(status);


                        if (status.compareTo("200") == 0 ){

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.username_or_email),usernameEditText.getText().toString()).apply();
                            editor.putString(getString(R.string.passwor),passwordEditText.getText().toString()).apply();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext() , PagesActivity.class);
                            intent.putExtra("user", s);
                            startActivity(intent);
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateMdp extends AsyncTask<String,Void,String >{

        @Override
        protected String doInBackground(String... strings) {

            String mail = "{"+"\"email\": \""+strings[0]+ "\""+"}";
            String resultApi = "null";

            Api api = new Api();
            System.out.println(mail);
            try{
                resultApi = api.put("auth/renewalpassword",mail);
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
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), getString(R.string.message_email_incorrect), Toast.LENGTH_LONG).show();
                    System.out.println("ok erreur 401");
                }else{
                    JSONObject status = obj.getJSONObject("status");

                    String code = status.getString("success");
                    String message = status.getString("message");

                    if (code.compareTo("true") == 0 ){
                        Toast.makeText(getApplicationContext(), getString(R.string.message_mdp_send), Toast.LENGTH_LONG).show();
                        LinearLayout forgetMdpLayout = findViewById(R.id.forgetMdpLayout);
                        forgetMdpLayout.setVisibility(View.GONE);
                        LinearLayout connexionLayout = findViewById(R.id.connexionLayout);
                        connexionLayout.setVisibility(View.VISIBLE);
                    }
                    if (code.compareTo("false") == 0 ){
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        LinearLayout forgetMdpLayout = findViewById(R.id.forgetMdpLayout);
                        forgetMdpLayout.setVisibility(View.GONE);
                        LinearLayout connexionLayout = findViewById(R.id.connexionLayout);
                        connexionLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

  /**/  @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){

      switch (keyCode) {
            case KeyEvent.KEYCODE_A: System.out.println("keyboard click "+KeyEvent.KEYCODE_A);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

}
