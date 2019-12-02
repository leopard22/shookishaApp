package com.example.shookisha.ui.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shookisha.R;
import com.example.shookisha.adapter.InfosRecyclerViewAdapter;
import com.example.shookisha.entity.Infodetail;
import com.example.shookisha.entity.Infos;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.UserDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class InfoFragment extends Fragment implements InfosRecyclerViewAdapter.ItemClickListener{

    private UserDataSource shookisher;
    private Activity context;
    private LinearLayout infosLinearLayout;
    private RecyclerView recyclerViewInfos;
    private TextView labelTitrePageInfos;
    private ScrollView infoDetailScrollView;
    private ImageView infoDetailImage;
    private TextView infoDetailContent;
    private ProgressBar loadInfosProgressBar;
    private ArrayList<Infos> infos;
    private Infodetail infodetail;
    private InfosRecyclerViewAdapter infosRecyclerViewAdapter;
    private Api api;

    public static InfoFragment newInstance(String user){

        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("infoFragment :: onCreate()");
        String userReceive = null;

        if (getArguments() != null) {
            System.out.println("je suis passé par ici getArguments() OffreFragment");

            userReceive = getArguments().getString("user");
            shookisher = new UserDataSource(userReceive);
        }

        api = new Api();

       /* TextView title =  getActivity().findViewById(R.id.title);
        title.setText(" ");*/
        ImageView logo = getActivity().findViewById(R.id.logoShookisha);
        logo.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         context = getActivity();
        View root = inflater.inflate(R.layout.activity_infos, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadInfosProgressBar = view.findViewById(R.id.loadInfosProgressBar);
        recyclerViewInfos = view.findViewById(R.id.recyclerViewInfos);
        recyclerViewInfos.setHasFixedSize(true);
        infosLinearLayout = view.findViewById(R.id.infosLinearLayout);
        infoDetailScrollView = view.findViewById(R.id.infoDetailScrollView);
        labelTitrePageInfos = view.findViewById(R.id.labelTitrePageInfos);
        infoDetailImage = view.findViewById(R.id.infoDetailImage);
        infoDetailContent = view.findViewById(R.id.infoDetailContent);

        infoDetailContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("infoDetailScrollView", "infosdetail called from infosfragment");
                System.out.println("setOnClickListener :: method");
                showInfosDetail(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("infoFragment :: onStart()");
        new InfosTask().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("infoFragment :: onStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("infoFragment :: onResume()");
    }

    private class InfosTask extends AsyncTask<Void,Void,String>{

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {

            Api api = new Api();
            String resultApi="";

            try{
                resultApi = api.getAuth("infos",shookisher.getUser().getLoggedInUser().getToken());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadInfosProgressBar.setVisibility(View.GONE);
            Log.i("Log_infostask", "infos called from infosfragment");
            System.out.println(" reponse server "+ s );

            try{
                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").compareTo("200") == 0){
                    JSONArray infosData = obj.getJSONArray("content");
                    parseInfos(infosData);
                }else{
                    Toast.makeText(context.getApplicationContext(), "Veuillez vous connecter à internet ", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseInfos(JSONArray infosData){
        try{
            System.out.println("parseInfos :: method");
            ObjectMapper mapper = new ObjectMapper();
            infos = mapper.readValue(infosData.toString(), new TypeReference<ArrayList<Infos>>(){});
            setupRecycler(infos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void setupRecycler(ArrayList<Infos> infosData){
        infosRecyclerViewAdapter = new InfosRecyclerViewAdapter(new ArrayList<>(infosData),context.getApplicationContext());
        infosRecyclerViewAdapter.setClickListener(this);
        recyclerViewInfos.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewInfos.setAdapter(infosRecyclerViewAdapter);
        recyclerViewInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("recyclerViewInfos :: method setOnItemClickListener");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("You clicked number " + infosRecyclerViewAdapter.getItem(position).getIdInfos()+" "+ infosRecyclerViewAdapter.getItem(position).getLabelInfos()+ ", which is at cell position " + position);
        new InfoDetailTask().execute(infosRecyclerViewAdapter.getItem(position).getIdInfos());
    }

    private class InfoDetailTask extends AsyncTask<Integer, Void,String>{

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Api api = new Api();
            String resultApi="";

            String jsonStr = "{"+ "\"objectId\": "+integers[0]+" } ";

            try{
                resultApi = api.postAuth("infodetail",jsonStr,shookisher.getUser().getLoggedInUser().getToken());
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
                if (obj.getString("status").compareTo("200") == 0){
                    String infosDetailData = obj.getString("content");
                    parseInfoDetail(infosDetailData);
                }else{
                    Toast.makeText(context.getApplicationContext(), "Veuillez vous connecter à internet ", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void parseInfoDetail(String infosDetailData){
        try{
            Log.i("Log_infoDetailtask", "infosdetail called from infosfragment");
            System.out.println("parseInfos :: method");
            ObjectMapper mapper = new ObjectMapper();
            infodetail = mapper.readValue(infosDetailData, new TypeReference<Infodetail>(){});
            System.out.println(infodetail.getLabel());
            infosdetails(infodetail);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void infosdetails(Infodetail infodetail){
        labelTitrePageInfos.setText(infodetail.getLabel());
        Picasso.get().load(api.getBaseUrlImg()+infodetail.getImage()).into(infoDetailImage);
        infoDetailContent.setText(infodetail.getContent());
        showInfosDetail(true);
    }

    public void showInfosDetail(boolean show){
        if(show){
            infosLinearLayout.setVisibility(View.GONE);
            infoDetailScrollView.setVisibility(View.VISIBLE);
        }else {
            labelTitrePageInfos.setText(getString(R.string.label_titre_page_infos));
            infosLinearLayout.setVisibility(View.VISIBLE);
            infoDetailScrollView.setVisibility(View.GONE);
        }

    }


}//fin class InfoFragment
