package com.example.shookisha.ui.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shookisha.PerimetreActivity;
import com.example.shookisha.R;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.ToolsUtility;
import com.example.shookisha.utility.UserDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfilFragment extends Fragment {

    private Activity context;
    private UserDataSource shookisher;
    private ScrollView mainFragmentProfil;
    private ToolsUtility toolsUtility;

    public static ProfilFragment newInstance(String user){

        System.out.println("ProfilFragment :: newInstance()");
        ProfilFragment fragment = new ProfilFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ProfilFragment :: onCreate()");

        toolsUtility = new ToolsUtility();
        context = getActivity();
        String userReceive=null;

        if (getArguments() != null) {
            userReceive = getArguments().getString("user");
            shookisher = new UserDataSource(userReceive);
        }

       /* TextView title =  getActivity().findViewById(R.id.title);
        title.setText("MON PROFIL");*/
        ImageView logo = getActivity().findViewById(R.id.logoShookisha);
        logo.setVisibility(View.VISIBLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("ProfilFragment :: onCreateView()");

        // context = getActivity();
        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        //TextView textView = root.findViewById(R.id.achatTtextView);


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        System.out.println("ProfilFragment :: onViewCreated()");
        FloatingActionButton fab = view.findViewById(R.id.logout);
        TextView p_nomPrenomText = view.findViewById(R.id.p_nomPrenomText);
        Button validerProfil = view.findViewById(R.id.p_validerProfil);
       /* EditText jour = view.findViewById(R.id.p_jourEditText);
        EditText mois = view.findViewById(R.id.p_moisEditText);
        EditText annee = view.findViewById(R.id.p_anneeditText);*/
        EditText nom = view.findViewById(R.id.p_nomEditText);
        EditText prenom = view.findViewById(R.id.p_prenomEditText);
        EditText mdp2 = view.findViewById(R.id.p_mdpEditText2);
        EditText mail = view.findViewById(R.id.p_emailEditText);
        EditText mdp = view.findViewById(R.id.p_mdpEditText);
        TextView bornAt = view.findViewById(R.id.p_bornat);
        mainFragmentProfil = view.findViewById(R.id.mainFragmentProfil);


bornAt.setText( toolsUtility.dateToString( Long.parseLong(shookisher.getUser().getConsommateur().getBornAt()) ) );
        p_nomPrenomText.setText(shookisher.getUser().getLoggedInUser().getDisplayName());
        prenom.setText(shookisher.getUser().getConsommateur().getFirstname());
        nom.setText(shookisher.getUser().getConsommateur().getLastname());
        mail.setText(shookisher.getUser().getConsommateur().getEmail());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutTask().execute();
            }
        });
    }

    private class LogoutTask extends AsyncTask<String,Void,String> {
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
                resultApi = api.getAuth("logout",shookisher.getUser().getLoggedInUser().getToken());
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
            exitShookisha(s);
        }
    }

    private void exitShookisha(String s){


        try{
            JSONObject obj = new JSONObject(s);
            String message ="vous etes deconnecté";

            if (obj.getString("status").compareTo("200") == 0 ){
                Snackbar.make(mainFragmentProfil, message, Snackbar.LENGTH_LONG).show();
                context.finishAffinity();
                System.exit(0);
            }else{
                System.out.println(" ne fonctionne pas" );
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ProfilFragment :: onStart()");

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ProfilFragment :: onStop()");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ProfilFragment :: onResume()");
    }


}
