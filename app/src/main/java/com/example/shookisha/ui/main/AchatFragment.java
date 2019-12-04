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

import com.example.shookisha.R;
import com.example.shookisha.adapter.PurchaseHistoryRVAdapter;
import com.example.shookisha.entity.PurchaseHistory;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.UserDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AchatFragment extends Fragment {

    private UserDataSource shookisher;
    private Activity context;
    private RecyclerView achatRecyclerView;
    private PurchaseHistoryRVAdapter purchaseHistoryRVAdapter;
    private ArrayList<PurchaseHistory> purchaseHistories;


    public static AchatFragment newInstance(String user){
        AchatFragment fragment = new AchatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("AchatFragment :: onCreate()");
        String userReceive="";

        if (getArguments() != null) {

            userReceive = getArguments().getString("user");
            shookisher = new UserDataSource(userReceive);
        }

        ImageView logo = getActivity().findViewById(R.id.logoShookisha);
        logo.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View root = inflater.inflate(R.layout.fragment_achat, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        achatRecyclerView = view.findViewById(R.id.achatRecyclerView);
        achatRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("AchatFragment :: onStart()");
        new AchatTask().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("AchatFragment :: onStop()");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("AchatFragment :: onResume()");
        new AchatTask().execute();
    }


    private class AchatTask extends AsyncTask<String,Integer,String>{
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
                resultApi = api.getAuth("purchaseHistory",token);
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
                    JSONArray purchaseData = obj.getJSONArray("content");
                    parsePurchase(purchaseData);
                }else{
                    //Toast.makeText(context.getApplicationContext(), "Veuillez vous connecter à internet ", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void parsePurchase(JSONArray purchaseData){

        try{
            ObjectMapper mapper = new ObjectMapper();
            purchaseHistories = mapper.readValue(purchaseData.toString(), new TypeReference<ArrayList<PurchaseHistory>>(){});
            setupRecycler(purchaseHistories);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupRecycler(ArrayList<PurchaseHistory> purchaseHistorie){
        purchaseHistoryRVAdapter = new PurchaseHistoryRVAdapter(new ArrayList<>(purchaseHistorie),context.getApplicationContext());
        achatRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        achatRecyclerView.setAdapter(purchaseHistoryRVAdapter);

    }
}
