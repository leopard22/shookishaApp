package com.example.shookisha.ui.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.R;
import com.example.shookisha.adapter.PanierRecyclerViewAdapter;
import com.example.shookisha.entity.Bascket;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.UserDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class PanierFragment extends Fragment {

    private UserDataSource shookisher;
    private RecyclerView panierRecyclerView;
    private ArrayList<Bascket> basckets;
    private PanierRecyclerViewAdapter panierRecyclerViewAdapter;
    private Activity context;

    public static PanierFragment newInstance(String user){

        PanierFragment fragment = new PanierFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userReceive="";
        System.out.println("PanierFragment :: onCreate()");

        if (getArguments() != null) {

            userReceive = getArguments().getString("user");
            shookisher = new UserDataSource(userReceive);
        }

        /*ImageView logo = getActivity().findViewById(R.id.logoShookisha);
        logo.setVisibility(View.VISIBLE);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View root = inflater.inflate(R.layout.fragment_panier, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        panierRecyclerView = view.findViewById(R.id.panierRecyclerView);
        panierRecyclerView.setHasFixedSize(true);


    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("PanierFragment :: onStart()");
        new PanierTask().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("PanierFragment :: onStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Log_infostask", "panier called from panierfragment");
        System.out.println("PanierFragment :: onResume()");
        new PanierTask().execute();
    }

    private class PanierTask extends AsyncTask<String,Integer,String>{

        private Api api ;
        private String token = shookisher.getUser().getLoggedInUser().getToken();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("Log_infostask", "panier called from panierfragment");
            System.out.println("requete lancé");

            api = new Api();

            String resultApi="";

            try{
                resultApi = api.getAuth("bascket",token);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Log_infostask", "panier called from panierfragment");
            System.out.println(s);
            try{
                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").compareTo("200") == 0){
                    JSONArray bascketData = obj.getJSONArray("content");
                    parseBasket(bascketData);
                }else{
                    //Toast.makeText(context.getApplicationContext(), "Veuillez vous connecter à internet ", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseBasket(JSONArray bascketData){

        try{
            ObjectMapper mapper = new ObjectMapper();
            basckets = mapper.readValue(bascketData.toString(), new TypeReference<ArrayList<Bascket>>(){});
            setupRecycler(basckets);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupRecycler(ArrayList<Bascket> bascketsList){
        panierRecyclerViewAdapter = new PanierRecyclerViewAdapter(new ArrayList<>(bascketsList) , context.getApplicationContext());
        panierRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        panierRecyclerView.setAdapter(panierRecyclerViewAdapter);
    }

}
