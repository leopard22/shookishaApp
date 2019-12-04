package com.example.shookisha.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;


import com.example.shookisha.OffreDetailActivity;
import com.example.shookisha.OffreRVAdapter;
import com.example.shookisha.R;
import com.example.shookisha.adapter.CategorieAdapter;
import com.example.shookisha.entity.Categorie;
import com.example.shookisha.entity.Offre;
import com.example.shookisha.entity.ResultFilter;
import com.example.shookisha.shared.Api;
import com.example.shookisha.utility.UserDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OffreFragment extends Fragment implements AdapterView.OnItemSelectedListener,
                                                        OffreRVAdapter.ItemClickListener,
                                                        GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener,
                                                        com.google.android.gms.location.LocationListener{
    //attribut pour la page offre
    private static final String TAG = "OffreFragment";
    private ProgressBar loadingProgressBar;
    private ArrayList<Offre> offers;
    private ArrayList<Offre> dataModelList;
    private RecyclerView offreRecyclerView;
    private OffreRVAdapter offreRVAdapter;
    private Spinner categorieSpinner;
    private ArrayList<ResultFilter>  resultFilters;
    private CategorieAdapter categorieAdapter;
    private Activity context;
    private UserDataSource shookisher;
    private List<Categorie> categorieList;
    private Location longitudeLatitude;
    private HashMap<String,Integer> filterCatList;

    //attribut pour permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 50 secs */
    private long FASTEST_INTERVAL = 40000; /* 40 sec */
    private boolean firstRun = true;
    private boolean refresh = false;
    private SwipeRefreshLayout swiperefresh;
    private ImageButton sortByPriceUp;
    private ImageButton sortByPriceDown;
    private boolean selectedTout = false;
    private boolean firstTimeConnected = true;
    private boolean itemSpinnerSelected = true;
    private boolean spinnerTouched = false;

    /**
     *
     * @param user
     * @return
     */
    public static OffreFragment newOffreFragment(String user){

        OffreFragment fragment = new OffreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("OffreFragment :: onCreate()");

        String userReceive=null;

        filterCatList = new HashMap<>();

        filterCatList.put("cat",0);
        filterCatList.put("priceup",0);
        filterCatList.put("pricedown",0);

        //permission requis
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (permissionsToRequest.size() > 0){
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // google api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        System.out.println(getArguments());
        if (getArguments() != null) {
            System.out.println("je suis passé par ici getArguments() OffreFragment");

            userReceive = getArguments().getString("user");

            shookisher = new UserDataSource(userReceive);
            categorieList = new ArrayList<Categorie>();
            for(int i=0; i < shookisher.getUser().getCategorie().length;i++){
                if(shookisher.getUser().getCategorie()[i].getChecked().compareTo("true") == 0){
                    categorieList.add(shookisher.getUser().getCategorie()[i]);
                }
            }
        }
        System.out.println("Fragment OFFRE ::");
       // System.out.println("longitude :"+ longitudeLatitude.getLongitude()+" latitude :"+longitudeLatitude.getLatitude());
        System.out.println("utilisateur ::"+userReceive);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View root = inflater.inflate(R.layout.activity_offres, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        categorieSpinner = view.findViewById(R.id.categorieSpinner);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        sortByPriceDown = view.findViewById(R.id.sortByPriceDown);
        sortByPriceUp = view.findViewById(R.id.sortByPriceUp);
        Log.i("Log_onViewCreated", "onViewCreated called from Offrefragment");

        categorieSpinner.setSelection(categorieSpinner.getSelectedItemPosition(),false);
        categorieSpinner.setOnItemSelectedListener(this);

        sortByPriceUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Log_sortByPriceUp", "onclick called from sortByPriceUp");
                filterCatList.put("priceup",1);
                filterCatList.put("pricedown",0);
                new OffersTask().execute( longitudeLatitude.getLatitude(),longitudeLatitude.getLongitude());
            }
        });

        sortByPriceDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Log_sortByPriceDown", "onclick called from sortByPriceDown");
                filterCatList.put("pricedown",1);
                filterCatList.put("priceup",0);
                new OffersTask().execute( longitudeLatitude.getLatitude(),longitudeLatitude.getLongitude());
            }
        });


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("Log_swipeRefresh", "onRefresh called from SwipeRefreshLayout");
                updateOperation();
            }
        });

        offreRecyclerView = view.findViewById(R.id.recyclerView);
        offreRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("select categorie :: Offre :"+adapterView.getItemAtPosition(position).toString());
        ResultFilter rf = (ResultFilter) adapterView.getItemAtPosition(position) ;

        if(itemSpinnerSelected) {
            itemSpinnerSelected = false;
        }else {
            filterCatList.put("cat",rf.getCatId());
            new OffersTask().execute( longitudeLatitude.getLatitude(),longitudeLatitude.getLongitude());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(View view, int position) {
        detailOffreActivity(offreRVAdapter.getItem(position));
        System.out.println("You clicked number " + offreRVAdapter.getItem(position).getOfferTitle() + ", which is at cell position " + position);
    }

    private void detailOffreActivity(Offre o){

        Intent intent = new Intent(context, OffreDetailActivity.class);
        intent.putExtra("uneOffre", o);
        intent.putExtra("token",shookisher.getUser().getLoggedInUser().getToken());
        intent.putExtra("from","offreFragment");
        startActivity(intent);
    }
/*
* requette pour recevoir la liste des offres.
* reçois en paramettre les coordonnée longitude et latitude
* */

    /**
     *
     */
    private class OffersTask extends AsyncTask<Double,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Double... strings) {

            Api api = new Api();
            String resultApi="";
            int sort = 0;
            double coordX = strings[0] ;
            double coordY =strings[1];
            ArrayList filterCat = new ArrayList();
            String jsonStr ;


            if(filterCatList.get("pricedown") == 1){
               sort = 1;
            }


            if(filterCatList.get("priceup") == 1){
                sort = 2;
            }

            filterCat.add(0, filterCatList.get("cat")) ;

           /* if(strings.length > 3){

            }else{
                filterCat = myCategories();
            }*/


            System.out.println("OffresActivity Doinbackground :: coordX = "+coordX+" coordy = "+coordY);

            for(int i=0; i < filterCat.size();i++){
                System.out.println(filterCat.get(i));
            }

            switch (sort){
                case 1 : jsonStr = "{"+  "\"filterCat\": "+filterCat+", \"sortByPriceDown\": "+true+", \"userCoordX\": "+coordX+ ","+" \"userCoordY\": "+ coordY+" } ";
                    break;
                case 2 : jsonStr = "{"+  "\"filterCat\": "+filterCat+", \"sortByPriceUp\": "+true+", \"userCoordX\": "+coordX+ ","+" \"userCoordY\": "+ coordY+" } ";
                    break;
                default:jsonStr = "{"+  "\"filterCat\": "+filterCat+", \"userCoordX\": "+coordX+ ","+" \"userCoordY\": "+ coordY+" } ";
            }

            System.out.println(jsonStr);

            try{
                resultApi = api.postAuth("offers",jsonStr,shookisher.getUser().getLoggedInUser().getToken());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return resultApi;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadingProgressBar.setVisibility(View.GONE);
            System.out.println(" reponse server "+ s );
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getString("status").compareTo("null") == 0 && obj.getString("totalElements").compareTo("0") == 0 ){
                    loadingProgressBar.setVisibility(View.GONE);
                    Snackbar.make(getView().findViewById(R.id.mainContent), R.string.message_resultat_list_offre,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    //Toast.makeText(context.getApplicationContext(), "Veuillez choisir vos préférences ", Toast.LENGTH_LONG).show();

                }

                if (obj.getString("status").compareTo("401") == 0  || obj.getString("status").compareTo("403") == 0 || obj.getString("status").compareTo("404") == 0) {
                    loadingProgressBar.setVisibility(View.GONE);
                    System.out.println("ok");
                }else{
                    System.out.println(" tout est ok ");
                    JSONArray offreData = obj.getJSONArray("content");
                    JSONArray filterData = obj.getJSONArray("resultFilter");
                    setDataOffer(offreData);
                    if(firstTimeConnected){
                        setDataResultFilter(filterData);
                        firstTimeConnected = false;
                    }

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param offreData
     */
    public void setDataOffer(JSONArray offreData){

        try{
            ObjectMapper mapper = new ObjectMapper();
            offers = mapper.readValue(offreData.toString(), new TypeReference<ArrayList<Offre>>(){});
            System.out.println("setDataoffer :: method");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //setGridView();
        setupRecycler();
    }

    /**
     *
     * @return
     */
    private ArrayList<Offre>getDataOffre(){
        return offers;
    }

    private void setupRecycler(){
        dataModelList = getDataOffre();

        offreRVAdapter = new OffreRVAdapter(context.getApplicationContext(), new ArrayList<>(dataModelList) );
        offreRVAdapter.setClickListener(this);

        offreRecyclerView.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 2));

        offreRecyclerView.setAdapter(offreRVAdapter);
        offreRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("offreRecyclerView :: method setOnItemClickListener");
            }
        });

    }

    private ArrayList<String>permissionsToRequest(ArrayList<String> wantedPermissions){
        ArrayList<String> result = new ArrayList<>();

        for(String perm : wantedPermissions){
            if(!hasPermission(perm)){
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permssion){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return getActivity().checkSelfPermission(permssion) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    //code localisation
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            longitudeLatitude = mLocation;
        } else {
            Snackbar.make(getView().findViewById(R.id.mainContent), R.string.derniere_position,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
        System.out.println("onConnectionSuspended method :: suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        System.out.println("onConnectionFailed method :: fail");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("OffreFragment :: onStart()");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("OffreFragment :: onStop()");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        firstTimeConnected = true;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  updateOperation();
        if(longitudeLatitude != null){
            refresh = true;
            new OffersTask().execute( longitudeLatitude.getLatitude() ,longitudeLatitude.getLongitude());
        }
        System.out.println("OffreFragment :: onResume()");
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("reque", "--->>>>");

    }

    @Override
    public void onLocationChanged(Location location) {
        longitudeLatitude = location;
        if((location != null) && (firstRun == true)){
            System.out.println("OffreFragment:: (location != null) && (firstRun == true) ");
            firstRun = false;
            new OffersTask().execute( location.getLatitude() ,  location.getLongitude());
            System.out.println("OffreFragment:: onLocationChanged:  longitude :"+ longitudeLatitude.getLongitude()+" latitude :"+longitudeLatitude.getLatitude());
        }
        if(location != null && refresh != false){

            System.out.println("OffreFragment:: location != null ");
            new OffersTask().execute(location.getLatitude() , location.getLongitude());
            System.out.println("OffreFragment:: onLocationChanged:  longitude :"+ longitudeLatitude.getLongitude()+" latitude :"+longitudeLatitude.getLatitude());
        }
        //if(location)
        refresh = false;
        System.out.println("OffreFragment:: onLocationChanged() ");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(getActivity().getApplicationContext()).
                                    setMessage("Activer la localisation pour utiliser cette application").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }

                } else {
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.connect();
                    }
                }

                break;
        }
    }

    private void updateOperation(){
        refresh = true;
        new OffersTask().execute(longitudeLatitude.getLatitude() ,longitudeLatitude.getLongitude());
        swiperefresh.setRefreshing(false);
    }

    private ArrayList<String> myCategories(){
        ArrayList<String> myCl = new ArrayList<>();
        for(int i=0; i < categorieList.size();i++ ){
            if(categorieList.get(i).getChecked().compareTo("true") == 0 ){
               myCl.add(categorieList.get(i).getId());
            }
        }
       return myCl;
    }

    private void setDataResultFilter(JSONArray filterData){
        try{
            ObjectMapper mapper = new ObjectMapper();
            resultFilters = mapper.readValue(filterData.toString(), new TypeReference<ArrayList<ResultFilter>>(){});
            System.out.println("setDataResultFilter :: method");
            setFilterSpinner();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setFilterSpinner(){
        categorieAdapter = new CategorieAdapter(context,resultFilters);
        categorieSpinner.setAdapter(categorieAdapter);
        //categorieSpinner.setSelection(0);

    }

}
